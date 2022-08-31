package moun.com.deli.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import moun.com.deli.fragment.MenuBurgersFragment;
import moun.com.deli.fragment.MenuDrinksFragment;
import moun.com.deli.fragment.MenuPizzaFragment;
import moun.com.deli.fragment.MenuSaladsFragment;
import moun.com.deli.fragment.MenuSandwichFragment;
import moun.com.deli.fragment.MenuDessertsFragment;

/**
 * Class define a view pager adapter for the swipe tabs feature.
 */

public class MenuPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MenuPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                // Create a new fragment
                MenuSandwichFragment tab1 = new MenuSandwichFragment();
                return tab1;
            case 1:
                MenuBurgersFragment tab2 = new MenuBurgersFragment();
                return tab2;
            case 2:
                MenuPizzaFragment tab3 = new MenuPizzaFragment();
                return tab3;
            case 3:
                MenuSaladsFragment tab4 = new MenuSaladsFragment();
                return tab4;
            case 4:
                MenuDessertsFragment tab5 = new MenuDessertsFragment();
                return tab5;
            case 5:
                MenuDrinksFragment tab6 = new MenuDrinksFragment();
                return tab6;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
