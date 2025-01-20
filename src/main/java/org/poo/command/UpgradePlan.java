package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.errors.Log;
import org.poo.transactions.UpgradedPlan;
import org.poo.utils.Utils;


public class UpgradePlan implements Command {


    private ObjectMapper mapper;
    private ArrayNode output;
    private Account account;
    private String newPlan;
    private int timestamp;

    public UpgradePlan(final ObjectMapper mapper, final ArrayNode output, final Account account,
                       final String newPlan, final int timestamp) {
        this.mapper = mapper;
        this.output = output;
        this.account = account;
        this.newPlan = newPlan;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {

        if (account == null) {
            Log error = new Log.Builder("upgradePlan", timestamp).setError("Account not found")
                    .build();
            output.add(error.print(mapper));
            return;
        }

        Utils.ERROR_UPGRADE_PLAN ret = account.upgradePlan(newPlan);

        if (ret == Utils.ERROR_UPGRADE_PLAN.SUCCESS) {
            UpgradedPlan transaction = new UpgradedPlan(timestamp, account.getIban().toString(),
                                                        "Upgrade plan", newPlan);
            account.getTransactions().add(transaction);
            account.getUser().getTransactions().add(transaction);
        }
        System.out.println(ret);
    }
}
