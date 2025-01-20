package org.poo.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciant.Commerciant;
import org.poo.system.Converter;

import java.util.*;


@Getter @Setter
public class BusinessAccount extends Account {


    public enum ROLE_TYPE {
        OWNER,
        MANAGER,
        EMPLOYEE
    }

    private HashMap<User, ROLE_TYPE> roles;
    private ArrayList<SimpleRecord> transactionReport;
    private ArrayList<CommerciantRecord> commerciantReport;
    private double spendingLimit;
    private double depositLimit;

    public BusinessAccount(final User user, final StringBuilder iban,
                           final StringBuilder currency) {

        super(user, iban, currency);
        setBalance(0);
        this.setType(new StringBuilder("business"));
        roles = new HashMap<>();
        roles.put(user, ROLE_TYPE.OWNER);
        spendingLimit = 500 * Converter.getInstance().convert("RON", currency.toString());
        depositLimit = spendingLimit;

        transactionReport = new ArrayList<>();
        commerciantReport = new ArrayList<>();
    }


    public boolean canDeleteCard(User user, Card card) {
        if (!roles.containsKey(user)) {
            return false;
        }

        if (roles.get(user) == ROLE_TYPE.EMPLOYEE
            && !card.getAccount().getUser().getEmail().toString().contentEquals(user.getEmail())) {
            return false;
        }

        return true;
    }

    public boolean canPay(User user, double amount) {
        if (!roles.containsKey(user)) {
            return false;
        }

        if (roles.get(user) == ROLE_TYPE.EMPLOYEE && amount > spendingLimit) {
            return false;
        }
        return true;
    }


    public boolean canDeposit(User user, double amount) {
        if (!roles.containsKey(user)) {
            System.out.println("AICI");
            return false;
        }

        if (roles.get(user) == ROLE_TYPE.EMPLOYEE && amount > depositLimit) {
            return false;
        }
        return true;
    }

    public void addSpendingTransaction(User user, double amount, Commerciant comm, int timestamp) {

        if (user.getEmail().toString().equals(getUser().getEmail().toString())) {
            return;
        }
        transactionReport.add(new SimpleRecord(user, amount, timestamp));
        if (comm != null) {
            commerciantReport.add(new CommerciantRecord(comm, amount, user, timestamp));
        }

    }

    public ArrayList<SimpleRecord> getTransactionsTimed(final int start, final int end) {
        ArrayList<SimpleRecord> timedTransactions = new ArrayList<>();
        for (SimpleRecord record : transactionReport) {
            if (record.getTimestamp() >= start && record.getTimestamp() <= end
                && !record.getUser().getEmail().toString().contentEquals(getUser().getEmail())) {
                timedTransactions.add(record);
            }
        }
        return timedTransactions;
    }

    public ArrayList<CommerciantRecord> getCommerciantsTimed(final int start, final int end) {
        ArrayList<CommerciantRecord> timedCommerciants = new ArrayList<>();
        for (CommerciantRecord record : commerciantReport) {
            if (record.getTimestamp() >= start && record.getTimestamp() <= end) {
                timedCommerciants.add(record);
            }
        }
        return timedCommerciants;
    }

