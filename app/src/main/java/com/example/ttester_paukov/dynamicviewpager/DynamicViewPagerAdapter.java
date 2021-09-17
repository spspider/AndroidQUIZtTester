package com.example.ttester_paukov.dynamicviewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by anskaal on 2/29/16.
 */
public class DynamicViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;

    public DynamicViewPagerAdapter(FragmentManager fm){
        super(fm);

        mFragments = new ArrayList<>();
    }

    public void setItems(ArrayList<Fragment> fragments) {
        mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return mFragments.indexOf(object);
    }


}
