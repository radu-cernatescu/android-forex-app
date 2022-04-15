package ca.senecacollege.dps924.assignment4_forexapp;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;
import java.util.Date;

@Entity(tableName = "conversions")
public class CurrencyConversionResult {

    @PrimaryKey(autoGenerate = true)
    int conversionID;

    String from;
    String to;
    double toAmountConverted;
    double fromAmountRequested;

    @Ignore
    Date date; // date of conversion

    long date_millis; // date of conversion in millis

    @RequiresApi(api = Build.VERSION_CODES.O)
    CurrencyConversionResult(String from, String to, double toAmountConverted, double fromAmountRequested, Date date) {
        this.from = from;
        this.to = to;
        this.toAmountConverted = toAmountConverted;
        this.fromAmountRequested = fromAmountRequested;
        this.date = date;
        this.date_millis = date.toInstant().getEpochSecond();
    }

    CurrencyConversionResult() {}

    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return "Conversion Result: " + fromAmountRequested + " " + from + " = " + df.format(toAmountConverted) + " " + to + " converted on " + date.toString();
    }
}
