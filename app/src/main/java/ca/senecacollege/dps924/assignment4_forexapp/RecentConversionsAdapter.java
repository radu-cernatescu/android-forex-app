package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecentConversionsAdapter extends RecyclerView.Adapter<RecentConversionsAdapter.ExchangeViewHolder> {

    interface exchangeClickListener{
        void onSaveClicked(CurrencyConversionResult conversionResult);
    }
    public exchangeClickListener listener;
    private Context mCtx;
    private ArrayList<CurrencyConversionResult> results;

    public RecentConversionsAdapter(Context mCtx, ArrayList<CurrencyConversionResult> results) {
        this.mCtx = mCtx;
        this.results = results;
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
        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.saveBtn.setClickable(false);
                holder.saveBtn.setEnabled(false);
                holder.saveBtn.setText(R.string.recent_conversions_save_btn_post);
                listener.onSaveClicked(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ExchangeViewHolder extends RecyclerView.ViewHolder
    {
        TextView conversionResult;
        Button saveBtn;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            conversionResult = itemView.findViewById(R.id.conversion_result);
            saveBtn = itemView.findViewById(R.id.save_recent_button);
        }
    }


}
