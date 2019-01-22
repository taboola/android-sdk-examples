package com.taboola.android.sdksamples.tabs;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;

public abstract class BaseTaboolaFragment extends Fragment {

    public static final String VIEW_ID = "VIEW_ID";

    protected String mViewId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mViewId = getArguments().getString(VIEW_ID);
        }
    }

    public void onPageSelected() {

    }

}
