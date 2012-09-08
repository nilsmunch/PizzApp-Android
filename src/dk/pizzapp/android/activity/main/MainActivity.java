package dk.pizzapp.android.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.data.Response;
import dk.pizzapp.android.data.Restaurant;
import dk.pizzapp.android.util.DistanceComparator;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MainActivity extends Activity {
    private ArrayList<ToggleButton> tabs = new ArrayList<ToggleButton>();
    private MainListAdapter listAdapter;
    private AQuery aQuery = new AQuery(this);
    private ProgressDialog progressDialog;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initProgressDialog();
        initTabs();
        initList();
    }

    private void initActionBar() {
        ((TextView) findViewById(R.id.zipcode)).setText(App.address.getPostalCode());
        ((TextView) findViewById(R.id.main_description)).setText(App.address.getAddressLine(0));
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
    }

    private void initTabs() {

        // Find all views and add them to tab-list
        tabs.add((ToggleButton) findViewById(R.id.main_tab_all));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_pizza));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_sushi));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_burger));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_pasta));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_sandwich));

        // Set their onClick listener
        for (ToggleButton tab : tabs) {
            tab.setOnClickListener(new TabClickListener());
        }

        // Check the first tab, and load initial results
        tabs.get(0).setChecked(true);
        aQuery.progress(progressDialog).ajax("http://pizzapi.dk/zip/" + App.address.getPostalCode(), JSONObject.class, new responseCallback());
    }

    private void initList() {
        list = (ListView) findViewById(R.id.main_list);
        listAdapter = new MainListAdapter(this);
        aQuery.id(list).scrolled(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        aQuery.id(list).adapter(listAdapter);
    }

    private class TabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            // Toggle all tabs to off, and set clicked one to on.
            for (ToggleButton tab : tabs)
                tab.setChecked(false);
            ((ToggleButton) view).setChecked(true);

            // Filter the visible results according to the tag
            App.visibleRestaurants.clear();
            String tag = ((ToggleButton) view).getTextOn().toString();
            if (tag.equalsIgnoreCase("all"))
                App.visibleRestaurants.addAll(App.restaurants);
            else
                for (Restaurant restaurant : App.restaurants) {
                    if (restaurant.getKeys().contains(tag.toLowerCase()))
                        App.visibleRestaurants.add(restaurant);
                }
            listAdapter.notifyDataSetChanged();
            list.setSelectionAfterHeaderView();
        }
    }

    private class responseCallback extends AjaxCallback<JSONObject> {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            if (object == null)
                showAlert("", status.getMessage());
            else {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    handleResponse(objectMapper.readValue(object.toString(), Response.class));
                } catch (Exception e) {
                    showAlert("", e.getMessage());
                }
            }
        }
    }

    private void handleResponse(Response response) {
        for (Map.Entry<String, Restaurant> entry : response.getResult().entrySet()) {

            // Set the id for the restaurant
            entry.getValue().setId(entry.getKey());

            // Calculate and set distance from current location
            float[] distances = new float[3];
            Location.distanceBetween(
                    App.location.getLatitude(), App.location.getLongitude(),
                    Double.parseDouble(entry.getValue().getLatitude()), Double.parseDouble(entry.getValue().getLongitude()),
                    distances);
            entry.getValue().setDistance(distances[0]);

        }

        // Clear existing results and add new ones
        App.restaurants.clear();
        App.restaurants.addAll(response.getResult().values());

        // Sort the results according to distance
        Collections.sort(App.restaurants, new DistanceComparator());

        // Update the list with the new results
        App.visibleRestaurants.addAll(App.restaurants);
        listAdapter.notifyDataSetChanged();
        list.setSelectionAfterHeaderView();
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
