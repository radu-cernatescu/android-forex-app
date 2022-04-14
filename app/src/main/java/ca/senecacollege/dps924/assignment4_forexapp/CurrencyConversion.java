package ca.senecacollege.dps924.assignment4_forexapp;

import java.util.Date;

public class CurrencyConversion {
    String base;
    String destination;
    Date date; // when the rate was updated
    double rate; // 1 base equivalent to rate

    CurrencyConversion(String base, String destination, Date date, double rate) {
        this.base = base;
        this.destination = destination;
        this.date = date;
        this.rate = rate;
    }

    public String toString() {
        return "Convert 1 " + base + " to " + rate + " " + destination + " updated on " + date.toString();
    }


}
