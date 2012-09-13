package dk.pizzapp.android.util;

import dk.pizzapp.android.api.Response;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Response.Restaurant> {

    @Override
    public int compare(Response.Restaurant restaurant, Response.Restaurant restaurant1) {
        return Float.compare(restaurant.getDistance(), restaurant1.getDistance());
    }
}
