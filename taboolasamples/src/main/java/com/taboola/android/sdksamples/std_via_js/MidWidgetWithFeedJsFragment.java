package com.taboola.android.sdksamples.std_via_js;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.taboola.android.js.OnRenderListener;
import com.taboola.android.js.OnResizeListener;
import com.taboola.android.js.TaboolaJs;
import com.taboola.android.utils.AssetUtil;

/**
 * The initialization of TaboolaJS is done in the Application class
 * If your project does not have an Application extending class, create one and then init TaboolaJs.
 */

public class MidWidgetWithFeedJsFragment extends Fragment implements OnRenderListener, OnResizeListener {

    private static final String TAG = "DEBUG";
    private static final String HTML_CONTENT_FILE_TITLE = "sampleContentPage.html";
    private static final String BASE_URL = "http://example.com";


    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = inflater.getContext();
        mWebView = new WebView(context);
        TaboolaJs.getInstance().registerWebView(mWebView);
        TaboolaJs.getInstance().setOnRenderListener(mWebView, new );
        TaboolaJs.getInstance().setOnResizeListener(mWebView, new OnResizeListener() {
            @Override
            public void onResize(WebView webView, String s, int i) {

            }
        });
        initWebViewSettings(mWebView);
        loadHtml();
        return mWebView;
    }


    private static void initWebViewSettings(WebView webView) {
        final WebSettings settings = webView.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    public void onDetach() {
        TaboolaJs.getInstance().setOnRenderListener(mWebView, null);
        TaboolaJs.getInstance().setOnResizeListener(mWebView, null);
        TaboolaJs.getInstance().unregisterWebView(mWebView);
        super.onDetach();
    }


    private void loadHtml() {

        //publisher should load its url here instead
        String htmlContent = null;
        try {
            htmlContent = AssetUtil.getHtmlTemplateFileContent(getContext(), HTML_CONTENT_FILE_TITLE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read asset file: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        mWebView.loadDataWithBaseURL(BASE_URL, htmlContent, "text/html", "UTF-8", "");
    }

    @Override
    public void onRenderSuccessful(WebView webView, String placementName, int height) {
        Log.d(TAG, "onRenderSuccessful() called with: placementName = [" + placementName + "], height = [" + height + "]");
    }

    @Override
    public void onRenderFailed(WebView webView, String placementName, String errorMessage) {
        Log.d(TAG, "onRenderFailed() called with: placementName = [" + placementName + "], errorMessage = [" + errorMessage + "]");
    }

    @Override
    public void onResize(WebView webView, String placementName, int height) {
        Log.d(TAG, "onResize() called with: placementName = [" + placementName + "], height = [" + height + "]");
    }
}
