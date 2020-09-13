package com.taboola.android.sdksamples.sdk_via_js;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import 	androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.taboola.android.js.TaboolaJs;
import com.taboola.android.sdksamples.tabs.BaseTabFragment;
import com.taboola.android.sdksamples.tabs.BaseTaboolaFragment;
import com.taboola.android.sdksamples.tabs.FragmentsAdapter;
import com.taboola.android.utils.AssetUtil;

public class ViewPagerViaJsFragment extends BaseTabFragment<ViewPagerViaJsFragment.SampleJsTaboolaFragment> {

    @Override
    protected void setupViewPagerAdapter(FragmentsAdapter<SampleJsTaboolaFragment> adapter) {
        super.setupViewPagerAdapter(adapter);
        adapter.addFragment(new SampleJsTaboolaFragment());
        adapter.addFragment(new SampleJsTaboolaFragment());
    }

    public static class SampleJsTaboolaFragment extends BaseTaboolaFragment {

        private static final String TAG = "SampleJsTaboolaFragment";
        private static final String HTML_CONTENT_FILE = "sampleContentPagePrefetch.html";
        private static final String BASE_URL = "https://example.com";

        private WebView mWebView;

        @Override
        public void onPageSelected() {
            TaboolaJs.getInstance().fetchContent(mWebView);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final Context context = inflater.getContext();
            mWebView = new WebView(context);
            TaboolaJs.getInstance().registerWebView(mWebView);
            initWebViewSettings(mWebView);
            loadHtml();
            return mWebView;
        }

        private void loadHtml() {

            //publisher should load its url here instead
            String htmlContent = null;
            try {
                htmlContent = AssetUtil.getHtmlTemplateFileContent(getContext(), HTML_CONTENT_FILE);
            } catch (Exception e) {
                Log.e(TAG, "Failed to read asset file: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
            mWebView.loadDataWithBaseURL(BASE_URL, htmlContent, "text/html", "UTF-8", "");
        }


        @Override
        public void onDetach() {
            TaboolaJs.getInstance().unregisterWebView(mWebView);
            super.onDetach();
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

    }


}
