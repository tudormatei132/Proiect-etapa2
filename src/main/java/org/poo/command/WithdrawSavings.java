package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.User;
import org.poo.errors.Log;
import org.poo.system.Converter;
import org.poo.transactions.Transaction;
import org.poo.transactions.WithdrawOfSavings;
import org.poo.utils.Utils;
import java.text.ParseException;

public class WithdrawSavings implements Command {


    private Account savingsAccount;
    private double amount;
    private String currency;
    private int timestamp;
    private ObjectMapper mapper;
    private ArrayNode output;

    public WithdrawSavings(final Account savingsAccount, final double amount,
                           final String currency, final int timestamp, final ObjectMapper mapper,
                           final ArrayNode output) {

        this.savingsAccount = savingsAccount;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.mapper = mapper;
        this.output = output;
    }

    /**
     * executes the command
     */
    @Override
    public void execute() {
        if (savingsAccount == null) {
            Log error = new Log.Builder("withdrawSavings", timestamp)
                        .setError("Account not found")
                        .build();

            return;
        }
        User owner = savingsAccount.getUser();
        if (!owner.isOver21()) {
            Transaction under21 = new Transaction(timestamp, "You don't have the minimum age required.");
            owner.getTransactions().add(under21);
            savingsAccount.getTransactions().add(under21);
            return;
        }


        double new_amount = amount;
        amount = Converter.getInstance().
                convert(currency, savingsAccount.getCurrency().toString()) * new_amount;

        Account account = owner.withdrawFromSavings(currency);
        if (account == null) {
            Transaction error = new Transaction(timestamp, "You do not have a classic account.");
            owner.getTransactions().add(error);
            savingsAccount.getTransactions().add(error);
            return;
        }

        savingsAccount.addFunds(-amount);
        account.addFunds(new_amount);
        WithdrawOfSavings success = new WithdrawOfSavings(amount, timestamp,
                account.getIban().toString(), savingsAccount.getIban().toString());
        savingsAccount.getTransactions().add(success);
        savingsAccount.getUser().getTransactions().add(success);

        account.getTransactions().add(success);
        account.getUser().getTransactions().add(success);


    }
}
