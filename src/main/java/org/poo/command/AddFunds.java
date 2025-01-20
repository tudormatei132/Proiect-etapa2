package org.poo.command;

import org.poo.account.Account;
import org.poo.account.User;
import org.poo.errors.Log;

public class AddFunds implements Command {

    private Account account;
    private double amount;
    private int timestamp;
    private User user;
    public AddFunds(final Account account, final double amount, final int timestamp,
                    final User user) {
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
        this.user = user;
    }

    /**
     * will try to add funds to account, if it exists
     */
    public void execute() {
        if (account == null) {
            Log error = new Log.Builder("addFunds", timestamp).
                    setError("Couldn't find the account").build();
            return;
        }

        if (!account.canDeposit(user, amount)) {
            return;
        }

        account.addFunds(amount);
        account.addSpendingTransaction(user, amount, null, timestamp);
    }



}
