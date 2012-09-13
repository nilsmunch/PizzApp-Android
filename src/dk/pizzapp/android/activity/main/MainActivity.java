package dk.pizzapp.android.activity.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.api.PizzaService;
import dk.pizzapp.android.api.Response;
import dk.pizzapp.android.util.DistanceComparator;

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
        loadResults();
    }

    private void initActionBar() {
        ((TextView) findViewById(R.id.zipcode)).setText(App.address.getSubLocality());

        Spannable span1 = new SpannableString("Åbne");
        Spannable span2 = new SpannableString(" restauranter der i øjeblikket kan levere mad til ");
        Spannable span3 = new SpannableString(App.address.getThoroughfare());
        Spannable span4 = new SpannableString(" og omegn.");

        span1.setSpan(new ForegroundColorSpan(Color.parseColor("#ea4d22")), 0, span1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span3.setSpan(new ForegroundColorSpan(Color.parseColor("#ea4d22")), 0, span3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, span1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, span3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) findViewById(R.id.main_description)).setText(TextUtils.concat(span1, span2, span3, span4));
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

        // Select first tab
        tabs.get(0).setChecked(true);
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

    private void loadResults() {
        PizzaService.getInstance(getApplication()).getRestaurants(App.address.getPostalCode(), new responseCallback());
    }

    private class responseCallback extends AjaxCallback<Response> {
        @Override
        public void callback(String url, Response response, AjaxStatus status) {
            for (Map.Entry<String, Response.Restaurant> entry : response.getResult().entrySet()) {
                entry.getValue().setId(entry.getKey());
                float[] distances = new float[3];
                Location.distanceBetween(
                        App.location.getLatitude(), App.location.getLongitude(),
                        Double.parseDouble(entry.getValue().getLatitude()), Double.parseDouble(entry.getValue().getLongitude()),
                        distances);
                entry.getValue().setDistance(distances[0]);
            }
            App.restaurants.clear();
            App.restaurants.addAll(response.getResult().values());
            Collections.sort(App.restaurants, new DistanceComparator());
            App.visibleRestaurants.addAll(App.restaurants);
            listAdapter.notifyDataSetChanged();
            list.setSelectionAfterHeaderView();
        }
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
                for (Response.Restaurant restaurant : App.restaurants) {
                    if (restaurant.getTags().contains(tag.toLowerCase()))
                        App.visibleRestaurants.add(restaurant);
                }
            listAdapter.notifyDataSetChanged();
            list.setSelectionAfterHeaderView();
        }
    }
}
