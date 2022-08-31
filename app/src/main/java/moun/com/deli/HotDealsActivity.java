package moun.com.deli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import moun.com.deli.fragment.HotDealsDetailFragment;
import moun.com.deli.fragment.HotDealsListFragment;
import moun.com.deli.util.AppUtils;

/**
 * An Activity handling two custom {@link android.support.v4.app.Fragment},
 * HotDealsListFragment and HotDealsDetailFragment.
 */
public class HotDealsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitle;
    private Fragment contentFragment;
    private HotDealsListFragment hotDealsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_deals);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.hot_deals));
        mTitle.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOLD));

        FragmentManager fragmentManager = getSupportFragmentManager();
        // Used for orientation change.
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("content")) {
                String content = savedInstanceState.getString("content");
                if (content.equals(HotDealsListFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(HotDealsListFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(HotDealsListFragment.ARG_ITEM_ID);
                    }
                }
                if (content.equals(HotDealsDetailFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(HotDealsDetailFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(HotDealsDetailFragment.ARG_ITEM_ID);
                    }
                }
            }

        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hotDealsListFragment = new HotDealsListFragment();
            transaction.replace(R.id.hot_content_fragment, hotDealsListFragment, HotDealsListFragment.ARG_ITEM_ID);
            transaction.commit();
        }

    }

    /**
     * Before the activity is destroyed, onSaveInstanceState() gets called.
     * The onSaveInstanceState() method saves the current fragment.
     *
     * @param outState bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof HotDealsListFragment) {
            outState.putString("content", HotDealsListFragment.ARG_ITEM_ID);
        } else if (contentFragment instanceof HotDealsDetailFragment) {
            outState.putString("content", HotDealsDetailFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hot_deals, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_cart:
                Intent intent = new Intent(this, MyCartActivity.class);
                startActivity(intent);
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
