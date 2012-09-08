package dk.pizzapp.android.activity;

import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import dk.pizzapp.android.App;

import java.util.List;
import java.util.Locale;

public class SplashscreenLocationListener implements LocationListener {
    private Context context;
    private LocationManager locationManager;

    public SplashscreenLocationListener(Context context, LocationManager locationManager) {
        this.context = context;
        this.locationManager = locationManager;
    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            // Unregister with the LocationManager
            locationManager.removeUpdates(this);

            // Convert the location to an address using the Geocoder API
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            // Set the location and address in the application class
            App.location = location;
            App.address = addressList.get(0);

            // Finish the splashscreen and continue to the main activity
            context.startActivity(new Intent(context, Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        // Continue looking for a location if we got an error parsing the current one
        catch (Exception e) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
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
