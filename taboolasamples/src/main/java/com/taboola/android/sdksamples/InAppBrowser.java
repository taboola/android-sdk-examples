package com.taboola.android.sdksamples;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InAppBrowser extends Fragment {

    private static final String TAG = "InAppBrowser";
    private static final String URL_PARAM = TAG + ".url";
    private android.support.v4.widget.ContentLoadingProgressBar mProgressBar;
    private String mUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(URL_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.taboola.android.R.layout.dialog_web, container, false);
        mProgressBar = rootView.findViewById(com.taboola.android.R.id.web_dialog_progress_bar);
        rootView.findViewById(R.id.web_dialog_dismiss_button).setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final WebView webView = view.findViewById(com.taboola.android.R.id.web_dialog_web_view);
        webView.post(() -> {
            initWebView(webView);
            webView.loadUrl(mUrl);
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(WebView webView) {
        final WebSettings settings = webView.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        CookieManager.getInstance().setAcceptCookie(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mProgressBar != null) {
                    mProgressBar.hide();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        mProgressBar = null;
        super.onDetach();
    }

    public static Fragment getInstance(String url) {
        Fragment f = new InAppBrowser();
        Bundle args = new Bundle();
        args.putString(URL_PARAM, url);
        f.setArguments(args);
        return f;
    }
}
