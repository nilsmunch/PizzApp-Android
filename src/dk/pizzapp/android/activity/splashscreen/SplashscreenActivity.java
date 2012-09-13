package dk.pizzapp.android.activity.splashscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import dk.pizzapp.android.R;

public class SplashscreenActivity extends Activity {
    private LocationManager locationManager;
    private SplashscreenLocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Instantiate our location listener
        locationListener = new SplashscreenLocationListener(this, locationManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if a network connection is available
        if (!isNetworkAvailable()) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setMessage("No Internet Available!")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            alertBuilder.show();
            return;
        }

        // Check if the network location provider is available
        if (!isLocationAvailable()) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setMessage("Location Services Disabled!")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            alertBuilder.show();
            return;
        }

        // Register cell-tower location updates with the Location Manager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1000, locationListener);
    }

    @Override
    protected void onPause() {

        // Unregister our location listener if its running
        locationManager.removeUpdates(locationListener);

        super.onPause();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isLocationAvailable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
