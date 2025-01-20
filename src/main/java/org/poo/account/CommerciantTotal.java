package org.poo.account;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
@Getter
public class CommerciantTotal {


    private Set<User> employees;
    private Set<User> managers;
    private double total;
    private String commerciant;

    public CommerciantTotal(final String commerciant) {

        this.commerciant = commerciant;
        this.employees = new HashSet<User>();
        this.managers = new HashSet<>();
        this.total = 0.0;
    }

    public void addUser(final User user, double amount, BusinessAccount.ROLE_TYPE role) {
        if (role == BusinessAccount.ROLE_TYPE.EMPLOYEE) {
            this.employees.add(user);
        } else {
            this.managers.add(user);
        }
        this.total += amount;
    }



}
