package ca.senecacollege.dps924.assignment4_forexapp;

import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

public class NetworkingService {
    private String forexURL = "https://frankfurter.app/";

    public static ExecutorService networkExecutorService = Executors.newFixedThreadPool(4);
    public static Handler networkingHandler = new Handler(Looper.getMainLooper());


    interface NetworkingListener {
        void currencyDataListener(String JSONstring);
        void currencyConversionListener(String JSONstring);
    }

    public NetworkingListener listener;

    public void getAllCurrencies() {
        String urlString = forexURL + "currencies/";
        connect(urlString, "get_all");
    }

    public void exchangeCurrencies(String from, String to) {
        String urlString = forexURL + "latest?from=" + from + "&to=" + to;
        connect(urlString, "convert");
    }

    public void connect(String url, String type) {
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
                    httpsURLConnection.connect();

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

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("JSON", e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON",  e.toString());
                } finally {
                    assert httpsURLConnection != null;
                    httpsURLConnection.disconnect();
                }
            }
        });
    }
}
