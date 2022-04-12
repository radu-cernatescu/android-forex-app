package ca.senecacollege.dps924.assignment4_forexapp;

import android.app.Application;

public class MyApp extends Application {

    private NetworkingService networkingService = new NetworkingService();

    private JsonService JsonService = new JsonService();

    public NetworkingService getNetworkingService(){return networkingService;}

    public JsonService getJsonService() {return JsonService;}
}
