package com.taboola.android.sdksamples.tabs;

import android.os.Bundle;

public class TabsPresenterImp implements TabsContract.TabsPresenter {

    private static final String TAG = "PagePresenterImp";
    private static final String PAGE_KEY = TAG + " " + "page_key";
    private TabsContract.TabsView mTabsView;
    private int mCurrentPage;


    @Override
    public void takeView(TabsContract.TabsView view) {
        mTabsView = view;
    }

    @Override
    public void dropView() {
        mTabsView = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        mCurrentPage = savedInstanceState.getInt(PAGE_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putInt(PAGE_KEY, mCurrentPage);
    }

    @Override
    public void onStart() {
        mTabsView.setCurrentPage(mCurrentPage);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {
        dropView();
    }


    @Override
    public void setCurrentPage(int position) {
        mCurrentPage = position;
    }
}
