package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.User;
import org.poo.errors.Log;
import org.poo.system.Converter;

public class SetLimit implements Command {


    private double limit;
    private Account businessAccount;
    private User user;
    private boolean whichLimit;
    private ArrayNode output;
    private ObjectMapper mapper;
    private int timestamp;
    public SetLimit(double limit, Account account, User user, boolean whichLimit,
                    ArrayNode output, ObjectMapper mapper, int timestamp) {
        this.limit = limit;
        this.businessAccount = account;
        this.user = user;
        this.whichLimit = whichLimit;
        this.output = output;
        this.mapper = mapper;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {
        if (!businessAccount.getType().toString().equals("business")) {
            return;
        }

        BusinessAccount account = (BusinessAccount) businessAccount;
        if (account.getRoles().get(user) != BusinessAccount.ROLE_TYPE.OWNER) {
            String command;
            if (whichLimit) {
                command = "changeSpendingLimit";
            } else {
                command = "changeDepositLimit";
            }
            Log error = new Log.Builder(command, timestamp).setDescription("You must be owner in order to change spending limit.")
                    .setDetailsTimestamp(timestamp).build();
            output.add(error.print(mapper));
            return;
        }
        if (whichLimit) {
            account.setSpendingLimit(limit);
        } else {
            account.setDepositLimit(limit);
        }
    }
}
