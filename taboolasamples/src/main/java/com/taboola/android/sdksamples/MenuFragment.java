package com.taboola.android.sdksamples;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taboola.android.sdksamples.sdk_via_native.FeedInsideRecycleViewCustomFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedWithMiddleArticleInsideListViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedWithMiddleArticleInsideRecycleViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.FeedWithMiddleArticleInsideScrollViewFragment;
import com.taboola.android.sdksamples.sdk_via_native.ViewPagerFragment;
import com.taboola.android.sdksamples.sdk_via_native.WidgetDynamicThemeChange;
import com.taboola.android.sdksamples.std_via_js.MidWidgetWithFeedJsFragment;
import com.taboola.android.sdksamples.std_via_js.SplitFeedJsFragment;
import com.taboola.android.sdksamples.std_via_js.ViewPagerViaJsFragment;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private ViewGroup mViewGroup;

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
        mViewGroup = view.findViewById(R.id.main_menu_lyt);


        addHeader("SDK VIA NATIVE");
        addButton("Widget + Feed (ScrollView)", R.id.std_mid_article_with_feed_lnr);
        addButton("Widget + Feed (RecyclerView)", R.id.std_mid_article_with_feed_rv);
        addButton("Widget + Feed (RecyclerView) Manual Scroll Switch", R.id.std_mid_article_with_feed_rv_manual);
        addButton("ViewPager/Horizontal Scroll", R.id.std_view_pager);
        addButton("Widget + Feed (ListView)", R.id.std_mid_article_with_feed_lv);
        addButton("Widget dynamic theme change (RecyclerView)", R.id.std_widget_dynamic_theme);

        addHeader("SDK VIA JS");
        addButton("Widget + Feed ", R.id.js_mid_article_with_feed);
        addButton("Split Feed", R.id.js_split);
        addButton("View Pager", R.id.js_view_pager);

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
                fragmentToOpen = new FeedWithMiddleArticleInsideRecycleViewFragment();
                break;

            case R.id.std_mid_article_with_feed_rv_manual:
                fragmentToOpen = new FeedInsideRecycleViewCustomFragment();
                break;
            case R.id.std_widget_dynamic_theme:
                fragmentToOpen = new WidgetDynamicThemeChange();
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

    private void addHeader(String title) {
        Context context = mViewGroup.getContext();
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.menu_header_item,
                mViewGroup, false);
        textView.setText(title);
        mViewGroup.addView(textView, mViewGroup.getChildCount() - 1);
    }

    private void addButton(String screenName, int id) {
        Context context = mViewGroup.getContext();
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.button_item, mViewGroup, false);
        textView.setText(screenName);
        textView.setTag(screenName);
        textView.setId(id);
        textView.setOnClickListener(this);

        mViewGroup.addView(textView, mViewGroup.getChildCount() - 1);
    }

    public interface OnFragmentInteractionListener {
        void onMenuItemClicked(Fragment fragment, String screenName);
    }
}
