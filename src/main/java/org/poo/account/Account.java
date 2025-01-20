package org.poo.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciant.Commerciant;
import org.poo.system.Converter;
import org.poo.transactions.Payment;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.HashMap;
import java.util.ArrayList;

import static org.poo.utils.Utils.ERROR_UPGRADE_PLAN;

@Getter
@Setter
public class Account {
    private User user;
    private StringBuilder iban;
    private StringBuilder currency;
    private StringBuilder type;
    private double balance;
    private ArrayList<Card> cards;
    private double minBalance;
    private ArrayList<Transaction> transactions;
    private ArrayList<Payment> payments;
    private HashMap<String, Double> moneySpent;
    private boolean[] oneTimeDiscounts;
    private double totalSpendings;
    private int transactionsCount;
    private double permanentDiscount;

    public Account(final User user, final StringBuilder iban, final StringBuilder currency) {
        this.user = user;
        this.iban = iban;
        this.currency = currency;
        this.balance = 0;
        cards = new ArrayList<>();
        minBalance = 0;
        transactions = new ArrayList<>();
        payments = new ArrayList<>();
        moneySpent = new HashMap<>();
        oneTimeDiscounts = new boolean[3];
        totalSpendings = 0;
        transactionsCount = 0;
        permanentDiscount = 0f;
    }

    /**
     * adds a card to the list of cards linked to the account
     * @param card the card to be added to the account
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     * removes a card from the card list of the account
     * @param card the card to be removed
     */
    public void removeCard(final Card card) {
        cards.remove(card);
    }

    /**
     * used to update the total amount spent
     * @param comm the commerciant
     * @param amount the amount paid
     */
    public void addNewSpending(Commerciant comm, final double amount) {
        if (comm.getCashbackStrategy().equals("spendingThreshold")) {
            totalSpendings += amount;
            return;
        }
        transactionsCount++;
    }


    /**
     * create an object node used to add the details of the account to the output
     * @param mapper mapper used to create the nodes
     * @return the object node which will be the added to the output
     */
    public ObjectNode printAccount(final ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("IBAN", iban.toString());
        node.put("currency", currency.toString());
        node.put("balance", balance);
        node.put("type", type.toString());
        ArrayNode cardsNode = mapper.createArrayNode();
        for (Card card : cards) {
            cardsNode.add(card.printCard(mapper));
        }
        node.set("cards", cardsNode);
        return node;
    }


    /**
     * adds funds to the account
     * @param funds the amount to be added
     */
    public void addFunds(final double funds) {
        balance += funds;
    }


    /**
     * gets all transactions made between 2 timestamps
     * @param startTimestamp the minimum timestamp for the transactions
     * @param endTimestamp the maximum timestamp for the transactions
     * @return the list of the transactions
     */
    public ArrayList<Transaction> getTransactions(final int startTimestamp,
                                                  final int endTimestamp) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp) {
                result.add(transaction);
            }
            if (transaction.getTimestamp() > endTimestamp) {
                break;
            }
        }
        return result;

    }

    /**
     * gets all payments made between 2 timestamps
     * @param start the minimum timestamp for the payments
     * @param end the maximum timestamp for the payments
     * @return the list of the Payments
     */
    public ArrayList<Payment> getPayments(final int start, final int end) {
        ArrayList<Payment> result = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getTimestamp() >= start && payment.getTimestamp() <= end) {
                result.add(payment);
            }
            if (payment.getTimestamp() > end) {
                break;
            }
        }

        return result;
    }

    /**
     * returns -1 for a classic account
     * @param rate the new rate
     * @return -1 if the operation can't be made
     */
    public int setInterestRate(final double rate) {
        return -1;
    }


    public void setFoodDiscount() {
        oneTimeDiscounts[0] = true;
    }

    public void setClothingDiscount() {
        oneTimeDiscounts[1] = true;
    }

    public void setTechDiscount() {
        oneTimeDiscounts[2] = true;
    }

    public double getCashBack(Commerciant comm) {
        if (comm.getType().equals("nrOfTransactions")) {
            if (comm.getType().equals("Food") && oneTimeDiscounts[0]) {
                oneTimeDiscounts[0] = false;
                return 2f / 100;
            }

            if (comm.getType().equals("Clothes") && oneTimeDiscounts[1]) {
                oneTimeDiscounts[1] = false;
                return 5f / 100;
            }

            if (comm.getType().equals("Tech") && oneTimeDiscounts[2]) {
                oneTimeDiscounts[2] = false;
                return 10f / 100;
            }
            return 0f;
        }
        return permanentDiscount;
    }

    public Utils.ERROR_UPGRADE_PLAN upgradePlan(String to) {
        if (getUser().getPlanType() == Utils.PLAN_TYPE.STANDARD ||
            getUser().getPlanType() == Utils.PLAN_TYPE.STUDENT) {

            if (to.equals("silver")) {
                if (balance >= 100 * Converter.getInstance().convert("RON", currency.toString())) {
                    balance -= 100 * Converter.getInstance().convert("RON", currency.toString());
                    getUser().setPlanType(Utils.PLAN_TYPE.SILVER);
                    return ERROR_UPGRADE_PLAN.SUCCESS;
                } else {
                    return Utils.ERROR_UPGRADE_PLAN.INSUFFICIENT;
                }

            } else if (to.equals("gold")) {
                if (balance >= 350 * Converter.getInstance().convert("RON", currency.toString())) {
                    balance -= 350 * Converter.getInstance().convert("RON", currency.toString());
                    getUser().setPlanType(Utils.PLAN_TYPE.GOLD);
                    return ERROR_UPGRADE_PLAN.SUCCESS;
                } else {
                    return ERROR_UPGRADE_PLAN.INSUFFICIENT;
                }
            } else {
                return ERROR_UPGRADE_PLAN.SAME_PLAN;
            }

        } else if (getUser().getPlanType() == Utils.PLAN_TYPE.SILVER) {

            if (to.equals("gold")) {
                if (balance >= 250 * Converter.getInstance().convert("RON", currency.toString())) {
                    balance -= 250 * Converter.getInstance().convert("RON", currency.toString());
                    getUser().setPlanType(Utils.PLAN_TYPE.GOLD);
                    return ERROR_UPGRADE_PLAN.SUCCESS;
                } else {
                    return ERROR_UPGRADE_PLAN.INSUFFICIENT;
                }
            } else if (to.equals("silver")) {
                return ERROR_UPGRADE_PLAN.SAME_PLAN;
            }

        }

        return ERROR_UPGRADE_PLAN.DOWNGRADE;

    }

}
