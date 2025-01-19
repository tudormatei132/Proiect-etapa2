package org.poo.account;

import lombok.Getter;


public class SavingsAccount extends Account {
    @Getter
    private double interestRate;

    public SavingsAccount(final User user, final StringBuilder iban, final StringBuilder currency,
                   final double interestRate) {
        super(user, iban, currency);
        this.interestRate = interestRate;
        setBalance(0);
        this.setType(new StringBuilder("savings"));
    }

    /**
     * sets the interest rate to a new one
     * @param rate the new rate
     * @return 0 to signal that the account is a savings account
     */
    public int setInterestRate(final double rate) {
        interestRate = rate;
        return 0;
    }
}
