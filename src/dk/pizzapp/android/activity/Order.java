package dk.pizzapp.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.model.Restaurant;

public class Order extends Activity {
    private WebView webView;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        restaurant = ((App) getApplication()).restaurants.get(getIntent().getIntExtra("id", 0));
        ((TextView) findViewById(R.id.order_name)).setText(restaurant.getName());

        webView = (WebView) findViewById(R.id.order_webview);
        webView.setWebViewClient(new WebClient());
        webView.loadUrl("http://pizzapi.dk/go/" + restaurant.getId());
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
