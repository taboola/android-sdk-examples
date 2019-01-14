package com.taboola.android.sdksamples.standard.tabs;

import android.support.annotation.NonNull;

import com.taboola.android.sdksamples.tabs.BaseTabFragment;
import com.taboola.android.sdksamples.tabs.BaseTaboolaFragment;
import com.taboola.android.sdksamples.tabs.FragmentsAdapter;

public class ViewPagerViaStandardFragment extends BaseTabFragment<BaseTaboolaFragment> {

    @Override
    protected void setupViewPagerAdapter(FragmentsAdapter<BaseTaboolaFragment> adapter) {
        super.setupViewPagerAdapter(adapter);
        String viewId = Long.toString(System.currentTimeMillis());
        adapter.addFragment(FeedInsideRecycleViewFragment.getInstance(viewId));
        adapter.addFragment(FeedInsideScrollViewFragment.getInstance(viewId));
    }

    @NonNull
    @Override
    protected String getTextForItem(int currentItem) {

        switch (currentItem) {
            case 0:
                return "FeedInsideRecycleView";
            case 1:
                return "FeedInsideScrollView";

            default:
                return super.getTextForItem(currentItem);
        }
    }
}
