package dk.pizzapp.android;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import com.androidquery.callback.AjaxCallback;
import dk.pizzapp.android.api.JsonTransformer;
import dk.pizzapp.android.api.Response;

import java.util.ArrayList;

public class App extends Application {
    public static Location location = null;
    public static Address address = null;
    public static ArrayList<Response.Restaurant> restaurants = new ArrayList<Response.Restaurant>();
    public static ArrayList<Response.Restaurant> visibleRestaurants = new ArrayList<Response.Restaurant>();
    public static Response.Restaurant restaurant = null;

    @Override
    public void onCreate() {
        JsonTransformer transformer = new JsonTransformer();
        AjaxCallback.setTransformer(transformer);
    }
}
