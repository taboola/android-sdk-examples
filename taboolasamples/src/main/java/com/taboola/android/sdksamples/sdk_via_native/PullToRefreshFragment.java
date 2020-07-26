package com.taboola.android.sdksamples.sdk_via_native;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.listeners.TaboolaUpdateContentListener;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

public class PullToRefreshFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, TaboolaUpdateContentListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TaboolaWidget mTaboolaWidget;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pull_to_refresh, container, false);
        mTaboolaWidget = view.findViewById(R.id.taboola_feed);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        buildBelowArticleWidget(inflater.getContext());
        return view;
    }

    private void buildBelowArticleWidget(Context context) {
        mTaboolaWidget
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setInterceptScroll(true);

        int displayHeight = SdkDetailsHelper.getDisplayHeight(context);
        int height = displayHeight * 2;
        ViewGroup.LayoutParams params = mTaboolaWidget.getLayoutParams();
        if (params == null) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mTaboolaWidget.setLayoutParams(params);
        } else {
            params.height = height;
        }
        mTaboolaWidget.fetchContent();
    }

    @Override
    public void onRefresh() {
        mTaboolaWidget.updateContent(this); //Ask Taboola to update its content (refresh)
    }

    @Override
    public void onUpdateContentCompleted() {
        mSwipeRefreshLayout.setRefreshing(false); //When content updating is completed - we would like to stop the refreshing animation of SwipeRefreshLayout
    }
}
