package dk.pizzapp.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Splashscreen extends Activity implements LocationListener {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            locationManager.removeUpdates(this);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addressList.get(0);

            ((App)(getApplication())).location = address;

            startActivity(new Intent(Splashscreen.this, Main.class));
            finish();
        } catch (IOException e) {
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
