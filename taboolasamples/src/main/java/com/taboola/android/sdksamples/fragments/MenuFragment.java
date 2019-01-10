package com.taboola.android.sdksamples.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.api.FourItemsApiFragment;
import com.taboola.android.sdksamples.api.feed.FeedApiFragment;
import com.taboola.android.sdksamples.js.MidWidgetWithFeedJsFragment;
import com.taboola.android.sdksamples.js.SplitFeedJsFragment;
import com.taboola.android.sdksamples.js.ViewPagerViaJsFragment;
import com.taboola.android.sdksamples.standard.FeedWithMiddleArticleInsideListViewFragment;
import com.taboola.android.sdksamples.standard.FeedWithMiddleArticleInsideRecycleViewCustomFragment;
import com.taboola.android.sdksamples.standard.FeedWithMiddleArticleInsideRecycleViewFragment;
import com.taboola.android.sdksamples.standard.FeedWithMiddleArticleInsideScrollViewFragment;
import com.taboola.android.sdksamples.standard.ViewPagerViaStandartFragment;

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
        addHeader("sdk standard");
        addButton("Mid Widget With Feed inside ScrollView", R.id.std_mid_article_with_feed_lnr);
        addButton("Mid Widget With Feed inside ListView", R.id.std_mid_article_with_feed_lv);
        addButton("Mid Widget With Feed inside RecyclerView", R.id.std_mid_article_with_feed_rv);
        addButton("Mid Widget With Feed inside RecyclerView (Manual scroll)", R.id.std_mid_article_with_feed_rv_manual);
        addButton("View Pager", R.id.std_view_pager);

        addHeader("sdk js");
        addButton("Mid Widget With Feed ", R.id.js_mid_article_with_feed);
        addButton("Split Feed", R.id.js_split);
        addButton("View Pager", R.id.js_view_pager);

        addHeader("sdk api");
        addButton("Widget With 4 items", R.id.api_4_items_widget);
        addButton("Feed", R.id.api_feed);
    }


    @Override
    public void onClick(View v) {

        String screenName = v.getTag().toString();
        Fragment fragmentToOpen = null;
        switch (v.getId()) {
            case R.id.api_4_items_widget:
                fragmentToOpen = new FourItemsApiFragment();
                break;

            case R.id.api_feed:
                fragmentToOpen = new FeedApiFragment();
                break;

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
                fragmentToOpen = new ViewPagerViaStandartFragment();
                break;

            case R.id.std_mid_article_with_feed_lv:
                fragmentToOpen = new FeedWithMiddleArticleInsideListViewFragment();
                break;

            case R.id.std_mid_article_with_feed_rv:
                fragmentToOpen = new FeedWithMiddleArticleInsideRecycleViewFragment();
                break;

            case R.id.std_mid_article_with_feed_rv_manual:
                fragmentToOpen = new FeedWithMiddleArticleInsideRecycleViewCustomFragment();
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
        mViewGroup.addView(textView);
    }

    private void addButton(String screenName, int id) {
        Context context = mViewGroup.getContext();
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.button_item, mViewGroup, false);
        textView.setText(screenName);
        textView.setTag(screenName);
        textView.setId(id);
        textView.setOnClickListener(this);

        mViewGroup.addView(textView);
    }

    public interface OnFragmentInteractionListener {
        void onMenuItemClicked(Fragment fragment, String screenName);
    }
}
