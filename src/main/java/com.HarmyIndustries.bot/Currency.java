package com.HarmyIndustries.bot;

public enum Currency {

    USD(0), EUR(1), BYN(2);

    private final int id;

    public int getId() {
        return id;
    }

    Currency(int id) {
        this.id = id;
    }
}
