package ca.senecacollege.dps924.assignment4_forexapp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener, NetworkingService.CurrencyHistoryListener,
 DatabaseManager.DatabaseListener{
    NetworkingService networkingService;
    JsonService JsonService;
    DataManager dataManager;
    ArrayAdapter currencyArrayAdapter;
    List<CurrencyConversionResult> savedConversions;
    HistoryAdapter savedConversionsAdapter;

    Spinner from;
    Spinner to;
    DatePicker datePicker;
    Button exchange;
    TextView conversion_text;
    RecyclerView savedExchanges;
    ItemTouchHelper itemSwipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_fragment, container, false);
        from = v.findViewById(R.id.history_from_spinner);
        to = v.findViewById(R.id.history_to_spinner);
        datePicker = v.findViewById(R.id.history_date_picker);
        exchange = v.findViewById(R.id.history_exchange_btn);
        conversion_text = v.findViewById(R.id.history_conversion_result);
        savedExchanges = v.findViewById(R.id.history_saved_result);
        savedExchanges.setLayoutManager(new LinearLayoutManager(getContext()));

        datePicker.setMinDate(946720800);
        datePicker.setMaxDate(System.currentTimeMillis());
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.networkingService = ((MyApp)getActivity().getApplication()).getNetworkingService();
        this.JsonService = ((MyApp)getActivity().getApplication()).getJsonService();
        this.dataManager = ((MyApp)getActivity().getApplication()).getDataManager();

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Historical exchange", "executed");

                try {
                    Date date = new SimpleDateFormat("yyyy-mm-dd").parse(datePicker.getYear()
                            + "-" + (datePicker.getMonth() < 10 ? "0" + (datePicker.getMonth()+1) : (datePicker.getMonth()+1)) +
                            "-" + (datePicker.getDayOfMonth() < 10 ? "0" + datePicker.getDayOfMonth(): datePicker.getDayOfMonth() ));

                    Log.e("Selected date", String.valueOf(datePicker.getMonth()));

                    networkingService.historyExchangeCurrency(from.getSelectedItem().toString(), to.getSelectedItem().toString(), date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("Error", e.toString());
                }
            }
        });

        currencyArrayAdapter = new ArrayAdapter(getContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, dataManager.dropDown);
        from.setAdapter(currencyArrayAdapter);
        to.setAdapter(currencyArrayAdapter);
        from.setOnItemSelectedListener(this);
        to.setOnItemSelectedListener(this);

        if (!dataManager.history_from_selection.equals("")) {
            from.setSelection(dataManager.dropDown.indexOf(dataManager.history_from_selection));
            to.setSelection(dataManager.dropDown.indexOf(dataManager.history_to_selection));
        }

        itemSwipe = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                dataManager.dbManager.removeConversion(savedConversions.get(position));
            }
        });
        itemSwipe.attachToRecyclerView(savedExchanges);

        this.networkingService.historyListener = this;
        dataManager.dbManager.listener = this;

        if (dataManager.savedConversions.size() == 0) {
            dataManager.dbManager.getAllConversions();
        }
        else {
            this.savedConversions = dataManager.savedConversions;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (from.getSelectedItem().toString().equals(to.getSelectedItem().toString())) {
            exchange.setClickable(false);
            exchange.setEnabled(false);
        }
        else {
            exchange.setClickable(true);
            exchange.setEnabled(true);
        }
        dataManager.history_from_selection = from.getSelectedItem().toString();
        dataManager.history_to_selection = to.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void getCurrencyHistory(String JSONstring, String to) {
        try {
            CurrencyConversion conversion = this.JsonService.getConversionFromJSON(JSONstring, to);

            conversion_text.setText(conversion.toString());
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListReady(List<CurrencyConversionResult> conversions) {
        Log.d("onListReady amount", String.valueOf(conversions.size()));
        this.savedConversions = conversions;
        savedConversionsAdapter = new HistoryAdapter(getContext(), savedConversions);
        savedExchanges.setAdapter(savedConversionsAdapter);
        savedConversionsAdapter.notifyDataSetChanged();

    }

    @Override
    public void removeConversion(CurrencyConversionResult conversion) {
        savedConversions.remove(conversion);
        savedConversionsAdapter.notifyDataSetChanged();
    }
}
