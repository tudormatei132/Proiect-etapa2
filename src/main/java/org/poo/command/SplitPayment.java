package org.poo.command;

import org.poo.account.Account;
import org.poo.account.User;
import org.poo.system.Converter;
import org.poo.system.SplitCustom;
import org.poo.transactions.SplitPay;
import org.poo.transactions.SplitPaymentError;

import java.util.*;

public class SplitPayment implements Command {

    private List<String> accounts;
    private int timestamp;
    private String currency;
    private double amount;
    private HashMap<String, Account> accountMap;
    private Converter converter;
    private String type;
    private List<Double> amounts;
    public SplitPayment(final List<String> accounts, final int timestamp, final String currency,
                        final double amount, final HashMap<String, Account> accountMap,
                        final Converter converter, final String type, final List<Double> amounts) {
        this.accounts = accounts;
        this.timestamp = timestamp;
        this.currency = currency;
        this.amount = amount;
        this.accountMap = accountMap;
        this.converter = converter;
        this.type = type;
        this.amounts = amounts;
    }

    /**
     * will split a pay between multiple accounts
     * and will convert the amount to all currencies of the accounts
     * and if all of them have enough funds, the sum will be deducted from the accounts' balances
     */
    public void execute() {
        ArrayList<Account> accountArray = new ArrayList<>();
        for (String acc : accounts) {
            Account toAdd = accountMap.get(acc);
            if (toAdd != null) {
                accountArray.add(toAdd);
            } else {
                return;
            }
        }

        Set<User> uniqueUsers = new HashSet<>();
        for (Account acc : accountArray) {
            uniqueUsers.add(acc.getUser());
        }

        SplitCustom payment = new SplitCustom(accountArray, type, currency, amount,
                                             timestamp, (ArrayList<Double>) amounts, uniqueUsers.size());

        for (User u : uniqueUsers) {
            u.getRequests().add(payment);
        }
    }
}
