package ca.senecacollege.dps924.assignment4_forexapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    interface DatabaseListener {
        void onListReady(List<CurrencyConversionResult> conversions);
        void removeConversion(CurrencyConversionResult conversion);
    }

    interface databaseAddListener {
        void onConversionAdded();
    }

    DatabaseListener listener;
    databaseAddListener addListener;
    static CurrencyConversionDb db;

    ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(4);
    Handler mainThread_Handler = new Handler(Looper.getMainLooper());

    private static void buildDBInstance(Context context) {
        db = Room.databaseBuilder(context, CurrencyConversionDb.class, "conversion_db").build();
    }

    public CurrencyConversionDb getDB(Context context) {
        if (db == null) {
            buildDBInstance(context);
        }
        return db;
    }

    public void getAllConversions() {
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<CurrencyConversionResult> savedConversions = db.currencyConversionDao().getAllConversions().blockingGet();
                mainThread_Handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onListReady(savedConversions);
                    }
                });
            }
        });
    }

    public void addConversion(CurrencyConversionResult conversion) {
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.currencyConversionDao().addConversion(conversion);
                mainThread_Handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("conversion", String.valueOf("Added"));
                        addListener.onConversionAdded();
                    }
                });
            }
        });
    }

    public void removeConversion(CurrencyConversionResult conversion) {
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.currencyConversionDao().removeConversion(conversion);
                mainThread_Handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("conversion", String.valueOf("Removed"));
                        listener.removeConversion(conversion);
                    }
                });
            }
        });
    }

    public void removeAllConversions() {
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.currencyConversionDao().removeAllConversions();
            }
        });
    }
}
