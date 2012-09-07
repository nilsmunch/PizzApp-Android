package dk.pizzapp.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.model.Restaurant;

public class Order extends Activity {
    private ProgressBar progressBar;
    private Restaurant restaurant;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        restaurant = ((App) getApplication()).restaurants.get(getIntent().getIntExtra("id", 0));
        ((TextView) findViewById(R.id.order_name)).setText(restaurant.getName());

        progressBar = (ProgressBar) findViewById(R.id.order_progress);

        webView = (WebView) findViewById(R.id.order_webview);
        webView.setWebViewClient(new WebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://pizzapi.dk/go/" + restaurant.getId());
    }

    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
