package ca.senecacollege.dps924.assignment4_forexapp;

import androidx.room.Room;

import java.util.ArrayList;

// Data that I want to persist throughout app's life
public class DataManager {

    public ArrayList<Currency> currencies = new ArrayList<>();
    public ArrayList<String> dropDown = new ArrayList<>();
    public ArrayList<CurrencyConversionResult> results = new ArrayList<>();
    public ArrayList<NewsArticle> articles = new ArrayList<>();
    CurrencyConversionDb db;
    CurrencyConversionDao currencyDao;

    public String from_selection = "";
    public String to_selection = "";
    public String menu_selection = "";

    public String history_from_selection = "";
    public String history_to_selection = "";

}
