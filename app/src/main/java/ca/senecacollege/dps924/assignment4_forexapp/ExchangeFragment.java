package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class ExchangeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, NetworkingService.CurrencyNetworkingListener{
    Spinner fromSpinner;
    Spinner toSpinner;
    Button exchangeButton;
    EditText from_amount;
    EditText to_amount;
    RecyclerView recent;
    RecentConversionsAdapter adapter;

    NetworkingService networkingService;
    JsonService JsonService;
    DataManager dataManager;

    ArrayList<String> shortForm;
    ArrayList<String> longForm;
    ArrayList<CurrencyConversionResult> recentConversions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exchange_fragment, container, false);

        shortForm = new ArrayList<>();
        longForm = new ArrayList<>();
        recentConversions = ((MyApp)getActivity().getApplication()).getDataManager().results;
        Log.d("List of currencies", shortForm.toString());

        fromSpinner = v.findViewById(R.id.from_spinner);
        toSpinner = v.findViewById(R.id.to_spinner);
        exchangeButton = v.findViewById(R.id.exchange_btn);
        exchangeButton.setOnClickListener(this);
        exchangeButton.setEnabled(false);
        from_amount = v.findViewById(R.id.from_amount);
        to_amount = v.findViewById(R.id.to_amount);
        to_amount.setEnabled(false);
        recent = v.findViewById(R.id.recent);
        recent.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecentConversionsAdapter(getContext(), recentConversions);
        recent.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.networkingService = ((MyApp)getActivity().getApplication()).getNetworkingService();
        this.JsonService = ((MyApp)getActivity().getApplication()).getJsonService();
        this.dataManager = ((MyApp)getActivity().getApplication()).getDataManager();

        this.networkingService.listener = this;
        if (dataManager.currencies.size() == 0) {
            this.networkingService.getAllCurrencies();
        }

        ArrayAdapter currencyArrayAdapter = new ArrayAdapter(getContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, dataManager.dropDown);
        fromSpinner.setAdapter(currencyArrayAdapter);
        toSpinner.setAdapter(currencyArrayAdapter);
        fromSpinner.setOnItemSelectedListener(this);
        toSpinner.setOnItemSelectedListener(this);


        from_amount.setText("");
        to_amount.setText("");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.exchange_btn:
                networkingService.exchangeCurrencies(fromSpinner.getSelectedItem().toString(), toSpinner.getSelectedItem().toString(), Double.parseDouble(from_amount.getText().toString()));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (fromSpinner.getSelectedItem().toString().equals(toSpinner.getSelectedItem().toString())) {
            exchangeButton.setClickable(false);
            exchangeButton.setEnabled(false);
        }
        else {
            exchangeButton.setClickable(true);
            exchangeButton.setEnabled(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if (fromSpinner.getSelectedItem().toString().equals(toSpinner.getSelectedItem().toString())) {
            exchangeButton.setClickable(false);
            exchangeButton.setEnabled(false);
        }
        else {
            exchangeButton.setClickable(true);
            exchangeButton.setEnabled(true);
        }
    }

    @Override
    public void currencyDataListener(String JSONstring) {
        if (dataManager.currencies.size() == 0) {
            dataManager.currencies = JsonService.getCurrenciesFromJSON(JSONstring);

            for (int i = 0; i < dataManager.currencies.size(); i++) {
                dataManager.dropDown.add(dataManager.currencies.get(i).getShortForm());
            }

        }
        try {
            ArrayAdapter currencyArrayAdapter = new ArrayAdapter(getContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, dataManager.dropDown);
            fromSpinner.setAdapter(currencyArrayAdapter);
            toSpinner.setAdapter(currencyArrayAdapter);
            fromSpinner.setOnItemSelectedListener(this);
            toSpinner.setOnItemSelectedListener(this);
        } catch (NullPointerException e) {}
    }

    @Override
    public void currencyConversionListener(String JSONstring, String to, double amountRequested) {
        CurrencyConversion conversion = JsonService.getExchangeRateFromJSON(JSONstring, to);

        Log.e("Conversion", conversion.toString());

        //Log.e("amountRequested", String.valueOf(amountRequested));
        double amountTo = conversion.rate * Double.parseDouble(from_amount.getText().toString());
        to_amount.setText(String.valueOf(amountTo));

        CurrencyConversionResult result = new CurrencyConversionResult(conversion.base, conversion.destination, amountTo, amountRequested, new Date());
        recentConversions.add(result);
        adapter.notifyDataSetChanged();
        Log.e("Result", result.toString());
    }

}
