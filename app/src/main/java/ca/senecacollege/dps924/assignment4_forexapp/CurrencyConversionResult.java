package ca.senecacollege.dps924.assignment4_forexapp;

import java.text.DecimalFormat;
import java.util.Date;

public class CurrencyConversionResult {
    String from;
    String to;
    double toAmountConverted;
    double fromAmountRequested;
    Date date; // date of conversion

    CurrencyConversionResult(String from, String to, double toAmountConverted, double fromAmountRequested, Date date) {
        this.from = from;
        this.to = to;
        this.toAmountConverted = toAmountConverted;
        this.fromAmountRequested = fromAmountRequested;
        this.date = date;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return "Conversion Result: " + fromAmountRequested + " " + from + " = " + df.format(toAmountConverted) + " " + to + " converted on " + date.toString();
    }
}
