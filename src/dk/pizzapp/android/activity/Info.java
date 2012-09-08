package dk.pizzapp.android.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;

public class Info extends MapActivity {
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        // Get the mapview
        mapView = (MapView) findViewById(R.id.info_map);

        // Set the text fields
        ((TextView) findViewById(R.id.info_name)).setText(App.restaurant.getName());
        ((TextView) findViewById(R.id.info_address)).setText(App.restaurant.getAddress() + ", " + App.restaurant.getCity());

        // Set the map center and zoom to the restaurants locations
        mapView.getController().setCenter(
                new GeoPoint(
                        (int) (Double.parseDouble(App.restaurant.getLatitude()) * 1E6),
                        (int) (Double.parseDouble(App.restaurant.getLongitude()) * 1E6))
        );
        mapView.getController().setZoom(18);

        // Add a map marker
        MapOverlay mapOverlay = new MapOverlay();
        mapView.getOverlays().clear();
        mapView.getOverlays().add(mapOverlay);
        mapView.invalidate();
    }

    class MapOverlay extends Overlay {

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);

            Point screenPoint = new Point();
            mapView.getProjection().toPixels(new GeoPoint(
                    (int) (Double.parseDouble(App.restaurant.getLatitude()) * 1E6),
                    (int) (Double.parseDouble(App.restaurant.getLongitude()) * 1E6))
                    , screenPoint);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
            canvas.drawBitmap(bitmap, screenPoint.x, screenPoint.y - bitmap.getHeight(), null);
        }


    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
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
                Uri.parse("geo:0,0?q=" + App.restaurant.getAddress() + ", " + App.restaurant.getCity()));
        if (isAppInstalled("com.google.android.apps.maps"))
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public void doOrder(View v) {
        startActivity(new Intent(Info.this, Order.class).putExtra("id", getIntent().getIntExtra("id", 0)));
    }
}
