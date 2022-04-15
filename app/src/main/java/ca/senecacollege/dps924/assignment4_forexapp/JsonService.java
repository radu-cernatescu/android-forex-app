package ca.senecacollege.dps924.assignment4_forexapp;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class JsonService {

    public ArrayList<Currency> getCurrenciesFromJSON(String JSONstring) {
        ArrayList<Currency> arrayList = new ArrayList<>(0);

        try {
            JSONObject currencies = new JSONObject(JSONstring);
            Iterator<String> keys = currencies.keys();

            int i = 1;
            while(keys.hasNext()) {
                String key = keys.next();
                Log.d("Key " + i, key);
                i++;
                Log.d("Currency JSON", currencies.get(key).toString());
                arrayList.add(new Currency(key, currencies.get(key).toString()));
            }
            Log.d("Currencies JSON", currencies.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("get currencies JSON", e.toString());
        }

        return arrayList;
    }

    public CurrencyConversion getExchangeRateFromJSON(String JSONstring, String to) {
        CurrencyConversion result = null;
        try {
            JSONObject exchangeObj = new JSONObject(JSONstring);

            JSONObject rateObj = (JSONObject) exchangeObj.get("rates");

            String base = (String) exchangeObj.get("base");
            Date date = new SimpleDateFormat("yyyy-mm-dd").parse((String) exchangeObj.get("date"));
            double exchangeRate = (double) rateObj.get(to);


            result = new CurrencyConversion(base, to, date, exchangeRate);
            Log.e("", exchangeObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<NewsArticle> getNewsArticleFromJSON(String JSONstring) {
        ArrayList<NewsArticle> articles = new ArrayList<>();

        try {
            JSONObject response_object = new JSONObject(JSONstring);

            JSONArray articles_object_array = (JSONArray) response_object.get("articles");

            for (int i = 0; i < articles_object_array.length(); i++) {
                JSONObject article_object = articles_object_array.getJSONObject(i);

                String source = (String) ((JSONObject) article_object.get("source")).get("name");
                String title = (String) article_object.get("title");

                String description = "";
                if (article_object.get("description") != JSONObject.NULL) {
                     description = (String) article_object.get("description");
                }

                String url = (String) article_object.get("url");
                String imageUrl = "";
                if (article_object.get("urlToImage") != JSONObject.NULL) {
                    imageUrl = (String) article_object.get("urlToImage");

                }

                NewsArticle article = new NewsArticle(source, title, description, url, imageUrl);
                articles.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return articles;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CurrencyConversion getConversionFromJSON(String JSONstring, String to) throws JSONException, ParseException {
        CurrencyConversion conversion = null;

        JSONObject response_object = new JSONObject(JSONstring);

        JSONObject rates_object = new JSONObject(String.valueOf((JSONObject) response_object.get("rates")));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(response_object.get("date").toString());
        Log.e("Here", response_object.get("date").toString());
        Log.e("Conversion", String.valueOf(response_object.get("date")));
        double rate = Double.parseDouble(String.valueOf(rates_object.get(to)));
        String from = String.valueOf(response_object.get("base"));
        conversion = new CurrencyConversion(from, to, date, rate);

        return conversion;
    }
}

