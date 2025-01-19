package org.poo.command;

import org.poo.account.Account;
import org.poo.account.Card;
import org.poo.transactions.CardCreation;
import org.poo.utils.Utils;

import java.util.HashMap;

public class CreateCard implements Command {

    private Account account;
    private HashMap<String, Card> cardMap;
    private int timestamp;
    private String email;

    public CreateCard(final Account account, final HashMap<String, Card> cardMap,
                      final int timestamp, final String email) {
        this.account = account;
        this.cardMap = cardMap;
        this.timestamp = timestamp;
        this.email = email;
    }

    /**
     * creates a card if the request was made by the owner of the account
     * and adds it to the cardMap and the Array List of the account
     */
    public void execute() {
        if (!account.getUser().getEmail().toString().equals(email)) {
            return;
        }
        Card temp = new Card(new StringBuilder(Utils.generateCardNumber()), account);
        account.addCard(temp);

        String cardNumber = temp.getCardNumber().toString();
        cardMap.put(cardNumber, temp);

        CardCreation newCard = new CardCreation(timestamp, temp.getCardNumber().toString(),
                account.getUser().getEmail().toString(), account.getIban().toString());
        account.getUser().getTransactions().add(newCard);
        account.getTransactions().add(newCard);
    }

}
