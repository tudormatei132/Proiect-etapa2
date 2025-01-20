package org.poo.commerciant;

import org.poo.account.Account;
import org.poo.fileio.CommerciantInput;
import org.poo.utils.Utils;

public class SpendingsCommerciant extends Commerciant {

    public SpendingsCommerciant(CommerciantInput input) {
        super(input);
    }

    @Override
    public void setCashBack(Account account) {
        if (account.getTotalSpendings() >= 100 && account.getTotalSpendings() < 300) {
            if (account.getUser().getPlanType() == Utils.PLAN_TYPE.STANDARD
                || account.getUser().getPlanType() == Utils.PLAN_TYPE.STUDENT) {
                account.setPermanentDiscount(0.1 / 100);
                return;
            }
            if (account.getUser().getPlanType() == Utils.PLAN_TYPE.SILVER) {
               account.setPermanentDiscount(0.3 / 100);
               return;
            }

            account.setPermanentDiscount(0.5 / 100);
            return;
        }

        if (account.getTotalSpendings() >= 300 && account.getTotalSpendings() < 500) {
            if (account.getUser().getPlanType() == Utils.PLAN_TYPE.STANDARD
                    || account.getUser().getPlanType() == Utils.PLAN_TYPE.STUDENT) {
                account.setPermanentDiscount(0.2 / 100);
                return;
            }
            if (account.getUser().getPlanType() == Utils.PLAN_TYPE.SILVER) {
                account.setPermanentDiscount(0.4 / 100);
                return;
            }

            account.setPermanentDiscount(0.55 / 100);
            return;
        }
        if (account.getTotalSpendings() >= 500) {
            if (account.getUser().getPlanType() == Utils.PLAN_TYPE.STANDARD
                    || account.getUser().getPlanType() == Utils.PLAN_TYPE.STUDENT) {
                account.setPermanentDiscount(0.25 / 100);
                return;
            }
            if (account.getUser().getPlanType() == Utils.PLAN_TYPE.SILVER) {
                account.setPermanentDiscount(0.5 / 100);
                return;
            }

            account.setPermanentDiscount(0.7 / 100);
        }

    }

}
