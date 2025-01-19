package org.poo.command;

import org.poo.account.Account;
import org.poo.account.User;


public class SetAlias implements Command {

    private String alias;
    private Account account;
    private User user;
    public SetAlias(final String alias, final Account account, final User user) {
        this.alias = alias;
        this.account = account;
        this.user = user;

    }

    /**
     * if the account exists, add the alias
     */
    public void execute() {
        if (account == null) {
            return;
        }
        user.getAliases().put(alias, account);
    }
}
