package com.taboola.android.sdksamples;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taboola.android.sdksamples.sdk_via_js.MidWidgetWithFeedJsFragment;
import com.taboola.android.sdksamples.sdk_via_js.SplitFeedJsFragment;
import com.taboola.android.sdksamples.sdk_via_js.ViewPagerViaJsFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedInsideRecyclerViewCustomFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedInsideRecyclerViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedWithMiddleArticleDarkModeInsideRecyclerViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedWithMiddleArticleInsideListViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedWithMiddleArticleInsideRecyclerViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedWithMiddleArticleInsideScrollViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.OCClickHandlerFragment;
import com.taboola.android.sdksamples.sdk_via_native.PullToRefreshFragment;
import com.taboola.android.sdksamples.sdk_via_native.RecyclerViewPreloadFragment;
import com.taboola.android.sdksamples.sdk_via_native.SimpleWidgetFragment;
import com.taboola.android.sdksamples.sdk_via_native.ViewPagerFragment;
import com.taboola.android.sdksamples.sdk_via_native.WidgetDynamicThemeChange;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = view.findViewById(R.id.main_menu_lyt);

        addHeader(getString(R.string.sdk_via_native), viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_lnr), R.id.std_mid_article_with_feed_lnr, viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_rv), R.id.std_mid_article_with_feed_rv, viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_rv_manual), R.id.std_mid_article_with_feed_rv_manual, viewGroup);
        addButton(getString(R.string.std_mid_article_preload), R.id.std_widget_preload, viewGroup);
        addButton(getString(R.string.std_view_pager), R.id.std_view_pager, viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_lv), R.id.std_mid_article_with_feed_lv, viewGroup);
        addButton(getString(R.string.std_widget_xml), R.id.std_widget_xml, viewGroup);
        addButton(getString(R.string.std_widget_oc_click), R.id.std_widget_oc_click, viewGroup);
        addButton(getString(R.string.std_feed_pull_to_refresh), R.id.std_feed_pull_to_refresh, viewGroup);
        addButton(getString(R.string.std_feed_lazy_loading_rv), R.id.std_feed_lazy_loading_rv, viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_dark_mode_rv), R.id.std_mid_article_with_feed_dark_mode_rv, viewGroup);

        addHeader(getString(R.string.sdk_via_js), viewGroup);
        addButton(getString(R.string.js_mid_article_with_feed), R.id.js_mid_article_with_feed, viewGroup);
        addButton(getString(R.string.js_split), R.id.js_split, viewGroup);
        addButton(getString(R.string.js_view_pager), R.id.js_view_pager, viewGroup);

        addHeader(getString(R.string.tbl_custom_tab), viewGroup);
        addButton(getString(R.string.tbl_custom_tab), R.id.tbl_custom_tab, viewGroup);

    }


    @Override
    public void onClick(View v) {

        String screenName = v.getTag().toString();
        Fragment fragmentToOpen = null;
        switch (v.getId()) {

            case R.id.js_split:
                fragmentToOpen = new SplitFeedJsFragment();
                break;

            case R.id.js_mid_article_with_feed:
                fragmentToOpen = new MidWidgetWithFeedJsFragment();
                break;

            case R.id.std_mid_article_with_feed_lnr:
                fragmentToOpen = new FeedWithMiddleArticleInsideScrollViewFragment();
                break;

            case R.id.js_view_pager:
                fragmentToOpen = new ViewPagerViaJsFragment();
                break;

            case R.id.std_view_pager:
                fragmentToOpen = new ViewPagerFragment();
                break;

            case R.id.std_mid_article_with_feed_lv:
                fragmentToOpen = new FeedWithMiddleArticleInsideListViewFragment();
                break;

            case R.id.std_mid_article_with_feed_rv:
                fragmentToOpen = new FeedWithMiddleArticleInsideRecyclerViewFragment();
                break;

            case R.id.std_mid_article_with_feed_rv_manual:
                fragmentToOpen = new FeedInsideRecyclerViewCustomFragment();
                break;

            case R.id.std_widget_xml:
                fragmentToOpen = new SimpleWidgetFragment();
                break;

            case R.id.std_widget_oc_click:
                fragmentToOpen = new OCClickHandlerFragment();
                break;

            case R.id.std_widget_preload:
                fragmentToOpen = new RecyclerViewPreloadFragment();
                break;

            case R.id.std_feed_pull_to_refresh:
                fragmentToOpen = new PullToRefreshFragment();
                break;

            case R.id.std_feed_lazy_loading_rv:
                fragmentToOpen = new FeedInsideRecyclerViewFragment();
                break;

            case R.id.std_mid_article_with_feed_dark_mode_rv:
                fragmentToOpen = new FeedWithMiddleArticleDarkModeInsideRecyclerViewFragment();
                break;

            case R.id.tbl_custom_tab:
                String url = "https://www.thestartmagazine.com/feed/summary?&publisherId=Ops_Testing&key=0QzhwJKBfOxouyYgW3woxhwv04inkqWH&countryCode=US&category=Finance&vendor=Reuters,Bloomberg&language=en";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getContext(), Uri.parse(url));
                break;

        }

        if (fragmentToOpen != null) {
            openFragment(fragmentToOpen, screenName);
        }
    }

    private void openFragment(Fragment fragment, String screenName) {
        if (mListener != null) {
            mListener.onMenuItemClicked(fragment, screenName);
        }
    }

    private void addHeader(String title, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.menu_header_item,
                viewGroup, false);
        textView.setText(title);
        viewGroup.addView(textView, viewGroup.getChildCount() - 1);
    }

    private void addButton(String screenName, int id, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false);
        textView.setText(screenName);
        textView.setTag(screenName);
        textView.setId(id);
        textView.setOnClickListener(this);

        viewGroup.addView(textView, viewGroup.getChildCount() - 1);
    }

    public interface OnFragmentInteractionListener {
        void onMenuItemClicked(Fragment fragment, String screenName);
    }
}