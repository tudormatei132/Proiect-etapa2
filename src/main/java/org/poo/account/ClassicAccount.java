package org.poo.account;

public class ClassicAccount extends Account {

    public ClassicAccount(final User user, final StringBuilder iban, final StringBuilder currency) {
        super(user, iban, currency);
        this.setType(new StringBuilder("classic"));
    }
}
