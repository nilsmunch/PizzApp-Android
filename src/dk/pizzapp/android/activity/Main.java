package dk.pizzapp.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.model.Response;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main extends Activity {
    ArrayList<ToggleButton> tabs = new ArrayList<ToggleButton>(6);
    AQuery aQuery = new AQuery(this);
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initProgressDialog();
        initActionBar();
        initTabs();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
    }

    private void initActionBar() {
        ((TextView) findViewById(R.id.zipcode)).setText(((App) getApplication()).location.getPostalCode());
        ((TextView) findViewById(R.id.main_description)).setText(((App) getApplication()).location.getAddressLine(0));
    }

    private void initTabs() {
        tabs.add((ToggleButton) findViewById(R.id.main_tab_all));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_pizza));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_sushi));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_burger));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_pasta));
        tabs.add((ToggleButton) findViewById(R.id.main_tab_sandwich));

        for (ToggleButton tab : tabs) {
            tab.setOnClickListener(new TabClickListener());
        }

        tabs.get(0).setChecked(true);
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
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/zip/" + ((App) getApplication()).location.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 1)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/pizza/" + ((App) getApplication()).location.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 2)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/sushi/" + ((App) getApplication()).location.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 3)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/burger/" + ((App) getApplication()).location.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 4)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/pasta/" + ((App) getApplication()).location.getPostalCode(), JSONObject.class, new responseCallback());
            else if (tabs.indexOf(view) == 5)
                aQuery.progress(progressDialog).ajax("http://pizzapi.dk/desire/sandwich/" + ((App) getApplication()).location.getPostalCode(), JSONObject.class, new responseCallback());
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
                    parseResponse(objectMapper.readValue(object.toString(), Response.class));
                } catch (Exception e) {
                    showAlert("", e.getMessage());
                }
            }
        }
    }

    private void parseResponse(Response response) {
        showAlert("", response.getResult().get(response.getResult().keySet().iterator().next()).getName());
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
