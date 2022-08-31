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
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import moun.com.deli.database.ItemsDAO;
import moun.com.deli.fragment.EditCartCustomDialogFragment;
import moun.com.deli.fragment.MyCartCheckoutFragment;
import moun.com.deli.fragment.MyCartFragment;
import moun.com.deli.model.Items;
import moun.com.deli.util.AppUtils;


/**
 * An Activity handling two custom {@link android.support.v4.app.Fragment},
 * MyCartFragment and MyCartCheckoutFragment.
 */
public class MyCartActivity extends AppCompatActivity implements EditCartCustomDialogFragment.EditCartDialogFragmentListener,
        MyCartFragment.NumberOfItemChangedListener {

    private Toolbar mToolbar;
    private TextView mTitle;
    private Fragment contentFragment;
    private MyCartFragment myCartFragment;
    private View mHeaderCardView;
    private TextView numberOfItems;
    private ItemsDAO itemsDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_cart);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.my_cart));
        mTitle.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOLD));
        mHeaderCardView = findViewById(R.id.header_card_view);
        numberOfItems = (TextView) findViewById(R.id.numb_of_items);
        addItemsNumber();

        FragmentManager fragmentManager = getSupportFragmentManager();
        // Used for orientation change.
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("content")) {
                String content = savedInstanceState.getString("content");
                if (content.equals(MyCartFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(MyCartFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(MyCartFragment.ARG_ITEM_ID);
                    }
                }
                if (content.equals(MyCartCheckoutFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(MyCartCheckoutFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(MyCartCheckoutFragment.ARG_ITEM_ID);
                    }
                }

            }


        } else {
            myCartFragment = new MyCartFragment();
            switchContent(myCartFragment, MyCartFragment.ARG_ITEM_ID);
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
        if (contentFragment instanceof MyCartFragment) {
            outState.putString("content", MyCartFragment.ARG_ITEM_ID);
        } else if (contentFragment instanceof MyCartCheckoutFragment) {
            outState.putString("content", MyCartCheckoutFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }


    /**
     * Used to replace One Fragment with Another and add the transaction to the back.
     *
     * @param fragment the fragment.
     * @param tag      the fragment tag.
     */
    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate()) ;

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out);
            // Replace whatever is in the content_fragment_cart view with this fragment
            transaction.replace(R.id.content_fragment_cart, fragment, tag);

            if (!(fragment instanceof MyCartFragment)) {
                // add the transaction to the back stack so the user can navigate back
                transaction.addToBackStack(tag);
            }

            // Commit the transaction
            transaction.commit();
            contentFragment = fragment;
        }
    }

    public void addItemsNumber() {
        itemsDAO = new ItemsDAO(this);
        ArrayList<Items> itemsList = itemsDAO.getCartItemsNotOrdered();
        String item = null;
        if (itemsList.size() == 0 || itemsList.size() == 1) {
            item = "item";
        } else if (itemsList.size() > 1) {
            item = "items";
        }
        numberOfItems.setText("YOU HAVE " + itemsList.size() + " " + item + " IN YOUR CART");
        numberOfItems.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOOK));

    }


    /*
     * Callback used to communicate with MyCartFragment to notify the list adapter.
     * Communication between fragments goes via their Activity class.
     */
    @Override
    public void onFinishDialog() {
        if (myCartFragment != null) {
            myCartFragment.updateView();
        }

    }

    @Override
    public void onNumberChanged() {
        addItemsNumber();
        if (myCartFragment != null) {
            myCartFragment.updateView();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_menu) {
            Intent intent = new Intent(this, MenuActivityWithTabs.class);
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
