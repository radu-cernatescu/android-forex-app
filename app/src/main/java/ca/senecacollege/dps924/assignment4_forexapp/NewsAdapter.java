package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context mCtx;
    private ArrayList<NewsArticle> articles;

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_news, parent, false);

        return new NewsAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articles.get(position);
        holder.articleTitle.setText(article.title);
        if (article.image != null) {
            holder.article_image.setImageBitmap(article.image);
        }
        else {
            holder.article_image.setImageDrawable(mCtx.getDrawable(R.drawable.news));
        }
        if (article.description.length() == 0) {
            holder.article_description.setText(R.string.no_description);
        } else {
            holder.article_description.setText(article.description);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(article.url));
                mCtx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public NewsAdapter(Context mCtx, ArrayList<NewsArticle> articles) {
        this.mCtx = mCtx;
        this.articles = articles;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle;
        ImageView article_image;
        TextView article_description;
        ConstraintLayout layout;

        public NewsViewHolder(View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.article_title);
            article_image = itemView.findViewById(R.id.article_image);
            article_description = itemView.findViewById(R.id.article_description);
            layout = itemView.findViewById(R.id.article_layout);
        }
    }
}
