package moun.com.deli.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import moun.com.deli.fragment.FavoritesFragment;

/**
 * Created by Mounzer on 12/9/2015.
 */
public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProfilePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FavoritesFragment tab1 = new FavoritesFragment();
                return tab1;
            case 1:
                FavoritesFragment tab2 = new FavoritesFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
