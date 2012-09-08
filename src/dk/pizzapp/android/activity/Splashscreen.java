package dk.pizzapp.android.activity;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import dk.pizzapp.android.R;

public class Splashscreen extends Activity {
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        // Register cell-tower location updates with the Location Manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 500, 1000, new SplashscreenLocationListener(this, locationManager));
    }
}
