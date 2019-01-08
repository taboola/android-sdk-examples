package com.taboola.android.sdksamples.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private FragmentManager.OnBackStackChangedListener onBackStackChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        resetToolbarTitle();

        onBackStackChangedListener = () -> {
            int lastBackStackEntryCount = getSupportFragmentManager().getBackStackEntryCount() - 1;

            if (lastBackStackEntryCount < 0) {
                resetToolbarTitle();
                if (getSupportActionBar() != null) {
                    showBackArrow(false);
                }
            } else {
                FragmentManager.BackStackEntry lastBackStackEntry =
                        getSupportFragmentManager().getBackStackEntryAt(lastBackStackEntryCount);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(lastBackStackEntry.getName());
                    showBackArrow(true);
                }
            }
        };

        getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new MenuFragment());
        transaction.commit();
    }

    private void showBackArrow(boolean shouldShowBackButton) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(shouldShowBackButton);
        getSupportActionBar().setDisplayShowHomeEnabled(shouldShowBackButton);
    }

    private void resetToolbarTitle() {
        getSupportActionBar().setTitle(R.string.toolbar_title);
    }


    @Override
    public void onMenuItemClicked(Fragment fragmentToOpen, String screenName) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragmentToOpen);
            transaction.addToBackStack(screenName);
            transaction.commit();
        } catch (Exception ignore) {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}