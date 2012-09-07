package dk.pizzapp.android;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import dk.pizzapp.android.model.Restaurant;

import java.util.ArrayList;

public class App extends Application {
    public Location location = null;
    public Address address = null;
    public ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    public ArrayList<Restaurant> visibleRestaurants = new ArrayList<Restaurant>();

    @Override
    public void onCreate() {

    }
}
