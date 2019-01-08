package com.taboola.android.sdksamples.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new MenuFragment());
        transaction.commit();
        mToolbar.setTitle("Choose example");

    }


    @Override
    public void onMenuItemClicked(Fragment fragmentToOpen, String screenName) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragmentToOpen);
            transaction.addToBackStack(screenName);
            transaction.commit();
            mToolbar.setTitle(screenName);

        } catch (Exception ignore) {
        }
    }


}
