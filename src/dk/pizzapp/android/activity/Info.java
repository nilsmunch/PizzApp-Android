package dk.pizzapp.android.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.model.Restaurant;

public class Info extends MapActivity {
    private Restaurant restaurant;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        restaurant = ((App) getApplication()).restaurants.get(getIntent().getIntExtra("id", 0));
        mapView = (MapView) findViewById(R.id.info_map);

        ((TextView) findViewById(R.id.info_name)).setText(restaurant.getName());
        ((TextView) findViewById(R.id.info_address)).setText(restaurant.getAddress() + ", " + restaurant.getCity());

        double latitude = Double.parseDouble(restaurant.getLatitude());
        double longitude = Double.parseDouble(restaurant.getLongitude());
        mapView.getController().setCenter(
                new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6))
        );
        mapView.getController().setZoom(18);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void findRoute(View v) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=" + restaurant.getAddress() + ", " + restaurant.getCity()));
        if (isAppInstalled("com.google.android.apps.maps"))
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public void doOrder(View v) {

    }
}
