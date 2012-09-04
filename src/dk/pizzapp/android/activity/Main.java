package dk.pizzapp.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.model.Response;
import dk.pizzapp.android.model.Restaurant;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class Main extends Activity {
    private ArrayList<ToggleButton> tabs = new ArrayList<ToggleButton>(6);
    private AQuery aQuery = new AQuery(this);
    private ProgressDialog progressDialog;
    private ListView list;
    private MainListAdapter arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initProgressDialog();
        initActionBar();
        initTabs();
        initList();
    }

    private void initList() {
        arrayAdapter = new MainListAdapter();
        list = (ListView) findViewById(R.id.main_list);
        aQuery.id(list).scrolled(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
        aQuery.id(list).adapter(arrayAdapter);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
    }

    private void initActionBar() {
        ((TextView) findViewById(R.id.zipcode)).setText(((App) getApplication()).address.getPostalCode());
        ((TextView) findViewById(R.id.main_description)).setText(((App) getApplication()).address.getAddressLine(0));
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

        // Check the first tab, and load "all" results
        tabs.get(0).setChecked(true);
        aQuery.progress(progressDialog).ajax("http://pizzapi.dk/zip/" + ((App) getApplication()).address.getPostalCode(), JSONObject.class, new responseCallback());
    }

    private class TabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            // Toggle all tabs to off, and set clicked one to on.
            for (ToggleButton tab : tabs)
                tab.setChecked(false);
            ((ToggleButton) view).setChecked(true);

            // Call the webservice
            if (tabs.indexOf(view) == 0)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/zip/" + ((App) getApplication()).address.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 1)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/pizza/" + ((App) getApplication()).address.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 2)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/sushi/" + ((App) getApplication()).address.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 3)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/burger/" + ((App) getApplication()).address.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 4)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/pasta/" + ((App) getApplication()).address.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 5)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/sandwich/" + ((App) getApplication()).address.getPostalCode(), JSONObject.class, new responseCallback());
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
                    ((App) getApplication()).location.getLatitude(), ((App) getApplication()).location.getLongitude(),
                    Double.parseDouble(entry.getValue().getLatitude()), Double.parseDouble(entry.getValue().getLongitude()),
                    distances);
            entry.getValue().setDistance(distances[0]);

        }

        // Clear existing results and add new ones
        ((App) getApplication()).restaurants.clear();
        ((App) getApplication()).restaurants.addAll(response.getResult().values());

        // Sort the results according to distance
        Collections.sort(((App) getApplication()).restaurants, new DistanceComparator());

        // Update the list with the new results
        arrayAdapter.notifyDataSetChanged();
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

    private class MainListAdapter extends ArrayAdapter<Response> {

        public MainListAdapter() {
            super(Main.this, R.layout.main_list_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.main_list_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.list_item_icon);
                holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                holder.address = (TextView) convertView.findViewById(R.id.list_item_address);
                holder.distance = (TextView) convertView.findViewById(R.id.list_item_distance);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Restaurant restaurant = ((App) getApplication()).restaurants.get(position);

            AQuery aq = new AQuery(convertView);
            holder.id = position;
            aq.id(holder.name).text(restaurant.getName());
            aq.id(holder.address).text(restaurant.getAddress());
            aq.id(holder.icon).image("http://pizzapi.dk/display/" + restaurant.getId(), true, false, 0, R.drawable.icon, aq.getCachedImage(R.drawable.icon), AQuery.FADE_IN_NETWORK);

            if ((int) restaurant.getDistance() < 1000)
                aq.id(holder.distance).text((int) restaurant.getDistance() + " m");
            else {
                aq.id(holder.distance).text((int) restaurant.getDistance() / 1000 + " km");
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewHolder viewHolder = (ViewHolder) view.getTag();
                    startActivity(new Intent(Main.this, Info.class).putExtra("id", viewHolder.id));
                }
            });

            return convertView;
        }

        @Override
        public int getCount() {
            return ((App) getApplication()).restaurants.size();
        }
    }

    static class ViewHolder {
        int id;
        ImageView icon;
        TextView name;
        TextView address;
        TextView distance;
    }

    private class DistanceComparator implements Comparator<Restaurant> {

        @Override
        public int compare(Restaurant restaurant, Restaurant restaurant1) {
            return Float.compare(restaurant.getDistance(), restaurant1.getDistance());
        }
    }
}
