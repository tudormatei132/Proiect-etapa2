package org.poo.account;



public class OneTimeCard extends Card {


    public OneTimeCard(final StringBuilder cardNumber, final Account account) {
        super(cardNumber, account);
    }

    /**
     * used to mark that the card needs to be replaced
     * @param amount the amount that will be extracted from the account
     */
    public void use(final double amount) {
        getAccount().addFunds(-amount);
        setStatus(new StringBuilder("mustBeReplaced"));
    }

}
