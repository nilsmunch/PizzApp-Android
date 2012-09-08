package dk.pizzapp.android.util;

import dk.pizzapp.android.data.Restaurant;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Restaurant> {

    // Compares the distance between restaurants
    @Override
    public int compare(Restaurant restaurant, Restaurant restaurant1) {
        return Float.compare(restaurant.getDistance(), restaurant1.getDistance());
    }
}
