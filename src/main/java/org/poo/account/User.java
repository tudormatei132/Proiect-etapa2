package org.poo.account;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.system.SplitCustom;
import org.poo.transactions.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.poo.utils.Utils;
import org.poo.utils.Utils.PLAN_TYPE;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter @Setter
public class User {
    private StringBuilder firstName;
    private StringBuilder surname;
    private StringBuilder email;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;
    private HashMap<String, Account> aliases;
    private String birthDate;
    private String occupation;
    private PLAN_TYPE planType;
    private ArrayList<SplitCustom> requests;


    public User(final UserInput input) {
        firstName = new StringBuilder(input.getFirstName());
        surname = new StringBuilder(input.getLastName());
        email = new StringBuilder(input.getEmail());
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        aliases = new HashMap<>();
        birthDate = input.getBirthDate();
        occupation = input.getOccupation();
        if (occupation.equals("student")) {
            planType = PLAN_TYPE.STUDENT;
        } else {
            planType = PLAN_TYPE.STANDARD;
        }
        requests = new ArrayList<>();
    }

    /**
     * adds an account for the user
     * @param account the account to be added in the list
     */
    public void addAccount(final Account account) {
        accounts.add(account);
    }


    /**
     * used to add to the output node the details of the user
     * @param mapper mapper used to create the ObjectNode
     * @return ObjectNode that has the details of the user
     */
    public ObjectNode printUser(final ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("firstName", firstName.toString());
        node.put("lastName", surname.toString());
        node.put("email", email.toString());
        ArrayNode accountsNode = mapper.createArrayNode();
        for (Account ac : accounts) {
            accountsNode.add(ac.printAccount(mapper));
        }
        node.set("accounts", accountsNode);
        return node;
    }


    /**
     * removes an account of the user
     * @param account the account that will be removed
     */
    public void removeAccount(final Account account) {
        accounts.remove(account);
    }

    /**
     * method used for checking for if a user is at least 21
     * @return whether user is 21 or not
     */
    public boolean isOver21() {

        return Utils.getDifferenceInYears(birthDate) >= 21;
    }

    /**
     * will search for the first account that matches the given currency
     * @param currency the currency used for the search
     * @return the account where the money will be deposited
     */
    public Account withdrawFromSavings(String currency) {
        for (Account account : accounts) {
            if (account.getCurrency().toString().equals(currency)
                    && account.getType().toString().equals("classic")) {
                return account;
            }
        }
        return null;
    }


}
