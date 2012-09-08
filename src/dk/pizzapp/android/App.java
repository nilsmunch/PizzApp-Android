package dk.pizzapp.android;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import dk.pizzapp.android.model.Restaurant;

import java.util.ArrayList;

public class App extends Application {
    public static Location location = null;
    public static Address address = null;
    public static ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    public static ArrayList<Restaurant> visibleRestaurants = new ArrayList<Restaurant>();
    public static Restaurant restaurant = null;

    @Override
    public void onCreate() {
        restaurants = new ArrayList<Restaurant>();
        visibleRestaurants = new ArrayList<Restaurant>();
    }
}
