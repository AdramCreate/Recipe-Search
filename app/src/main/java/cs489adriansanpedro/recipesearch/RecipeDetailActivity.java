package cs489adriansanpedro.recipesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RecipeDetailActivity extends AppCompatActivity{
    //MARK: Variables
    private WebView mWebView;

    //Mark: Functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        String title = this.getIntent().getExtras().getString("title");
        String url = this.getIntent().getExtras().getString("url");

        setTitle(title);

        mWebView = (WebView) findViewById(R.id.detail_web_view);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        mWebView.loadUrl(url);
    }
}
