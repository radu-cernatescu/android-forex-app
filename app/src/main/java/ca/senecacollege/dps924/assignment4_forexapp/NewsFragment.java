package ca.senecacollege.dps924.assignment4_forexapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class NewsFragment extends Fragment implements NetworkingService.NewsNetworkingListener {

    TextView articleTitle;

    NewsAdapter adapter;
    RecyclerView newsArticles;

    NetworkingService networkingService;
    JsonService JsonService;
    DataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_fragment, container, false);
        articleTitle = v.findViewById(R.id.article_title);
        newsArticles = v.findViewById(R.id.recycler_article);
        newsArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        this.networkingService = ((MyApp)getActivity().getApplication()).getNetworkingService();
        this.JsonService = ((MyApp)getActivity().getApplication()).getJsonService();
        this.dataManager = ((MyApp)getActivity().getApplication()).getDataManager();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (dataManager.articles.size() == 0) {
            networkingService.getNews();
        }

        this.networkingService.newsListener = this;
        adapter = new NewsAdapter(getContext(), this.dataManager.articles);
        newsArticles.setAdapter(adapter);
    }

    @Override
    public void getArticlesListener(String JSONstring) {
        dataManager.articles = JsonService.getNewsArticleFromJSON(JSONstring);

        int i = 0;
        for (NewsArticle article : dataManager.articles) {
            networkingService.getArticleImage(article.imageUrl, i);
            i++;
        }
        adapter = new NewsAdapter(getContext(), this.dataManager.articles);
        newsArticles.setAdapter(adapter);

    }

    @Override
    public void setImage(Bitmap image, int index) {
        if (image != null) {
            Log.e("image", image.toString());
            dataManager.articles.get(index).image = image;
        }

        adapter = new NewsAdapter(getContext(), this.dataManager.articles);
        newsArticles.setAdapter(adapter);

    }
}
