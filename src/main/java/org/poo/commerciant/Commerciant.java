package org.poo.commerciant;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.fileio.CommerciantInput;
import org.poo.utils.Utils;

@Getter @Setter
public abstract class Commerciant {
    private String name;
    private int id;
    private String account, type, cashbackStrategy;


    public Commerciant(CommerciantInput comm) {
        name = comm.getCommerciant();
        id = comm.getId();
        account = comm.getAccount();
        type = comm.getType();
        cashbackStrategy = comm.getCashbackStrategy();
    }
    /**
     * used to set the cashback percentage
     * @param account the account that made the transaction
     */
    public abstract void setCashBack(Account account);

}
