package org.poo.transactions;

public class AccountCreation extends Transaction {
    public AccountCreation(final int timestamp) {
        super(timestamp, "New account created");
    }
}
