package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    NewsAdapter.ArticleClickListener listener;
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
        holder.article_image.setImageBitmap(article.image);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    interface ArticleClickListener {
        void onArticleClicked();

    }


    public NewsAdapter(Context mCtx, ArrayList<NewsArticle> articles) {
        this.mCtx = mCtx;
        this.articles = articles;
        this.listener = (MainActivity) mCtx;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder
            implements
            View.OnClickListener
    {
        TextView articleTitle;
        ImageView article_image;


        public NewsViewHolder(View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.article_title);
            article_image = itemView.findViewById(R.id.article_image);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onArticleClicked();
        }
    }
}
