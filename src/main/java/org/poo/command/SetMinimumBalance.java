package org.poo.command;

import org.poo.account.Account;

public class SetMinimumBalance implements Command {

    private double minBalance;
    private Account account;

    public SetMinimumBalance(final double minBalance, final Account account) {
        this.minBalance = minBalance;
        this.account = account;
    }

    /**
     * sets the minimum balance for the account if it exists
     */
    @Override
    public void execute() {
        if (account == null) {
            return;
        }
        account.setMinBalance(minBalance);
    }
}
