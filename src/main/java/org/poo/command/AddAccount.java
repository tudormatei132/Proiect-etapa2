package org.poo.command;

import org.poo.account.*;
import org.poo.fileio.CommandInput;
import org.poo.transactions.AccountCreation;
import org.poo.utils.Utils;

import java.util.HashMap;

public class AddAccount implements  Command {


    private CommandInput command;
    private HashMap<String, User> userMap;
    private HashMap<String, Account> map;

    public AddAccount(final CommandInput action, final HashMap<String, User> userMap,
                      final HashMap<String, Account> map) {
        command = action;
        this.userMap = userMap;
        this.map = map;
    }

    /**
     * will create a new account and add it to the Account Map and
     * in its user's ArrayList
     */
    @Override
    public void execute() {
        User temp = userMap.get(command.getEmail());
        Account account;
        if (command.getAccountType().equals("classic")) {
            account = new ClassicAccount(temp,
                    new StringBuilder(Utils.generateIBAN()),
                    new StringBuilder(command.getCurrency()));
        } else if (command.getAccountType().equals("savings")) {
            account = new SavingsAccount(temp,
                    new StringBuilder(Utils.generateIBAN()),
                    new StringBuilder(command.getCurrency()),
                    command.getInterestRate());
        } else {
            account = new BusinessAccount(temp, new StringBuilder(Utils.generateIBAN()),
                    new StringBuilder(command.getCurrency()));
        }




        temp.addAccount(account);
        String iban = account.getIban().toString();
        map.put(iban, account);

        AccountCreation creation = new AccountCreation(command.getTimestamp());
        account.getTransactions().add(creation);
        userMap.get(command.getEmail()).getTransactions().add(creation);
    }
}
