package moun.com.deli;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import moun.com.deli.util.AppUtils;
import moun.com.deli.util.MenuPagerAdapter;

/**
 * An Activity handling two custom {@link android.support.v4.app.Fragment}s,
 * with {@link TabLayout} and {@link ViewPager}.
 */
public class MenuActivityWithTabs extends AppCompatActivity {

    FloatingActionButton fab;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.our_menu));
        mTitle.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOLD));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sandwich)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.burgers)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pizza)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.salads)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sweets)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.drinks)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MenuPagerAdapter adapter = new MenuPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        int i = getIntent().getIntExtra("currentItem", 0);
        if (i == 1) {
            viewPager.setCurrentItem(0);
        }
        if (i == 2) {
            viewPager.setCurrentItem(1);
        }
        if (i == 3) {
            viewPager.setCurrentItem(2);
        }
        if (i == 4) {
            viewPager.setCurrentItem(3);
        }
        if (i == 5) {
            viewPager.setCurrentItem(4);
        }
        if (i == 6) {
            viewPager.setCurrentItem(5);
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivityWithTabs.this, MyCartActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_our_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_hot) {
            Intent intent = new Intent(this, HotDealsActivity.class);
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
