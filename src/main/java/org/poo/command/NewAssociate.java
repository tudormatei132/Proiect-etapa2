package org.poo.command;

import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.User;

public class NewAssociate implements Command {


    private Account businessAccount;
    private String role;
    private User user;
    private int timestamp;

    public NewAssociate(final Account account, final String role, final User user,
                        final int timestamp) {

        this.businessAccount = account;
        this.role = role;
        this.user = user;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {
        if (!businessAccount.getType().toString().equals("business")) {

            return;
        }
        BusinessAccount account = (BusinessAccount) businessAccount;
        if (account.getRoles().containsKey(user)) {
            return;
        }
        if (role.equals("manager")) {
            account.getRoles().put(user, BusinessAccount.ROLE_TYPE.MANAGER);
        } else {
            account.getRoles().put(user, BusinessAccount.ROLE_TYPE.EMPLOYEE);
        }
    }
}
