package org.poo.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.utils.Utils;

@Getter @Setter
public class Card {
    private StringBuilder status;
    private StringBuilder cardNumber;
    private Account account;

    public Card(final StringBuilder cardNumber, final Account account) {
        this.status = new StringBuilder("active");
        this.cardNumber = cardNumber;
        this.account = account;
    }

    /**
     * used to add the details of a card to the output
     * @param mapper mapper used to create the ObjectNode
     * @return an ObjectNode that represents the details of the card
     */
    public ObjectNode printCard(final ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("status", status.toString());
        node.put("cardNumber", cardNumber.toString());
        return node;
    }

    /**
     * used for payments, to lower the balance
     * @param amount the amount of money that will be extracted from the account
     */
    public void use(final double amount) {
        getAccount().addFunds(-amount);
    }

    /**
     * used for the checking of the status
     */
    public void update() {
        if (Double.compare(getAccount().getBalance() - getAccount().getMinBalance(),
                Utils.WARNING_DIFF) > 0) {
            status = new StringBuilder("active");
            return;
        }
        if (Double.compare(getAccount().getBalance(), getAccount().getMinBalance()) <= 0) {
            status = new StringBuilder("frozen");
            return;
        }
        status = new StringBuilder("warning");
    }


}
