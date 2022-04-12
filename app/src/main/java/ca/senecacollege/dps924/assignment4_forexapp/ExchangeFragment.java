package ca.senecacollege.dps924.assignment4_forexapp;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ExchangeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner fromSpinner;
    Spinner toSpinner;
    Button exchangeButton;

    //NetworkingService networkingService = ((MyApp)getActivity().getApplication()).getNetworkingService();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exchange_fragment, container, false);

        ArrayList<String> shortForm = new ArrayList<>();
        ArrayList<String> longForm = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shortForm = bundle.getStringArrayList("short_form");
            longForm = bundle.getStringArrayList("long_form");
        }
        Log.d("List of currencies", shortForm.toString());

        fromSpinner = v.findViewById(R.id.from_spinner);
        toSpinner = v.findViewById(R.id.to_spinner);
        exchangeButton = v.findViewById(R.id.exchange_btn);
        exchangeButton.setOnClickListener(this);

        ArrayAdapter currencyArrayAdapter = new ArrayAdapter(getContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, shortForm);
        fromSpinner.setAdapter(currencyArrayAdapter);
        toSpinner.setAdapter(currencyArrayAdapter);
        fromSpinner.setOnItemSelectedListener(this);
        toSpinner.setOnItemSelectedListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.exchange_btn:

                //this.networkingService.exchangeCurrencies(fromSpinner.toString(), toSpinner.toString());
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

}
