package moun.com.deli.fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import moun.com.deli.R;
import moun.com.deli.adapter.MenuListAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;


/**
 * This Fragment used to handle the list of items under Sandwich category using
 * {@link RecyclerView} with a {@link LinearLayoutManager}.
 */
public class MenuSaladsFragment extends Fragment implements MenuListAdapter.ClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuListAdapter menuListAdapter;
    ArrayList<MenuItems> listItems;
    private static final String ITEMS_STATE = "items_state";
    private AlphaInAnimationAdapter alphaAdapter;
    private ItemsDAO itemDAO;
    private AddItemTask task;
    private MenuItems menuItemsFavorite = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        itemDAO = new ItemsDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_list_items, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.sandwich_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Used for orientation change.
        if (savedInstanceState != null) {
            // We will restore the state of data list when the activity is re-created
            listItems = savedInstanceState.getParcelableArrayList(ITEMS_STATE);
        } else {
            // Initialize listItems.
            listItems = getSaladMenuList();

        }
        menuListAdapter = new MenuListAdapter(getActivity(), listItems, inflater, R.layout.single_row_menu_list);
        // Set MenuListAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(menuListAdapter);
        menuListAdapter.setClickListener(this);

        return rootView;
    }

    /**
     * Before the activity is destroyed, onSaveInstanceState() gets called.
     * The onSaveInstanceState() method saves the list of data.
     *
     * @param outState bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ITEMS_STATE, listItems);
    }

    @Override
    public void itemClicked(View view, int position, boolean isLongClick) {
        MenuItems menuItems = getSaladMenuList().get(position);
        if (isLongClick) {
            if (itemDAO.getItemFavorite(menuItems.getItemName()) == null) {
                menuItemsFavorite = new MenuItems();
                menuItemsFavorite.setItemName(menuItems.getItemName());
                menuItemsFavorite.setItemDescription(menuItems.getItemDescription());
                menuItemsFavorite.setItemImage(menuItems.getItemImage());
                menuItemsFavorite.setItemPrice(menuItems.getItemPrice());
                task = new AddItemTask(getActivity());
                task.execute((Void) null);
                // set heart_red drawable
                ImageView heart = (ImageView) view.findViewById(R.id.heart);
                heart.setImageResource(R.mipmap.ic_favorite_red_24dp);
            } else {
                AppUtils.CustomToast(getActivity(), getString(R.string.already_added_to_favorites));
            }

        } else {
            if (menuItems != null) {
                Bundle arguments = new Bundle();
                arguments.putParcelable("selectedItem", menuItems);
                // Create an instance of the dialog fragment and give it an argument for the selected article
                // and show it
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.setArguments(arguments);
                customDialogFragment.show(getFragmentManager(),
                        CustomDialogFragment.ARG_ITEM_ID);
            }
        }


    }

    /**
     * Save the item to Favorite table asynchronously.
     */
    public class AddItemTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;

        public AddItemTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = itemDAO.saveToFavoriteTable(menuItemsFavorite);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    AppUtils.CustomToast(getActivity(), getString(R.string.added_to_favorites));
                Log.d("ITEM: ", menuItemsFavorite.toString());
            }
        }
    }

    /**
     * Generates data for RecyclerView's adapter, this data would usually come from a local content provider
     * or remote server.
     *
     * @return items list
     */
    private ArrayList<MenuItems> getSaladMenuList() {
        ArrayList<MenuItems> menuItems = new ArrayList<MenuItems>();
        menuItems.add(new MenuItems(getString(R.string.uptown), R.drawable.salad1, 9.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.mediterranean), R.drawable.salad2, 11.25, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.farmhouse), R.drawable.salad3, 10.50, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.chicken), R.drawable.salad4, 13.75, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.wreck), R.drawable.salad5, 8.25, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.chickpea), R.drawable.salad6, 11.00, getString(R.string.short_lorem)));

        return menuItems;
    }
}
