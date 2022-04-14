package ca.senecacollege.dps924.assignment4_forexapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class NewsArticle {
    String source;
    String title;
    String description;
    String url;
    String imageUrl;
   Bitmap image;

    NewsArticle(String source, String title, String description, String url, String imageUrl) {
        this.source = source;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        image = null;
    }

    public String toString() {
        return this.title;
    }
}
