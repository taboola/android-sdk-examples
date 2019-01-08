package com.taboola.android.sdksamples.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.api.FourItemsApiFragment;
import com.taboola.android.sdksamples.api.feed.FeedApiFragment;
import com.taboola.android.sdksamples.js.MidWidgetWithFeedJsFragment;
import com.taboola.android.sdksamples.js.SplitFeedJsFragment;

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
        addHeader("Sdk Standard");
        // TODO: 08/01/2019 add std screens
        addHeader("Sdk Js");
        addButton("Mid Widget With Feed ", R.id.js_mid_article_with_feed);
        addButton("Split Feed", R.id.js_split);
        addHeader("Sdk Api");
        addButton("Widget With 4 items", R.id.api_4_items_widget);
        addButton("Feed", R.id.api_feed);
    }


    @Override
    public void onClick(View v) {

        String screenName = v.getTag().toString();

        switch (v.getId()) {
            case R.id.api_4_items_widget:
                openFragment(new FourItemsApiFragment(), screenName);
                break;

            case R.id.api_feed:
                openFragment(new FeedApiFragment(), screenName);
                break;

            case R.id.js_split:
                openFragment(new SplitFeedJsFragment(), screenName);
                break;

            case R.id.js_mid_article_with_feed:
                openFragment(new MidWidgetWithFeedJsFragment(), screenName);
                break;

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

        textView.setTypeface(Typeface.MONOSPACE);
        textView.setText(title);
        mViewGroup.addView(textView);
    }

    private void addButton(String screenName, int id) {
        Context context = mViewGroup.getContext();
        Button button = (Button) LayoutInflater.from(context).inflate(R.layout.button_item, mViewGroup, false);
        button.setText(screenName);
        button.setTag(screenName);
        button.setId(id);
        button.setOnClickListener(this);
        mViewGroup.addView(button);
    }

    public interface OnFragmentInteractionListener {
        void onMenuItemClicked(Fragment fragment, String screenName);
    }
}
