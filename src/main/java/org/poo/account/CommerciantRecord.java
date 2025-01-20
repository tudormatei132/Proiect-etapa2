package org.poo.account;

import lombok.Getter;
import org.poo.commerciant.Commerciant;
@Getter
public class CommerciantRecord {

    private Commerciant commerciant;
    private double amount;
    private User user;
    private int timestamp;

    public CommerciantRecord(Commerciant commerciant, double amount, User user, int timestamp) {
        this.commerciant = commerciant;
        this.amount = amount;
        this.user = user;
        this.timestamp = timestamp;
    }



}
