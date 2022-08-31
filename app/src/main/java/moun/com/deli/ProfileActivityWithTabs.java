package moun.com.deli;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import moun.com.deli.util.AppUtils;
import moun.com.deli.util.ProfilePagerAdapter;

/**
 * An Activity handling two custom {@link android.support.v4.app.Fragment},
 * FavoritesFragment and OrdersHistoryFragment with {@link TabLayout} and {@link ViewPager}.
 */
public class ProfileActivityWithTabs extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.my_profile));
        mTitle.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOLD));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.favorites_tab)));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.orders_tab));

        // Custom tabs with text and icon.
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.favorites_tab));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_favorite_white_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.orders_tab));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_history_white_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ProfilePagerAdapter adapter = new ProfilePagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        int i = getIntent().getIntExtra("historyTab", 0);
        if (i == 1) {
            viewPager.setCurrentItem(1);
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_cart) {
            Intent intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
