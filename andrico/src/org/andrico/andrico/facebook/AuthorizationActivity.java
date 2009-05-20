/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/

package org.andrico.andrico.facebook;

import org.andrico.andrico.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

/**
 * An activity that will connect to facebook, have the user log in and return to
 * the caller a string representing session information. It expects to receive
 * an Intent with the extras "api_key" and "api_secret" as provided by facebook.
 */
abstract class AuthorizationActivity extends Activity {
    private class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            AuthorizationActivity.this.setProgress(newProgress * 100);
        }
    }
   
    /**
     * Capture a web session and close it as soon as a user has successfully
     * logged in.
     */
    
    private class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(LOG, "onPageStarted: " + url);
            AuthorizationActivity.this.onPageStarted(url);   
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(LOG, "onPageStarted: " + url);
            if ((url.indexOf("http://www.facebook.com/login.php") > -1 ))
            {
            	//if (AuthorizationActivity.this.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            	//{
            	mWebView.scrollTo(190, 230);
            	//}
            	((WebView)findViewById(R.id.web_view)).zoomIn();
            }
            AuthorizationActivity.this.onPageFinished(url);
        }
    }
    
    // ----------------------------------------------------------------------------
    
    
    private static final String LOG = "AuthorizationActivity";

    private Uri mLastUrl = null;
    private WebView mWebView;

    public void loadUri(Uri url) {
        Log.d(LOG, "loadUrl: " + url);
        mLastUrl = url;
        mWebView.loadUrl(url.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        Log.e(LOG, "onCreate");

        CookieSyncManager.createInstance(this);

        setContentView(R.layout.facebook_auth_activity);
        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    abstract public void onPageStarted(String url);
    abstract public void onPageFinished(String url);

    @Override
    protected void onStart() {
        super.onStart();
        if (mLastUrl != null) {
            mWebView.loadUrl(mLastUrl.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }
}
