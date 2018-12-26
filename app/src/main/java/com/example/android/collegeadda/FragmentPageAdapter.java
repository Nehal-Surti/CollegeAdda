package com.example.android.collegeadda;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.collegeadda.Fragments.Chat;

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private String names = "Nehal";
    private Context context;

    public FragmentPageAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0)
        {
            return new Chat();
        }
        else {
            return new Chat();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return names;
    }
}
