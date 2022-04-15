package ca.senecacollege.dps924.assignment4_forexapp;

import android.app.Application;

import androidx.room.Room;

public class MyApp extends Application {
    public static DataManager dataManager = new DataManager();
    private NetworkingService networkingService = new NetworkingService();
    private JsonService JsonService = new JsonService();

    public NetworkingService getNetworkingService(){return networkingService;}
    public JsonService getJsonService() {return JsonService;}
    public DataManager getDataManager() {return dataManager;}
}
