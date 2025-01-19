package org.poo.utils;

import org.poo.account.User;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class Utils {
    public static final int WARNING_DIFF = 30;
    public enum PLAN_TYPE{
        STANDARD,
        STUDENT,
        SILVER,
        GOLD
    }

    private Utils() {
        // Checkstyle error free constructor
    }

    private static final int IBAN_SEED = 1;
    private static final int CARD_SEED = 2;
    private static final int DIGIT_BOUND = 10;
    private static final int DIGIT_GENERATION = 16;
    private static final String RO_STR = "RO";
    private static final String POO_STR = "POOB";


    private static Random ibanRandom = new Random(IBAN_SEED);
    private static Random cardRandom = new Random(CARD_SEED);

    /**
     * Utility method for generating an IBAN code.
     *
     * @return the IBAN as String
     */
    public static String generateIBAN() {
        StringBuilder sb = new StringBuilder(RO_STR);
        for (int i = 0; i < RO_STR.length(); i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        sb.append(POO_STR);
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Utility method for generating a card number.
     *
     * @return the card number as String
     */
    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(cardRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Resets the seeds between runs.
     */
    public static void resetRandom() {
        ibanRandom = new Random(IBAN_SEED);
        cardRandom = new Random(CARD_SEED);
    }

    /**
     * method used for transfers and withdrawals
     * @param user user that initiated the transaction
     * @param amount the amount of money involved
     * @return value of the commission based on user's plan type and amount
     */
    public static double getCommission(User user, double amount) {
        if (user.getPlanType() == PLAN_TYPE.STANDARD) {
            return 0.2 / 100;
        }
        if (user.getPlanType() == PLAN_TYPE.STUDENT || user.getPlanType() == PLAN_TYPE.GOLD) {
            return 0;
        }

        if (amount < 500)
            return 0;
        return 0.1 / 100;
    }


    public static int getDifferenceInYears(String date) {

        LocalDate dateCurrent = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate birthDate = LocalDate.parse(date, formatter);

        Period difference = Period.between(birthDate, dateCurrent);
        System.out.println(difference.getYears());
        return difference.getYears();

    }


}
