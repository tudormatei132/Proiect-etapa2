package org.poo.command;

import org.poo.account.Account;
import org.poo.errors.Log;

public class AddFunds implements Command {

    private Account account;
    private double amount;
    private int timestamp;
    public AddFunds(final Account account, final double amount, final int timestamp) {
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
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
        account.addFunds(amount);
    }
}
