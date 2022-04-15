package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExchangeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, NetworkingService.CurrencyNetworkingListener,
RecentConversionsAdapter.exchangeClickListener, DatabaseManager.databaseAddListener{
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
    ArrayAdapter currencyArrayAdapter;

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
        adapter.listener = this;
        recent.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.networkingService = ((MyApp)getActivity().getApplication()).getNetworkingService();
        this.JsonService = ((MyApp)getActivity().getApplication()).getJsonService();
        this.dataManager = ((MyApp)getActivity().getApplication()).getDataManager();
        this.dataManager.dbManager.getDB(getContext());

        dataManager.dbManager.addListener=this;
        this.networkingService.listener = this;

        if (dataManager.currencies.size() == 0) {
            this.networkingService.getAllCurrencies();
        }

        currencyArrayAdapter = new ArrayAdapter(getContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, dataManager.dropDown);
        fromSpinner.setAdapter(currencyArrayAdapter);
        toSpinner.setAdapter(currencyArrayAdapter);
        fromSpinner.setOnItemSelectedListener(this);
        toSpinner.setOnItemSelectedListener(this);


        from_amount.setText("");
        to_amount.setText("");

        if (!dataManager.from_selection.equals("")) {
            fromSpinner.setSelection(dataManager.dropDown.indexOf(dataManager.from_selection));
            toSpinner.setSelection(dataManager.dropDown.indexOf(dataManager.to_selection));
        }
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
        dataManager.from_selection = fromSpinner.getSelectedItem().toString();
        dataManager.to_selection = toSpinner.getSelectedItem().toString();
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
        currencyArrayAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void currencyConversionListener(String JSONstring, String to, double amountRequested) {
        CurrencyConversion conversion = JsonService.getExchangeRateFromJSON(JSONstring, to);

        double amountTo = conversion.rate * Double.parseDouble(from_amount.getText().toString());
        DecimalFormat df = new DecimalFormat("0.00");
        to_amount.setText(df.format(amountTo));

        CurrencyConversionResult result = new CurrencyConversionResult(conversion.base, conversion.destination, amountTo, amountRequested, new Date().toInstant().toEpochMilli());
        recentConversions.add(result);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveClicked(CurrencyConversionResult currencyConversionResult) {
        this.dataManager.dbManager.addConversion(currencyConversionResult);
    }

    @Override
    public void onConversionAdded() {
        Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
    }
}
