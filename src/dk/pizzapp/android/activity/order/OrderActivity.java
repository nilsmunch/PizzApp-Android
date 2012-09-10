package dk.pizzapp.android.activity.order;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;

public class OrderActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ((TextView) findViewById(R.id.order_name)).setText(App.restaurant.getName());

        webView = (WebView) findViewById(R.id.order_webview);
        webView.setWebViewClient(new WebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://pizzapi.dk/go/" + App.restaurant.getId());
    }

    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // Load the requested URL in our webview
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            Toast.makeText(OrderActivity.this, url, Toast.LENGTH_SHORT).show();

            // Return to info page if '/area' is in the URL
            if (url.contains("/area")) {
                finish();
            }
        }
    }
}
