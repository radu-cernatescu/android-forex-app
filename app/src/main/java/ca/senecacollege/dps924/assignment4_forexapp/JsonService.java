package ca.senecacollege.dps924.assignment4_forexapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
}
