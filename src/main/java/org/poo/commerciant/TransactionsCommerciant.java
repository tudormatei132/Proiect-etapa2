package org.poo.commerciant;

import org.poo.account.Account;
import org.poo.fileio.CommerciantInput;

public class TransactionsCommerciant extends Commerciant{

    public TransactionsCommerciant(CommerciantInput input) {
        super(input);
    }

    @Override
    public void setCashBack(Account account) {
        if (account.getTransactionsCount() == 2) {
            account.setFoodDiscount();
            return;
        }
        if (account.getTransactionsCount() == 5) {
            account.setClothingDiscount();
            return;
        }
        if (account.getTransactionsCount() == 10) {
            account.setTechDiscount();
        }
    }
}
