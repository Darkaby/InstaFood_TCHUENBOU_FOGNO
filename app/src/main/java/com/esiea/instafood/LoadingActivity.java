package com.esiea.instafood;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoadingActivity extends AppCompatActivity {

    public static final String LOCATION_UPDATE = "com.esiea.instafood.LOCATION_UPDATE";
    private static Location location;
    Button access_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        findLocation();

    }

    @SuppressLint("SetTextI18n")
    public void findLocation() {

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                //return;
            }
        } else {

            assert lm != null;
            Intent intent = new Intent(this, GPSReceiver.class);
            PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, pending);
            lm.removeUpdates(pending);

            setLocation(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            if (location != null) {
                LocalisationService.startActionResto(LoadingActivity.this, location);
                IntentFilter intentFilter = new IntentFilter(LOCATION_UPDATE);
                LocalBroadcastManager.getInstance(this).registerReceiver(new LocationUpdate(), intentFilter);
            }
        }

    }

    public class LocationUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Log.i("TAG", "Téléchargement terminé");
            assert notificationManager != null;
            new NotiClass(LoadingActivity.this, notificationManager, "Téléchargement terminé!");

            LocalisationService.startActionJSON(LoadingActivity.this);
            new NotiClass(LoadingActivity.this, "Parse json fait");

            ((Button) findViewById(R.id.access_list)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoadingActivity.this, RestaurantActivity.class));
                }
            }); // prévoir une action de notification ici
        }
    }

    public class GPSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setLocation((Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                break;
            default:
                break;
        }
    }

    public static Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        LoadingActivity.location = location;
    }
}
