package dk.pizzapp.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;

public class Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        TextView name = (TextView) findViewById(R.id.info_name);
        name.setText(((App) getApplication()).restaurants.get(getIntent().getIntExtra("id", 0)).getName());
    }
}
