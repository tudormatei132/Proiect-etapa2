package org.poo.account;

import lombok.Getter;

@Getter
public class SimpleRecord {

    private User user;
    private double amount;
    private int timestamp;

    public SimpleRecord(User user, double amount, int timestamp) {
        this.user = user;
        this.amount = amount;
        this.timestamp = timestamp;
    }

}
