package eu.restio.designernews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BrowserActivity extends ActionBarActivity {
    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        setToolbarUI();
        setWebViewAndNavigate();
    }

    private void setWebViewAndNavigate() {
        Bundle b = getIntent().getExtras();
        String url = b.getString("url","");
        if(url.equals("")){
            finish(); // Close activity, there is no point to show blank screen
        }else {
            webView = (WebView) findViewById(R.id.webView);

            WebSettings webSettings = webView.getSettings();
            webSettings.setBuiltInZoomControls(false);
            webSettings.setJavaScriptEnabled(true);

            webView.setWebViewClient(new wvCallback());

            webView.loadUrl(url);
            getSupportActionBar().setTitle(url);
        }
    }

    class wvCallback extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false); // prevent loading in other apps, true if you check for other apps
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            getSupportActionBar().setTitle(view.getTitle());
        }
    }
    private void setToolbarUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
