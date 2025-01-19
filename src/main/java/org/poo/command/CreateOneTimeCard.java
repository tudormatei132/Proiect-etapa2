package org.poo.command;

import org.poo.account.Account;
import org.poo.account.Card;
import org.poo.account.OneTimeCard;
import org.poo.transactions.CardCreation;
import org.poo.utils.Utils;

import java.util.HashMap;

public class CreateOneTimeCard implements Command {

    private Account account;
    private HashMap<String, Card> cardMap;
    private int timestamp;
    public CreateOneTimeCard(final Account account, final HashMap<String, Card> cardMap,
                             final int timestamp) {
        this.account = account;
        this.cardMap = cardMap;
        this.timestamp = timestamp;
    }

    /**
     * used to create a one time pay card
     */
    public void execute() {
        OneTimeCard temp = new OneTimeCard(new StringBuilder(Utils.generateCardNumber()), account);
        account.addCard(temp);
        String cardNumber = temp.getCardNumber().toString();
        cardMap.put(cardNumber, temp);
        CardCreation newCard = new CardCreation(timestamp, temp.getCardNumber().toString(),
                account.getUser().getEmail().toString(), account.getIban().toString());
        account.getUser().getTransactions().add(newCard);
        account.getTransactions().add(newCard);
    }
}
