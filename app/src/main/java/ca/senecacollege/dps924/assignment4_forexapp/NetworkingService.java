package ca.senecacollege.dps924.assignment4_forexapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import android.os.Handler;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

public class NetworkingService {
    private String forexURL = "https://frankfurter.app/";
    private String newsURL = "https://newsapi.org/v2/";
    private String newsAPIkey = "&apiKey=37bb457f9dce4f75a18f60640e187a70";
    private double from_amount;
    private String from;
    private String to;

    public static ExecutorService networkExecutorService = Executors.newFixedThreadPool(4);
    public static Handler networkingHandler = new Handler(Looper.getMainLooper());


    interface CurrencyNetworkingListener {
        void currencyDataListener(String JSONstring);
        void currencyConversionListener(String JSONstring, String to, double amountRequested);
    }

    public CurrencyNetworkingListener listener;

    interface  NewsNetworkingListener {
        void getArticlesListener(String JSONstring);
        void setImage(Bitmap image, int index);
    }

    NewsNetworkingListener newsListener;

    public void getAllCurrencies() {
        String urlString = forexURL + "currencies/";
        connect(urlString, "get_all");
    }

    public void exchangeCurrencies(String from, String to, double from_amount) {
        String urlString = forexURL + "latest?from=" + from + "&to=" + to;
        this.from = from;
        this.to = to;
        this.from_amount = from_amount;
        connect(urlString, "convert");
    }

    public void getNews() {
        String urlString = newsURL + "top-headlines?category=business&country=ca" + newsAPIkey;
        connect(urlString, "news");
    }

    public void getArticleImage(String url, int index) {
        AtomicReference<Bitmap> image = new AtomicReference<>();
        networkExecutorService.execute(() -> {
            InputStream is = null;

            try {
                if (!url.equals("")) {
                    is = (InputStream) new URL(url).getContent();

                    image.set(BitmapFactory.decodeStream(is));
                    if (image.get() != null) {
                        Log.e("Image networking", image.toString());
                    }
                }
                networkingHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        newsListener.setImage(image.get(), index);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void connect(String url, String type) {
        final int[] status = {0};
        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection httpsURLConnection = null;
                String JSONstring = "";
                try {
                    URL urlObject = new URL(url);

                    httpsURLConnection = (HttpsURLConnection) urlObject.openConnection();
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setRequestProperty("Content-Type","application/json");
                    httpsURLConnection.addRequestProperty("User-Agent", "Mozilla");
                    httpsURLConnection.connect();


                    status[0] = httpsURLConnection.getResponseCode();
                    InputStream in = httpsURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int inputStreamData = 0;
                    while ( (inputStreamData = reader.read()) != -1){
                        char current = (char)inputStreamData;
                        JSONstring += current;
                    }
                    in.close();
                    reader.close();

                    Log.e("JSON", JSONstring);

                    final String finalJSONstring = JSONstring;
                    if (type.equals("get_all")) {
                        networkingHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.currencyDataListener(finalJSONstring);
                            }
                        });
                    }
                    else if (type.equals("convert")) {
                        networkingHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.currencyConversionListener(finalJSONstring, to, from_amount);
                            }
                        });
                    }
                    else if (type.equals("news")) {
                        networkingHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                newsListener.getArticlesListener(finalJSONstring);
                            }
                        });
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("JSON", e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON", status[0] + ": " + e.toString());
                } finally {
                    assert httpsURLConnection != null;
                    httpsURLConnection.disconnect();
                }
            }
        });
    }
}
