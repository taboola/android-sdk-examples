package com.taboola.android.sdksamples.sdk_via_native;


import android.content.Context;
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
    private TaboolaWidget mTaboolaWidget;
    private boolean mShouldFetch;
    private boolean isTaboolaFetched = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_feed_inside_sv, container, false);
        mTaboolaWidget = view.findViewById(R.id.taboola_widget_below_article);
        buildBelowArticleWidget(inflater.getContext());
        return view;
    }

    @Override
    public void onPageSelected() {
        // In ScrollView the widget will need to be rendered only when page is selected, no need to fetch if user didn't see taboola view
        // this is the most common use for view pager and you should follow this example
        // unless you use RecycleView, then you need to follow FeedInsideRecycleViewFragment example
        if (!isTaboolaFetched) {
            mShouldFetch = true;
            if (mTaboolaWidget != null) {
                fetchContent();
            }
        }
    }

    private void buildBelowArticleWidget(Context context) {

        mTaboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setInterceptScroll(true);

        //optional
        if (!TextUtils.isEmpty(mViewId)) {
            mTaboolaWidget.setViewId(mViewId);
        }

        //used for enable horizontal scroll
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("enableHorizontalScroll", "true");
        mTaboolaWidget.setOptionalPageCommands(optionalPageCommands);

        mTaboolaWidget.getLayoutParams().height = SdkDetailsHelper.getDisplayHeight(context);

        fetchContent();
    }

    private void fetchContent() {
        if (mShouldFetch) {
            mShouldFetch = false;
            mTaboolaWidget.fetchContent();
            isTaboolaFetched = true;
        }
    }

    public static FeedInsideScrollViewFragment getInstance(String viewId) {
        FeedInsideScrollViewFragment baseTaboolaFragment = new FeedInsideScrollViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIEW_ID, viewId);
        baseTaboolaFragment.setArguments(bundle);
        return baseTaboolaFragment;
    }

}
