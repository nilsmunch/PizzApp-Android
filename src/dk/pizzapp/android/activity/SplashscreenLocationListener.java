package dk.pizzapp.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.*;
import android.os.Bundle;
import android.provider.Settings;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import dk.pizzapp.android.App;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

            // Unregister with the LocationManager
            locationManager.removeUpdates(this);

            // Convert the location to an address using the Geocoder API
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            // Set the location and address in the application class
            App.location = location;
            App.address = addressList.get(0);

            // Register the device with the server
            if (!isDeviceRegistered())
                registerDevice();
            else {
                activity.startActivity(new Intent(activity, Main.class));
                activity.finish();
            }
        }

        // Continue looking for a location if we got an error parsing the current one
        catch (Exception e) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
        }
    }

    private void registerDevice() {

        // Instantiate android-query and register with the server
        AQuery aQuery = new AQuery(activity);
        String url = "http://affiliate.pizzapp.dk/register.php";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uuid", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
        params.put("url", "new");
        params.put("type", "android");

        aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {

                if (status.getCode() == 200) {
                    // Set device as registered
                    SharedPreferences.Editor editor = activity.getSharedPreferences("PizzApp", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("registered", true);
                    editor.commit();
                    editor.apply();
                }

                // Finish the splashscreen and continue to the main activity
                activity.startActivity(new Intent(activity, Main.class));
                activity.finish();
            }
        });
    }

    private boolean isDeviceRegistered() {

        // Check the registered state from the shared preferences
        SharedPreferences preferences = activity.getSharedPreferences("PizzApp", Context.MODE_PRIVATE);
        return preferences.getBoolean("registered", false);
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
