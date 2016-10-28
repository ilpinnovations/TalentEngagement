package com.maverick.tcs.innovations.talentengagment.beans;

/**
 * Created by 1241575(Azim Ansari)  on 7/25/2016.
 * Module
 */
public enum Module {
    TCS_IN_UK(1),
    ON_BOARDING_FORMALITIES(2),
    WHATS_WHERE_ON_ULTIMATIX(3),
    ON_SITE_REPORTING(4),
    OPENING_BANK_ACCOUNT(5),
    THINGS_TO_KNOW(6),
    CONTACTS(7),
    HEALTH_SAFETY(8),
    NIA(9),
    NHS_MAYFAIR(10),
    MEDICAL(11),
    WORKPLACE_BEHAVIOUR(12);


    private int id;

    Module(int i) {
        this.id = i;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + id;
    }

    public int getId() {
        return id;
    }
}
