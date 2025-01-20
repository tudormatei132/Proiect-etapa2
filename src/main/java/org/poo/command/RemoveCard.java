package org.poo.command;

import org.poo.account.Card;
import org.poo.account.User;
import org.poo.errors.Log;
import org.poo.transactions.CardDestruction;

import java.util.HashMap;

public class RemoveCard implements Command {

    private String cardNumber;
    private HashMap<String, Card> cards;
    private User user;
    private int timestamp;
    public RemoveCard(final String cardNumber, final HashMap<String, Card> cards,
                       final int timestamp, final User user) {
        this.cardNumber = cardNumber;
        this.cards = cards;
        this.timestamp = timestamp;
        this.user = user;
    }

    /**
     * deletes a card by removing it from the card map and the ArrayList of the account
     */
    @Override
    public void execute() {
        Card temp = cards.get(cardNumber);
        if (temp == null) {
            Log error = new Log.Builder("deleteCard", timestamp).setDetailsTimestamp(timestamp)
                            .setError("Card not found").build();
            return;
        }

        if (!temp.getAccount().canDeleteCard(user, temp)) {
            return;
        }

        CardDestruction removed = new CardDestruction(timestamp, cardNumber,
                                    temp.getAccount().getUser().getEmail().toString(),
                                    temp.getAccount().getIban().toString());

        temp.getAccount().getUser().getTransactions().add(removed);
        temp.getAccount().getTransactions().add(removed);

        temp.getAccount().removeCard(temp);
        cards.remove(temp.getCardNumber().toString());
    }
}
