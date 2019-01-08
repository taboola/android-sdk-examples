package com.taboola.android.sdksamples.js;

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

import com.taboola.android.js.TaboolaJs;
import com.taboola.android.utils.AssetUtil;

public class SplitFeedJsFragment extends Fragment {

    private static final String TAG = "MidWidgetWithFeedJsFrag";
    private static final String HTML_CONTENT_FILE_TITLE = "sampleContentPageSplitFeed.html";
    private static final String BASE_URL = "http://example.com";


    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = inflater.getContext();
        mWebView = new WebView(context);
        initWebViewSettings(mWebView);
        initTaboolaJs(mWebView);
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

    private void initTaboolaJs(WebView webView) {
        TaboolaJs.getInstance()
                .init(webView.getContext().getApplicationContext())
                .registerWebView(mWebView);
    }

    @Override
    public void onDetach() {
        TaboolaJs.getInstance().unregisterWebView(mWebView);
        super.onDetach();
    }



    private void loadHtml() {

        //publisher should load it's url here instead
        String htmlContent = null;
        try {
            htmlContent = AssetUtil.getHtmlTemplateFileContent(getContext(), HTML_CONTENT_FILE_TITLE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read asset file: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        mWebView.loadDataWithBaseURL(BASE_URL, htmlContent, "text/html", "UTF-8", "");
    }
}
