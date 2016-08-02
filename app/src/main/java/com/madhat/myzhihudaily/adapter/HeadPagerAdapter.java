package com.madhat.myzhihudaily.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.ui.fragment.HeadItemFragment;

import java.util.List;

/**
 * Created by Avast on 2016/7/19.
 */
public class HeadPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    public void setFragmentList(List<Fragment> fragments) {
        if(this.fragmentList != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragmentList){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragmentList = fragments;
        notifyDataSetChanged();
    }

    private List<Fragment> fragmentList;
    private FragmentManager fm;
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public HeadPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fm=fm;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        HeadItemFragment headItemFragment =(HeadItemFragment) super.instantiateItem(container, position);
        return  headItemFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}

