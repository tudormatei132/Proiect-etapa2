package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;

public class BusinessReport implements Command {

    private Account businessAccount;
    private int timestamp;
    private ArrayNode output;
    private ObjectMapper mapper;
    private String type;
    private int start;
    private int end;
    public BusinessReport(final Account businessAccount, final ArrayNode output,
                          final ObjectMapper mapper, final int timestamp, final String type,
                          final int start, final int end) {
        this.businessAccount = businessAccount;
        this.output = output;
        this.mapper = mapper;
        this.timestamp = timestamp;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public void execute() {

        if (!businessAccount.getType().toString().equals("business")) {
            return;
        }

        BusinessAccount account = (BusinessAccount) businessAccount;


        if (type.equals("commerciant")) {
            output.add(account.printCommerciantReport(mapper, start, end, timestamp));
        } else {
            output.add(account.printTransactionsReport(mapper, start, end, timestamp));
        }
    }
}
