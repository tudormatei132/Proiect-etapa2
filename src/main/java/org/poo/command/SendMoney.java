package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.User;
import org.poo.commerciant.Commerciant;
import org.poo.errors.Log;
import org.poo.system.Converter;
import org.poo.transactions.Transaction;
import org.poo.transactions.Transfer;
import org.poo.utils.AutoUpgrader;
import org.poo.utils.Utils;

import java.util.HashMap;

public class SendMoney implements Command {

    private String description;
    private User user;
    private double amount;
    private int timestamp;
    private Converter converter;
    private String sender, receiver;
    private HashMap<String, Account> accountMap;
    private ArrayNode output;
    private ObjectMapper mapper;
    private HashMap<String, Commerciant> commerciantMap;

    public SendMoney(final String description, final User user, final double amount,
                     final Converter converter, final String sender, final String receiver,
                     final int timestamp, final HashMap<String, Account> accountMap,
                     final ArrayNode output, final ObjectMapper mapper,
                     final HashMap<String, Commerciant> commerciantMap) {

        this.description = description;
        this.user = user;
        this.amount = amount;
        this.converter = converter;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.accountMap = accountMap;
        this.output = output;
        this.mapper = mapper;
        this.commerciantMap = commerciantMap;
    }

    /**
     * checks if the sender account exists and if the receiver alias exists
     * or if it's an IBAN
     * if the conditions are met, transfer money from the sender to receiver
     * it also converts the amount that will get into the receiver's account
     */
    public void execute() {

        Commerciant comm = null;

        Account senderAccount = accountMap.get(sender);
        if (senderAccount == null) {
            Log error = new Log.Builder("sendMoney", timestamp).setDetailsTimestamp(timestamp)
                    .setDescription("User not found").build();
            output.add(error.print(mapper));
            return;
        }


        Account receiverAccount = user.getAliases().get(receiver);
        if (receiverAccount == null) {
            receiverAccount = accountMap.get(receiver);
            if (receiverAccount == null) {
                comm = commerciantMap.get(receiver);
                if (comm == null) {
                    Log error = new Log.Builder("sendMoney", timestamp).setDetailsTimestamp(timestamp)
                            .setDescription("User not found").build();
                    output.add(error.print(mapper));
                    return;
                }
            }
        }

        if (!senderAccount.canPay(user, amount)) {
            return;
        }

        if (senderAccount.getBalance() - senderAccount.getMinBalance()
                <= amount * (1 + Utils.getCommission(senderAccount.getUser(), amount, senderAccount.getCurrency().toString()))) {
            Transaction transaction = new Transaction(timestamp, "Insufficient funds");
            senderAccount.getUser().getTransactions().add(transaction);
            senderAccount.getTransactions().add(transaction);
            return;
        }

        senderAccount.addFunds(- amount * (1 + Utils.getCommission(senderAccount.getUser(), amount, senderAccount.getCurrency().toString())));



        if (receiverAccount != null) {
            receiver = receiverAccount.getIban().toString();
        }

        Transfer transfer = new Transfer(timestamp, description,
                Double.toString(amount) + " " + senderAccount.getCurrency(), "sent",
                senderAccount.getIban().toString(), receiver);

        senderAccount.getTransactions().add(transfer);
        senderAccount.getUser().getTransactions().add(transfer);

        if (receiverAccount != null) {
            double converted = amount * converter.convert(senderAccount.getCurrency().toString(),
                    receiverAccount.getCurrency().toString());
            Transfer receivedMoney = new Transfer(timestamp, description,
                    Double.toString(converted) + " "
                            + receiverAccount.getCurrency(), "received",
                    senderAccount.getIban().toString(),
                    receiverAccount.getIban().toString());

            receiverAccount.addFunds(converted);
            receiverAccount.getTransactions().add(receivedMoney);
            receiverAccount.getUser().getTransactions().add(receivedMoney);
        }
        double amountInRon = amount * Converter.getInstance().
                convert(senderAccount.getCurrency().toString(), "RON");

        senderAccount.addSpendingTransaction(user, -amount, comm, timestamp);
        if (comm != null) {
            comm.setCashBack(senderAccount);
            senderAccount.addFunds(amount * senderAccount.getCashBack(comm));
        }

        if (amountInRon >= 300 && senderAccount.getUser().getPlanType() == Utils.PLAN_TYPE.SILVER) {
            AutoUpgrader.getInstance().updateStatus(senderAccount);
        }

    }
}
