package dk.pizzapp.android.api;

import android.content.Context;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;

import java.util.HashMap;
import java.util.Map;

public class PizzaService {
    private static PizzaService instance;
    private AQuery androidQuery;

    private PizzaService(Context context) {
        androidQuery = new AQuery(context);
    }

    public static PizzaService getInstance(Context context) {
        if (instance == null)
            instance = new PizzaService(context);
        return instance;
    }

    public void getRestaurants(String postalCode, AjaxCallback<Response> callback) {
        androidQuery.ajax("http://pizzapi.dk/zip/" + postalCode, Response.class, callback);
    }

    public void registerDevice(final String uuid, final String url, final String type) {
        Map<String, Object> params = new HashMap<String, Object>() {
            {
                put("uuid", uuid);
                put("url", url);
                put("type", type);
            }
        };
        androidQuery.ajax("http://affiliate.pizzapp.dk/register.php", params, String.class, new AjaxCallback<String>());
    }
}
