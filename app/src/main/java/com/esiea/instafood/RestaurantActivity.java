package com.esiea.instafood;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RestaurantActivity extends AppCompatActivity {

    private FusedLocationProviderClient locationProviderClient;
    private static Location location;
    private MenuItem menuItem;
    private RecyclerView list_restaurants;
    public static final String LOCATION_UPDATE = "com.esiea.instafood.LOCATION_UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        //////////////////
        //// Location ////
        //////////////////

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                //return;
            }
        } else {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (ActivityCompat.checkSelfPermission(RestaurantActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(RestaurantActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (location != null) {
                        //latitude.append("\n Longitude: " + location.getLongitude() + "; Latitude: " + location.getLatitude());
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            };

            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
            setLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

            if (location != null){
                //latitude.append("\n Longitude: " + location.getLongitude() + "; Latitude: " + location.getLatitude());
                LocalisationService.startActionLocal(RestaurantActivity.this, location);
            }

            //IntentFilter intent = new IntentFilter(LOCATION_UPDATE);
            //PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        //////////////////
        // RecyclerView //
        //////////////////

        RecyclerView list_restaurants = (RecyclerView)findViewById(R.id.rv_restaurant);
        list_restaurants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list_restaurants.setAdapter(new RestoAdaptater(getRestoFromFile()));

        IntentFilter intentFilter = new IntentFilter(LOCATION_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new LocationUpdate(), intentFilter);

    }


    //////////////////
    // Gestion Menu //
    //////////////////

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.geo:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+getLocation().getLatitude()+","+getLocation().getLongitude()+"?q=Restaurants")));
                return true;
            case R.id.sign_out:
                //Toast.makeText(MainActivity.this, " Item du Menu sélectionné", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, SecondActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //////////////////
    // Gestion Menu //
    //////////////////

    ///////////////////////////////
    // Localisation du téléphone //
    ///////////////////////////////

        public class LocationUpdate extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                setLocation((Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED));
                //list_restaurants.updateViewLayout(RestaurantActivity.this, new ViewGroup.LayoutParams();
                //list_restaurants.getAdapter().;
                Log.i("TAG", "Téléchargement terminé"); // prévoir une action de notification ici

            }
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            switch (requestCode) {
                case 10:
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    break;
                default:
                    break;
            }
        }

        public static Location getLocation () {
            return location;
        }

        public void setLocation(Location location){
            RestaurantActivity.location = location;
        }

    ///////////////////////////////
    // Localisation du téléphone //
    ///////////////////////////////

    //////////////////
    // RecyclerView //
    //////////////////


    public JSONArray getRestoFromFile(){
        try {
            InputStream inputStream = new FileInputStream(getCacheDir() + "/" + "restos.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            return  new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