    public ObjectNode printTransactionsReport(ObjectMapper mapper, int start, int end,
                                              int timestamp) {
        ObjectNode result = mapper.createObjectNode();
        double totalSpending = 0, totalDeposit = 0;

        HashMap<User, Pair<Double, Double>> report = new HashMap<>();



        ArrayList<SimpleRecord> transactions = getTransactionsTimed(start, end);
        System.out.println(transactionReport.size());
        for (SimpleRecord record : transactions) {
            User user = record.getUser();
            Double amount = record.getAmount();
            if (!report.containsKey(user)) {
                report.put(user, new Pair<>(0., 0.));
            }
            if (amount > 0) {
                totalDeposit += amount;

                Pair<Double, Double> pair = report.get(user);
                pair.setSecond(pair.getSecond() + amount);
                report.put(user, pair);

            } else {
                amount = -amount;
                totalSpending += amount;

                Pair<Double, Double> pair = report.get(user);
                pair.setFirst(pair.getFirst() + amount);
                report.put(user, pair);
            }
        }

        result.put("command", "businessReport");
        result.put("timestamp", timestamp);

        ObjectNode output = mapper.createObjectNode();

        output.put("IBAN", getIban().toString());
        output.put("balance", getBalance());
        output.put("currency", getCurrency().toString());
        output.put("spending limit", getSpendingLimit());
        output.put("deposit limit", getDepositLimit());
        output.put("statistics type", "transaction");
        output.put("total spent", totalSpending);
        output.put("total deposited", totalDeposit);

        ArrayNode managers = mapper.createArrayNode();
        ArrayNode employees = mapper.createArrayNode();

        ArrayList<User> managerUsers = new ArrayList<>();
        ArrayList<User> employeeUsers = new ArrayList<>();


        for (Map.Entry<User, ROLE_TYPE> entry : roles.entrySet()) {
            if (entry.getValue() == ROLE_TYPE.EMPLOYEE) {
                employeeUsers.add(entry.getKey());
            } else if (entry.getValue() == ROLE_TYPE.MANAGER) {
                managerUsers.add(entry.getKey());
            }
        }



        managerUsers.sort((a, b) -> -a.getSurname().toString().compareTo(b.getSurname().toString()));
        employeeUsers.sort((a, b) -> a.getSurname().toString().compareTo(b.getSurname().toString()));

        for (User user : managerUsers) {
            ObjectNode manager = mapper.createObjectNode();
            manager.put("username", user.getSurname().toString() + " " +  user.getFirstName().toString());
            if (report.containsKey(user)) {
                manager.put("spent", report.get(user).getFirst());
                manager.put("deposited", report.get(user).getSecond());
            } else {
                manager.put("spent", 0.);
                manager.put("deposited", 0.);
            }
            managers.add(manager);
        }


        for (User user : employeeUsers) {
            ObjectNode employee = mapper.createObjectNode();
            employee.put("username", user.getSurname().toString() + " " +  user.getFirstName().toString());
            if (report.containsKey(user)) {
                employee.put("spent", report.get(user).getFirst());
                employee.put("deposited", report.get(user).getSecond());
            } else {
                employee.put("spent", 0.);
                employee.put("deposited", 0.);
            }
            employees.add(employee);
        }
        output.put("managers", managers);
        output.put("employees", employees);
        result.put("output", output);
        return result;
    }

    public ObjectNode printCommerciantReport(ObjectMapper mapper, int start, int end, int timestamp) {

        ObjectNode result = mapper.createObjectNode();
        ArrayList<CommerciantRecord> timedCommerciants = getCommerciantsTimed(start, end);
        result.put("command", "businessReport");
        result.put("timestamp", timestamp);

        ObjectNode output = mapper.createObjectNode();

        output.put("IBAN", getIban().toString());
        output.put("balance", getBalance());
        output.put("currency", getCurrency().toString());
        output.put("spending limit", getSpendingLimit());
        output.put("deposit limit", getDepositLimit());

        ArrayNode commerciants = mapper.createArrayNode();
        ArrayList<CommerciantTotal> totalToCommerciants = getCommerciants(timedCommerciants);

        for (CommerciantTotal comm : totalToCommerciants) {
            ObjectNode commerciant = mapper.createObjectNode();
            ArrayNode managers = mapper.createArrayNode();
            ArrayNode employees = mapper.createArrayNode();
            for (User manager : comm.getManagers()) {
                managers.add(manager.getFirstName().toString() + " " + manager.getSurname().toString());
            }
            for (User employee : comm.getEmployees()) {
                employees.add(employee.getFirstName().toString() + " " + employee.getSurname().toString());
            }

            commerciant.put("managers", managers);
            commerciant.put("employees", employees);
            commerciant.put("totalReceived", comm.getTotal());
            commerciant.put("commerciant", comm.getCommerciant());

            commerciants.add(commerciant);
        }

        output.put("commerciants", commerciants);
        result.put("output", output);

        return result;

    }

    public ArrayList<CommerciantTotal> getCommerciants(ArrayList<CommerciantRecord> records) {
        ArrayList<CommerciantTotal> result = new ArrayList<>();

        for (CommerciantRecord record : records) {
            for (CommerciantTotal total : result) {
                if (record.getCommerciant().getName().equals(total.getCommerciant())) {
                    total.addUser(record.getUser(), record.getAmount(), getRole(record.getUser()));
                    break;
                }
            }
            CommerciantTotal notFound = new CommerciantTotal(record.getCommerciant().getName());
            result.add(notFound);
        }
        return result;
    }

    public ROLE_TYPE getRole(User user) {
        return roles.get(user);
    }


}
