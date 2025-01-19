package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.errors.Log;
import org.poo.transactions.Transaction;

public class ChangeInterestRate implements Command {


    private Account account;
    private double newRate;
    private int timestamp;
    private ArrayNode output;
    private ObjectMapper mapper;

    public ChangeInterestRate(final Account account, final double newRate, final int timestamp,
                              final ArrayNode output, final ObjectMapper mapper) {
        this.account = account;
        this.newRate = newRate;
        this.timestamp = timestamp;
        this.output = output;
        this.mapper = mapper;
    }

    /**
     * will try to change the interest rate of an account if it's a savings account of it exists
     */
    @Override
    public void execute() {
        if (account == null) {
            Log error = new Log.Builder("changeInterestRate", timestamp).
                            setDetailsTimestamp(timestamp).setError("Account not found").build();
            output.add(error.print(mapper));
            return;
        }


        if (account.setInterestRate(newRate) == -1) {
            Log log = new Log.Builder("changeInterestRate", timestamp).
                    setDetailsTimestamp(timestamp).
                    setDescription("This is not a savings account").build();

            output.add(log.print(mapper));
            return;
        }

        Transaction newInterestRate = new Transaction(timestamp, "Interest rate of "
                + "the account changed to " + newRate);
        account.getTransactions().add(newInterestRate);
        account.getUser().getTransactions().add(newInterestRate);

    }
}
