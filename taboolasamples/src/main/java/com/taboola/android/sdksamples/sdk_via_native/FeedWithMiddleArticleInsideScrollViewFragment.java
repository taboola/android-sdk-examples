package com.taboola.android.sdksamples.sdk_via_native;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

    GlobalNotificationReceiver mGlobalNotificationReceiver = new GlobalNotificationReceiver();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_standard, container, false);
        buildMiddleArticleWidget(view.findViewById(R.id.taboola_widget_middle));
        buildBelowArticleWidget(view.findViewById(R.id.taboola_widget_below_article));
        return view;
    }

    private void buildMiddleArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Mid Article")
                .setMode("alternating-widget-without-video-1-on-1")
                .setTargetType("mix");
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setOptionalPageCommands(optionalPageCommands);
        taboolaWidget.fetchContent();
    }

    private void buildBelowArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setInterceptScroll(true);

        taboolaWidget.getLayoutParams().height = SdkDetailsHelper.getDisplayHeight(taboolaWidget.getContext());
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setOptionalPageCommands(optionalPageCommands);
        taboolaWidget.fetchContent();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGlobalNotificationReceiver.registerNotificationsListener(this);
        mGlobalNotificationReceiver.registerReceiver(getActivity());
    }


    @Override
    public void onPause() {
        super.onPause();
        mGlobalNotificationReceiver.unregisterNotificationsListener();
        mGlobalNotificationReceiver.unregisterReceiver(getActivity());
    }

    @Override
    public void taboolaDidReceiveAd(TaboolaWidget taboolaWidget) {
        Log.d(TAG, "taboolaDidReceiveAd() called with: taboolaWidget = [" + taboolaWidget + "]");
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
    }

}
