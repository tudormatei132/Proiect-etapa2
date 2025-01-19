package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.errors.Log;
import org.poo.fileio.CommandInput;
import org.poo.transactions.NonNullBalance;

import java.util.HashMap;

public class DeleteAccount implements Command {

    private CommandInput command;
    private HashMap<String, Account> map;
    private ObjectMapper mapper;
    private ArrayNode output;
    private int timestamp;
    public DeleteAccount(final CommandInput command, final HashMap<String, Account> map,
                         final ObjectMapper mapper, final ArrayNode output) {
        this.command = command;
        this.map = map;
        this.mapper = mapper;
        this.output = output;
        this.timestamp = command.getTimestamp();
    }

    /**
     * deletes the account if it exists and it has no funds
     * will also remove it from the map and the array list
     */
    @Override
    public void execute() {
        Account temp = map.get(command.getAccount());
        if (temp == null) {
            Log error = new Log.Builder("deleteAccount", timestamp).
                    setError("Couldn't find account").build();
            output.add(error.print(mapper));
            return;
        }

        if (temp.getBalance() == 0) {
            temp.getUser().removeAccount(temp);
            map.remove(command.getAccount());
            Log succes = new Log.Builder("deleteAccount", timestamp).
                    setDetailsTimestamp(timestamp).setSuccess("Account deleted").build();
            output.add(succes.print(mapper));
            return;
        }

        Log log = new Log.Builder("deleteAccount", timestamp).setDetailsTimestamp(timestamp)
                .setError("Account couldn't be deleted - see org.poo.transactions "
                        + "for details").build();
        output.add(log.print(mapper));

        NonNullBalance deletionFailed = new NonNullBalance(timestamp);
        temp.getTransactions().add(deletionFailed);
        temp.getUser().getTransactions().add(deletionFailed);
    }
}
