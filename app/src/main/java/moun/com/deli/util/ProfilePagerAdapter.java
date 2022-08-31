package moun.com.deli.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import moun.com.deli.adapter.OrdersHistoryAdapter;
import moun.com.deli.fragment.FavoritesFragment;
import moun.com.deli.fragment.OrdersHistoryFragment;

/**
 * Class define a view pager adapter for the swipe tabs feature.
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
                OrdersHistoryFragment tab2 = new OrdersHistoryFragment();
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
