package org.poo.system;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.transactions.SplitPay;
import org.poo.transactions.SplitPaymentError;
import org.poo.transactions.Transaction;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Getter @Setter
public class SplitCustom {

    private Account[] involvedAccounts;
    private String type, currency;
    private double amount;
    private Double[] amounts;
    private int timestamp;
    private int accepted;

    public SplitCustom(ArrayList<Account> involvedAccounts, String type, String currency,
                       double amount, int timestamp, ArrayList<Double> amounts) {
        this.involvedAccounts = new Account[involvedAccounts.size()];
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.timestamp = timestamp;
        this.amounts = new Double[involvedAccounts.size()];
        for (int i = 0; i < involvedAccounts.size(); i++) {
            if (type.equals("equal")) {
                this.amounts[i] = amount / involvedAccounts.size();
            } else {
                this.amounts[i] = amounts.get(i);
            }
            this.involvedAccounts[i] = involvedAccounts.get(i);
        }
        accepted = 0;
    }

    public void accept() {
        accepted++;
        String stringAmount = String.format("%.2f", amount);
        if (accepted == involvedAccounts.length) {
            for (int i = 0; i < accepted; i++) {
                double converted = amounts[i] * Converter.getInstance().convert(currency,
                        involvedAccounts[i].getCurrency().toString());

                if (involvedAccounts[i].getBalance() - involvedAccounts[i].getMinBalance()
                   < converted) {
                    List<String> accounts = new ArrayList<>();
                    for (Account account : involvedAccounts) {
                        accounts.add(account.getIban().toString());
                    }
                    SplitPaymentError error = new SplitPaymentError(timestamp,
                            "Split payment of " + stringAmount + " " + currency,
                            amounts[i], currency, accounts,
                            involvedAccounts[i].getIban().toString(), type,
                            Arrays.stream(amounts).toList(),"Account " +
                            involvedAccounts[i].getIban().toString() + " has insufficient" +
                            " funds for a split payment.");
                    notify(error);
                    return;
                }


            }
            List<String> accounts = new ArrayList<>();
            for (Account account : involvedAccounts) {
                accounts.add(account.getIban().toString());
            }

            SplitPay success = new SplitPay(timestamp, "Split payment of " + stringAmount + " " +  currency,
                                           currency, accounts, amount, type,
                                            Arrays.stream(amounts).toList());

            for (int i = 0; i < accepted; i++) {
                double converted = amounts[i] * Converter.getInstance().convert(currency,
                        involvedAccounts[i].getCurrency().toString());
                involvedAccounts[i].addFunds(-converted);
            }
            notify(success);
        }
    }


    public void reject() {
        accepted = -1;
        List<String> accounts = new ArrayList<>();
        for (Account account : involvedAccounts) {
            accounts.add(account.getIban().toString());
        }
        SplitPaymentError error = new SplitPaymentError(timestamp, "Split payment of " + amount + " " +  currency,
                                                        amount, currency, accounts, null, type,
                                                        Arrays.stream(amounts).toList(), "One user rejected the paymenyt");
        for (int i = 0; i < accepted; i++) {
            ArrayList<SplitCustom> requests = involvedAccounts[i].getUser().getRequests();
            requests.removeIf(request -> request.getAccepted() == -1);
        }
    }


    public void notify(Transaction transaction) {
        for (Account involvedAccount : involvedAccounts) {
            involvedAccount.update(transaction);
        }
    }


}
