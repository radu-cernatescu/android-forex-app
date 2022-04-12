package ca.senecacollege.dps924.assignment4_forexapp;

import java.util.Date;

public class ExchangeResult {
    String from;
    String to;
    double amount; // 1 unit of from will give amount units of to
    Date date;

    ExchangeResult(String from, String to, double amount, Date date) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
    }

    public String toString() {
        return from + " to " + to + ":";
    }
}
