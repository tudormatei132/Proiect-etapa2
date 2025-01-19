package org.poo.command;

import org.poo.account.Account;
import org.poo.system.Converter;
import org.poo.transactions.SplitPay;
import org.poo.transactions.SplitPaymentError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplitPayment implements Command {

    private List<String> accounts;
    private int timestamp;
    private String currency;
    private double amount;
    private HashMap<String, Account> accountMap;
    private Converter converter;

    public SplitPayment(final List<String> accounts, final int timestamp, final String currency,
                        final double amount, final HashMap<String, Account> accountMap,
                        final Converter converter) {
        this.accounts = accounts;
        this.timestamp = timestamp;
        this.currency = currency;
        this.amount = amount;
        this.accountMap = accountMap;
        this.converter = converter;
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
        int len = accountArray.size();
        String description = String.format("Split payment of %.2f %s", amount, currency);
        for (Account acc : accountArray.reversed()) {
            double convertedAmount = amount
                                     * converter.convert(currency, acc.getCurrency().toString());

            if (convertedAmount / len > (acc.getBalance() - acc.getMinBalance())) {
                SplitPaymentError error = new SplitPaymentError(timestamp, description, amount
                        / len, currency, accounts, acc.getIban().toString());
                for (Account it : accountArray) {
                    it.getTransactions().add(error);
                    it.getUser().getTransactions().add(error);
                }
                return;
            }
        }

        SplitPay split = new SplitPay(timestamp, description, currency, accounts, amount / len);
        for (Account acc : accountArray) {
            double convertedAmount = amount / len
                    * converter.convert(currency, acc.getCurrency().toString());
            acc.addFunds(-convertedAmount);
            acc.getTransactions().add(split);
            acc.getUser().getTransactions().add(split);
        }

    }
}
