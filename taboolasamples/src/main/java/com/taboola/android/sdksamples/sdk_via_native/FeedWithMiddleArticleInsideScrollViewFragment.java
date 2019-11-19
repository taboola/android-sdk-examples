package com.taboola.android.sdksamples.sdk_via_native;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;


public class FeedWithMiddleArticleInsideScrollViewFragment extends Fragment {

    private static final String TAG = "DEBUG";
    private static final String TABOOLA_VIEW_ID = String.valueOf(System.currentTimeMillis());
    private String mTaboolaType;


    public static FeedWithMiddleArticleInsideScrollViewFragment newInstance(String taboolaType) {
        FeedWithMiddleArticleInsideScrollViewFragment myFragment = new FeedWithMiddleArticleInsideScrollViewFragment();
        Bundle args = new Bundle();
        args.putString("taboolaType", taboolaType);
        myFragment.setArguments(args);
        return myFragment;
}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_standard, container, false);

        mTaboolaType = getArguments().getString("taboolaType");
        if (TextUtils.isEmpty(mTaboolaType)) {
            Log.e(TAG, "Error!!!!!!!! TaboolaType is null!");
            return view;
        }

        if (mTaboolaType.equals("widget")) {
            buildBelowArticleWidgetWidget(view.findViewById(R.id.taboola_widget_below_article));
        }
        else {
            buildBelowArticleWidgetFeed(view.findViewById(R.id.taboola_widget_below_article));
        }
        return view;
    }

    private void buildBelowArticleWidgetWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-rnd")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Widget with video")
                .setMode("alternating-widget-with-video")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID)
                .setInterceptScroll(false);

//        taboolaWidget.getLayoutParams().height = SdkDetailsHelper.getDisplayHeight(taboolaWidget.getContext()) * 2;
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setExtraProperties(optionalPageCommands);
        taboolaWidget.fetchContent();
    }

    private void buildBelowArticleWidgetFeed(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-rnd")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed with video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID)
                .setInterceptScroll(true);

        taboolaWidget.getLayoutParams().height = SdkDetailsHelper.getDisplayHeight(taboolaWidget.getContext()) * 2;
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setExtraProperties(optionalPageCommands);
        taboolaWidget.fetchContent();
    }

}
