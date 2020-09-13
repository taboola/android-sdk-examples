package com.taboola.android.sdksamples.tabs;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentsAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private final List<T> mFragments = new ArrayList<>();


    public FragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    public void clear() {
        mFragments.clear();
    }

    public void addFragment(T fragment) {
        mFragments.add(fragment);
    }

    @Override
    public T getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        if (manager != null) {
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }

        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ("page " + position);
    }
}
