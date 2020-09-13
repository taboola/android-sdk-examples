package com.taboola.android.sdksamples.sdk_via_native;


import android.os.Bundle;
import 	androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.globalNotifications.GlobalNotificationReceiver;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;


public class FeedWithMiddleArticleInsideScrollViewFragment extends Fragment implements GlobalNotificationReceiver.OnGlobalNotificationsListener {

    private static final String TAG = "DEBUG";
    private static final String TABOOLA_VIEW_ID = "123456";

    GlobalNotificationReceiver mGlobalNotificationReceiver = new GlobalNotificationReceiver();
    private TaboolaWidget mTaboolaWidgetBottom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlobalNotificationReceiver.registerNotificationsListener(this);
        mGlobalNotificationReceiver.registerReceiver(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_standard, container, false);
        buildMiddleArticleWidget(view.findViewById(R.id.taboola_widget_middle));
        mTaboolaWidgetBottom = view.findViewById(R.id.taboola_widget_below_article);
        return view;
    }

    private void buildMiddleArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Mid Article")
                .setMode("alternating-widget-without-video-1x1")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID); // setViewId - used in order to prevent duplicate recommendations between widgets on the same page view
        HashMap<String, String> extraProperties = new HashMap<>();
        extraProperties.put("useOnlineTemplate", "true");

        /*
            Adding this flag will require handling of collapsing Taboola on taboolaDidFailAd()
            in case Taboola fails to render (set to "true" by default):
            extraProperties.put("autoCollapseOnError", "false");
         */

        /*
            "detailedErrorCodes" set to "true" will stop the use of unorganized and unmaintained error reasons strings
            and will instead use detailed error codes (see taboolaDidFailAd() for more details)
         */
        extraProperties.put("detailedErrorCodes", "true");

        taboolaWidget.setExtraProperties(extraProperties);
        taboolaWidget.fetchContent();
    }

    private void buildBelowArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID)
                .setInterceptScroll(true);

        taboolaWidget.getLayoutParams().height = SdkDetailsHelper.getDisplayHeight(taboolaWidget.getContext()) * 2;
        HashMap<String, String> extraProperties = new HashMap<>();
        extraProperties.put("useOnlineTemplate", "true");

        /*
            Adding this flag will require handling of collapsing Taboola on taboolaDidFailAd()
            in case Taboola fails to render (set to "true" by default):
            extraProperties.put("autoCollapseOnError", "false");
         */

        /*
            "detailedErrorCodes" set to "true" will stop the use of unorganized and unmaintained error reasons strings
            and will instead use detailed error codes (see taboolaDidFailAd() for more details)
         */
        extraProperties.put("detailedErrorCodes", "true");

        taboolaWidget.setExtraProperties(extraProperties);
        taboolaWidget.fetchContent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGlobalNotificationReceiver.unregisterNotificationsListener();
        mGlobalNotificationReceiver.unregisterReceiver(getActivity());
    }

    @Override
    public void taboolaDidReceiveAd(TaboolaWidget taboolaWidget) {
        Log.d(TAG, "taboolaDidReceiveAd() called with: taboolaWidget = [" + taboolaWidget + "]");
        if (taboolaWidget.getId() == R.id.taboola_widget_middle) {
            buildBelowArticleWidget(mTaboolaWidgetBottom); //fetch content for the 2nd taboola asset only after completion of 1st item
        }
    }

    @Override
    public void taboolaViewResized(TaboolaWidget taboolaWidget, int height) {
        Log.d(TAG, "taboolaViewResized() called with: taboolaWidget = [" + taboolaWidget + "], height = [" + height + "]");
    }

    @Override
    public void taboolaItemDidClick(TaboolaWidget taboolaWidget) {
        Log.d(TAG, "taboolaItemDidClick() called with: taboolaWidget = [" + taboolaWidget + "]");
    }

    @Override
    public void taboolaDidFailAd(TaboolaWidget taboolaWidget, String reason) {
        Log.d(TAG, "taboolaDidFailAd() called with: taboolaWidget = [" + taboolaWidget + "], reason = [" + reason + "]");

        // If "detailedErrorCodes is set to "true":
        switch (reason) {
            case "NO_ITEMS":
                Log.d(TAG, "Taboola server returned a valid response, but without any items");
                break;
            case "TIMEOUT":
                Log.d(TAG, "no response from Taboola server after 10 seconds");
                break;
            case "WRONG_PARAMS":
                Log.d(TAG, "wrong Taboola mode");
                break;
            case "RESPONSE_ERROR":
                Log.d(TAG, "Taboola server is not reachable, or it returned a bad response");
                break;
            default:
                Log.d(TAG, "UNKNOWN_ERROR");
        }

        if (taboolaWidget.getId() == R.id.taboola_widget_middle) {
            buildBelowArticleWidget(mTaboolaWidgetBottom); //fetch content for the 2nd taboola asset only after completion of 1st item
        }
    }

}
