package dk.pizzapp.android.activity.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.activity.order.OrderActivity;

public class InfoActivity extends MapActivity {
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        initUi();
        initMap();
    }

    private void initUi() {
        ((TextView) findViewById(R.id.info_name)).setText(App.restaurant.getName());
        ((TextView) findViewById(R.id.info_address)).setText(App.restaurant.getAddress() + ", " + App.restaurant.getCity());

        findViewById(R.id.info_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNavigate();
            }
        });
        findViewById(R.id.info_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOrder();
            }
        });
    }

    private void initMap() {
        mapView = (MapView) findViewById(R.id.info_map);

        mapView.getController().setCenter(new GeoPoint(
                (int) (Double.parseDouble(App.restaurant.getLatitude()) * 1E6),
                (int) (Double.parseDouble(App.restaurant.getLongitude()) * 1E6)));
        mapView.getController().setZoom(18);

        mapView.setOnTouchListener(new View.OnTouchListener() {
            float x;
            float y;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (Math.abs(motionEvent.getX() - x) < 10 && Math.abs(motionEvent.getY() - y) < 10) {
                        while (mapView.getZoomLevel() != 18) {
                            if (mapView.getZoomLevel() < 18)
                                mapView.getController().zoomIn();
                            else
                                mapView.getController().zoomOut();
                        }
                        mapView.getController().animateTo(new GeoPoint(
                                (int) (Double.parseDouble(App.restaurant.getLatitude()) * 1E6),
                                (int) (Double.parseDouble(App.restaurant.getLongitude()) * 1E6)));
                        return true;
                    }
                }
                return false;
            }
        });

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

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker);
            canvas.drawBitmap(bitmap, screenPoint.x, screenPoint.y - bitmap.getHeight(), null);
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public void doNavigate() {
        String saddr = App.address.getThoroughfare() + " " + App.address.getSubThoroughfare() + ", " + App.address.getLocality();
        String daddr = App.restaurant.getAddress() + ", " + App.restaurant.getCity();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps" +
                        "?saddr=" + saddr.replace(" ", "+") +
                        "&daddr=" + daddr.replace(" ", "+")));
        startActivity(intent);
    }

    public void doOrder() {
        startActivity(new Intent(InfoActivity.this, OrderActivity.class));
    }

    public void goBack(View v) {
        finish();
    }
}
