package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.errors.Log;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class PrintReport implements Command {

    private Account account;
    private int start, stop, timestamp;
    private ArrayNode output;
    private ObjectMapper mapper;
    public PrintReport(final Account account, final int start, final int stop,
                       final ArrayNode output, final ObjectMapper mapper, final int timestamp) {
        this.account = account;
        this.start = start;
        this.stop = stop;
        this.output = output;
        this.mapper = mapper;
        this.timestamp = timestamp;
    }

    /**
     * checks if the account exists and prints its reports
     */
    @Override
    public void execute() {
        if (account == null) {
            Log log = new Log.Builder("report", timestamp).
                    setDescription("Account not found").setDetailsTimestamp(timestamp).build();
            output.add(log.print(mapper));
            return;
        }
        ArrayList<Transaction> transactions = account.getTransactions(start, stop);
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "report");
        ObjectNode details = mapper.createObjectNode();
        details.put("IBAN", account.getIban().toString());
        details.put("balance", account.getBalance());
        details.put("currency", account.getCurrency().toString());
        ArrayNode report = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            report.add(transaction.print(mapper));
        }
        details.put("transactions", report);
        node.put("output", details);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
