package ca.senecacollege.dps924.assignment4_forexapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter {
    class ExchangeViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle;
        ImageView article_image;
        TextView article_description;
        ConstraintLayout layout;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.article_title);
            article_image = itemView.findViewById(R.id.article_image);
            article_description = itemView.findViewById(R.id.article_description);
            layout = itemView.findViewById(R.id.article_layout);
        }
    }
}
