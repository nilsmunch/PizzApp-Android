package dk.pizzapp.android.activity.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.provider.Settings;
import dk.pizzapp.android.App;
import dk.pizzapp.android.activity.main.MainActivity;
import dk.pizzapp.android.api.PizzaService;

import java.util.List;
import java.util.Locale;

public class SplashscreenLocationListener implements LocationListener {

    private Activity activity;
    private LocationManager locationManager;

    public SplashscreenLocationListener(Activity activity, LocationManager locationManager) {
        this.activity = activity;
        this.locationManager = locationManager;
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            locationManager.removeUpdates(this);

            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            App.location = location;
            App.address = addressList.get(0);

            PizzaService.getInstance(activity.getApplication()).registerDevice(Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID), "new", "android");

            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (Exception e) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1000, this);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
