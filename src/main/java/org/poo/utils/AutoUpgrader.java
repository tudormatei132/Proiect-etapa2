package org.poo.utils;

import org.poo.account.Account;

import java.util.HashMap;

public class AutoUpgrader {

    private HashMap<Account, Integer> silverAccounts;
    private static AutoUpgrader instance;
    private AutoUpgrader() {
        silverAccounts = new HashMap<>();
    }

    public static AutoUpgrader getInstance() {
        if (instance == null) {
            instance = new AutoUpgrader();
        }
        return instance;
    }

    public void updateStatus(Account account) {
        if (!silverAccounts.containsKey(account)) {
            silverAccounts.put(account, 0);
        }
        int count = silverAccounts.get(account);
        silverAccounts.put(account, count + 1);

        if (count + 1 == 5) {
            account.getUser().setPlanType(Utils.PLAN_TYPE.GOLD);
            silverAccounts.remove(account);
        }

    }

}
