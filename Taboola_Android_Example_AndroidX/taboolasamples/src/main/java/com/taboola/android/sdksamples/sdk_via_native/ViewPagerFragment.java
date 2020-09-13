package com.taboola.android.sdksamples.sdk_via_native;

import 	androidx.annotation.NonNull;

import com.taboola.android.sdksamples.tabs.BaseTabFragment;
import com.taboola.android.sdksamples.tabs.BaseTaboolaFragment;
import com.taboola.android.sdksamples.tabs.FragmentsAdapter;

public class ViewPagerFragment extends BaseTabFragment<BaseTaboolaFragment> {

    @Override
    protected void setupViewPagerAdapter(FragmentsAdapter<BaseTaboolaFragment> adapter) {
        super.setupViewPagerAdapter(adapter);
        String viewId = Long.toString(System.currentTimeMillis());
        adapter.addFragment(FeedInsideRecyclerViewFragment.getInstance(viewId));
        adapter.addFragment(FeedInsideScrollViewFragment.getInstance(viewId));
    }

    @NonNull
    @Override
    protected String getTextForItem(int currentItem) {

        switch (currentItem) {
            case 0:
                return "FeedInsideRecyclerView";
            case 1:
                return "FeedInsideScrollView";

            default:
                return super.getTextForItem(currentItem);
        }
    }
}
