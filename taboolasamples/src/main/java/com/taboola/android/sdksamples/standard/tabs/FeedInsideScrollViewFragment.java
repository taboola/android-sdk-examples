package com.taboola.android.sdksamples.standard.tabs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.tabs.BaseTaboolaFragment;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;


public class FeedInsideScrollViewFragment extends BaseTaboolaFragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_feed_inside_sv, container, false);
        buildBelowArticleWidget(view.findViewById(R.id.taboola_widget_below_article));
        return view;
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

        //optional
        if (!TextUtils.isEmpty(mViewId)) {
            taboolaWidget.setViewId(mViewId);
        }

        //used for enable horizontal scroll
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("enableHorizontalScroll", "true");
        taboolaWidget.setOptionalPageCommands(optionalPageCommands);

        taboolaWidget.getLayoutParams().height = SdkDetailsHelper.getDisplayHeight(taboolaWidget.getContext());
        taboolaWidget.fetchContent();
    }

    public static FeedInsideScrollViewFragment getInstance(String viewId) {
        FeedInsideScrollViewFragment baseTaboolaFragment = new FeedInsideScrollViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIEW_ID, viewId);
        baseTaboolaFragment.setArguments(bundle);
        return baseTaboolaFragment;
    }

}
