package com.example.multichoice_app.adapter;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.multichoice_app.R;
import com.example.multichoice_app.fragment.DapAnFragment;


import java.util.ArrayList;

public class AdapterDapAnFragment extends FragmentPagerAdapter {

    Activity context;
    ArrayList<DapAnFragment> fragmentList;

    public AdapterDapAnFragment(FragmentManager fm, Activity context, ArrayList<DapAnFragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    public AdapterDapAnFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return context.getResources().getString(R.string.sentence) + " " + (position + 1);
    }
}
