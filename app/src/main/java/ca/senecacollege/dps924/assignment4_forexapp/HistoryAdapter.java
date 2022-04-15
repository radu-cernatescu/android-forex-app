package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ExchangeViewHolder>{

    private Context mCtx;
    private List<CurrencyConversionResult> conversions;

    HistoryAdapter(Context mCtx, List<CurrencyConversionResult> conversions) {
        this.mCtx = mCtx;
        this.conversions = conversions;
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_conversions, parent, false);
        return new HistoryAdapter.ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
        CurrencyConversionResult conversion = conversions.get(position);

        holder.conversionResult.setText(conversion.toString());
    }

    @Override
    public int getItemCount() {
        return conversions.size();
    }

    class ExchangeViewHolder extends RecyclerView.ViewHolder {

        TextView conversionResult;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            conversionResult = itemView.findViewById(R.id.history_conversion_result);
        }
    }
}
