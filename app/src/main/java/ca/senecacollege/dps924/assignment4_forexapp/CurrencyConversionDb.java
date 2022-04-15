package ca.senecacollege.dps924.assignment4_forexapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CurrencyConversionResult.class}, version = 1)
public abstract class CurrencyConversionDb extends RoomDatabase {
    public abstract CurrencyConversionDao currencyConversionDao();
}