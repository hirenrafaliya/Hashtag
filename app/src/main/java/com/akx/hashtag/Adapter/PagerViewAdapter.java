package com.akx.hashtag.Adapter;

import com.akx.hashtag.Fragment.CaptionFragment;
import com.akx.hashtag.Fragment.HashtagFragment;

import androidx.annotation.Keep;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

@Keep
public class PagerViewAdapter extends FragmentPagerAdapter {

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position==0){
            fragment=new HashtagFragment();
        }
        else if(position==1){
            fragment=new CaptionFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
