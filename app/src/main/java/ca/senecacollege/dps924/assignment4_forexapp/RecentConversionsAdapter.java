package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecentConversionsAdapter extends RecyclerView.Adapter<RecentConversionsAdapter.ExchangeViewHolder> {

    interface saveClickListener {
        public void onSaveClicked();
    }
    saveClickListener listener;
    private Context mCtx;
    private ArrayList<CurrencyConversionResult> results;

    public RecentConversionsAdapter(Context mCtx, ArrayList<CurrencyConversionResult> results) {
        this.mCtx = mCtx;
        this.results = results;
        this.listener = (MainActivity) mCtx;
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_recent, parent, false);
        return new ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
        CurrencyConversionResult result = results.get(position);
        holder.conversionResult.setText(result.toString());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ExchangeViewHolder extends RecyclerView.ViewHolder
            implements
            View.OnClickListener
    {
        TextView conversionResult;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            conversionResult = itemView.findViewById(R.id.conversion_result);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            listener.onSaveClicked();
        }
    }


}
