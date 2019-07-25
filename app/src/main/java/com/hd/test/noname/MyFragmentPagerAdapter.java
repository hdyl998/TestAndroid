package com.hd.test.noname;

/**
 * Created by liugd on 2017/3/28.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragmentList;
    private String[] titleList;

    public MyFragmentPagerAdapter(FragmentManager fm, Fragment[] fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public MyFragmentPagerAdapter(FragmentManager fm, Fragment[] fragmentList, String[] titleList) {
        this(fm, fragmentList);
        this.titleList = titleList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList[position];
    }

    @Override
    public int getCount() {
        return fragmentList.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "AAA";
    }

}
