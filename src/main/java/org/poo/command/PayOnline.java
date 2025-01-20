package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Card;
import org.poo.account.User;
import org.poo.commerciant.Commerciant;
import org.poo.errors.Log;
import org.poo.system.Converter;

import org.poo.transactions.CardCreation;
import org.poo.transactions.CardDestruction;
import org.poo.transactions.Payment;
import org.poo.transactions.Transaction;
import org.poo.utils.AutoUpgrader;
import org.poo.utils.Utils;

import java.util.HashMap;

public class PayOnline implements Command {


    private Card card;
    private double amount;
    private String currency;
    private String email;
    private ArrayNode output;
    private ObjectMapper mapper;
    private int timestamp;
    private HashMap<String, Card> cardMap;
    private Converter converter;
    private Commerciant commerciant;
    private User user;

    public PayOnline(final Card card, final double amount, final String currency,
                     final String email, final ArrayNode output, final ObjectMapper mapper,
                     final int timestamp, final HashMap<String, Card> cardMap,
                     final Converter converter, final Commerciant commerciant, final User user) {
        this.card = card;
        this.amount = amount;
        this.currency = currency;
        this.email = email;
        this.output = output;
        this.mapper = mapper;
        this.timestamp = timestamp;
        this.cardMap = cardMap;
        this.converter = converter;
        this.commerciant = commerciant;
        this.user = user;
    }

    /**
     * will check if the card exists and if its owner made the request
     * if both conditions are met, check if it has enough money and remove funds from
     * the account balance
     * will also generate a new card number for a one time pay card if the payment
     * was successful
     */
    public void execute() {
        if (card == null) {
            Log log = new Log.Builder("payOnline", timestamp).setDetailsTimestamp(timestamp).
                    setDescription("Card not found").build();

            output.add(log.print(mapper));
            return;
        }


        if (!card.getAccount().canPay(user, amount)) {
            System.out.println("NU se poate!");
            return;
        }

        if (amount == 0)
            return;




        double amountInRon = Converter.getInstance().convert(currency, "RON") * amount;
        double cashbackAmount = amount * converter.convert(currency, card.getAccount().getCurrency().toString());
        amount = amount * (1 + Utils.getCommission(card.getAccount().getUser(), amount, currency))
                * (converter.convert(currency, card.getAccount().getCurrency().toString()));




        if (Double.compare(card.getAccount().getBalance() - card.getAccount().getMinBalance(),
                amount) < 0) {
            Transaction error = new Transaction(timestamp, "Insufficient funds");
            card.getAccount().getTransactions().add(error);
            card.getAccount().getUser().getTransactions().add(error);
            return;
        }

        if (card.getStatus().toString().equals("frozen")) {
            Transaction error = new Transaction(timestamp, "The card is frozen");
            card.getAccount().getTransactions().add(error);
            card.getAccount().getUser().getTransactions().add(error);
            return;
        }


        Payment newPayment = new Payment(timestamp, "Card payment", cashbackAmount, commerciant.getName());

        card.getAccount().getTransactions().add(newPayment);
        card.getAccount().getPayments().add(newPayment);
        card.getAccount().getUser().getTransactions().add(newPayment);
        card.use(amount);



        if (card.getStatus().toString().equals("mustBeReplaced")) {
            CardDestruction oldCardDestruction = new CardDestruction(timestamp,
                                                 card.getCardNumber().toString(),
                                                 card.getAccount().getUser().getEmail().toString(),
                                                 card.getAccount().getIban().toString());

            card.getAccount().getTransactions().add(oldCardDestruction);
            card.getAccount().getUser().getTransactions().add(oldCardDestruction);

            cardMap.remove(card.getCardNumber().toString());

            card.setCardNumber(new StringBuilder(Utils.generateCardNumber()));
            card.setStatus(new StringBuilder("active"));

            CardCreation newCard = new CardCreation(timestamp, card.getCardNumber().toString(),
                                    card.getAccount().getUser().getEmail().toString(),
                                    card.getAccount().getIban().toString());
            card.getAccount().getTransactions().add(newCard);
            card.getAccount().getUser().getTransactions().add(newCard);

            cardMap.put(card.getCardNumber().toString(), card);
        }

        card.getAccount().addNewSpending(commerciant, amountInRon);
        commerciant.setCashBack(card.getAccount());
        card.getAccount().addFunds(cashbackAmount * card.getAccount().getCashBack(commerciant));



        card.getAccount().addSpendingTransaction(user, -cashbackAmount, commerciant, timestamp);

        if (amountInRon >= 300 && card.getAccount().getUser().getPlanType() == Utils.PLAN_TYPE.SILVER) {
            AutoUpgrader.getInstance().updateStatus(card.getAccount());
        }
    }

}
