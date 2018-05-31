package com.esiea.instafood;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.System.in;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class LocalisationService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_RESTO = "com.esiea.instafood.action.GET_RESTO";
    private static final String ACTION_JSON = "com.esiea.insatafood.action.JSON";
    public static ArrayList<Restaurant> list_restaurant = new ArrayList<Restaurant>();
    private static final String TAG = "TAG";

    public LocalisationService() {
        super("LocalisationService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionResto(Context context, Location location) {
        Intent intent = new Intent(context, LocalisationService.class);
        intent.setAction(ACTION_GET_RESTO);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionJSON(Context context) {
        Intent intent = new Intent(context, LocalisationService.class);
        intent.setAction(ACTION_JSON);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_RESTO.equals(action)) {
                handleActionResto(LoadingActivity.getLocation(),ParameterActivity.getFilename());
            }
            if (ACTION_JSON.equals(action)){
                handleActionParseJsonToRestaurant(ParameterActivity.getFilename());
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionResto(Location location, String filename) {


        Log.i(TAG, "Handle action Location");
        URL url = null;
        String search = URLEncoder.encode("Restaurants "+ParameterActivity.getType()+" "+ParameterActivity.getPlace());
        String search_here = URLEncoder.encode("Restaurants "+ParameterActivity.getType());
        try{
            Log.i(TAG , "Je suis dans le try");

            if (ParameterActivity.getType().equals(getString(R.string.geolocalisation))){
                url= new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+search_here+"&location="+ location.getLongitude() +","+location.getLatitude() +"&radius=10000&key="+getString(R.string.google_api_key));
            } else {
                url= new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+search+"&location=0,0&radius=10000&key="+getString(R.string.google_api_key));
            }

            Log.i(TAG , "J'ai travaillé l'url");
            HttpURLConnection conn=(HttpsURLConnection) url.openConnection();
            Log.i(TAG , "J'ai travaillé la connection");
            conn.setRequestMethod("GET");
            conn.connect();
            Log.i(TAG , "Connexion etablie");
            if(HttpsURLConnection.HTTP_OK == conn.getResponseCode()){
                Log.i(TAG , "Je rentre dans le if");
                copyInputStreamToFile(conn.getInputStream() , new File(getCacheDir(), filename));
                Log.i(TAG, "restos.json DOWLOADED");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LoadingActivity.LOCATION_UPDATE));

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("TAG" , "Je suis une exception");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void  handleActionParseJsonToRestaurant(String filename) {

        try {
            list_restaurant.clear();

            JSONObject jsonObject = getAllLocationsFromFile(ParameterActivity.getFilename());

            JSONArray results = jsonObject.getJSONArray("results");
                results = jsonObject.getJSONArray("results");
                Log.i(TAG, results.toString());

                //Parsing
                for (int i = 0; i < results.length(); i++) {
                    JSONObject data = results.getJSONObject(i);

                    String name = data.getString("name");
                    String address = data.getString("formatted_address");

                    Restaurant restaurant = new Restaurant(address, name);
                    Log.i(TAG, "\n" + restaurant.toString() + "\n");

                    list_restaurant.add(restaurant);
                }

            Log.i("TAG", list_restaurant.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONObject getAllLocationsFromFile(String filename) {
        try {
            InputStream fileInputStream = new FileInputStream(getCacheDir() + "/"+filename);
            return new JSONObject(convertStreamToString(fileInputStream));
        }  catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<Restaurant> getList_restaurant() {
        return list_restaurant;
    }

    public static void setList_restaurant(ArrayList<Restaurant> list_restaurant) {
        LocalisationService.list_restaurant = list_restaurant;
    }
}
