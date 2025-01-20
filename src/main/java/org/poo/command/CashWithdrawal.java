package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Card;
import org.poo.account.User;
import org.poo.errors.Log;
import org.poo.fileio.CommandInput;
import org.poo.system.Converter;
import org.poo.transactions.Transaction;
import org.poo.transactions.WithdrawCash;
import org.poo.utils.Utils;

public class CashWithdrawal implements Command {

    private Card card;
    private double amount;
    private String location;
    private int timestamp;
    private User user;
    private ObjectMapper mapper;
    private ArrayNode output;


    public CashWithdrawal(Card card, CommandInput command, User user, ObjectMapper mapper,
                          ArrayNode output) {
        this.card = card;
        this.amount = command.getAmount();
        this.location = command.getLocation();
        this.timestamp = command.getTimestamp();
        this.user = user;
        this.output = output;
        this.mapper = mapper;
    }

    @Override
    public void execute() {
        if (user == null) {
            Log error = new Log.Builder("cashWithdrawal", timestamp)
                    .setDescription("User not found").setDetailsTimestamp(timestamp).build();
            output.add(error.print(mapper));
            return;
        }

        if (card == null) {
            Log error = new Log.Builder("cashWithdrawal", timestamp)
                    .setDescription("Card not found").setDetailsTimestamp(timestamp).build();
            output.add(error.print(mapper));
            return;
        }

        if (card.getStatus().toString().equals("frozen")) {
            Transaction error = new Transaction(timestamp, "The card is frozen");
            card.getAccount().getTransactions().add(error);
            user.getTransactions().add(error);
            return;
        }

        double finalAmount = amount * (1 + Utils.getCommission(user, amount, "RON")) *
                Converter.getInstance().convert("RON", card.getAccount().getCurrency().toString());

        if (finalAmount > card.getAccount().getBalance()) {
            Transaction error = new Transaction(timestamp, "Insufficient funds");
            card.getAccount().getTransactions().add(error);
            user.getTransactions().add(error);
            return;
        }

        if (finalAmount > card.getAccount().getBalance() - card.getAccount().getMinBalance()) {
            Transaction error = new Transaction(timestamp, "Cannot perform payment due to a" +
                    "minimum balance being set");
            card.getAccount().getTransactions().add(error);
            user.getTransactions().add(error);
            return;
        }

        card.getAccount().addFunds(-finalAmount);
        WithdrawCash success = new WithdrawCash(timestamp, "Cash withdrawal of " + amount, amount);
        card.getAccount().getTransactions().add(success);
        user.getTransactions().add(success);
    }
}
