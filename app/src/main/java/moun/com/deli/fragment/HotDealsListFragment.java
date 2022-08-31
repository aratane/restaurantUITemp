package moun.com.deli.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

import moun.com.deli.R;
import moun.com.deli.adapter.HotDealsAdapter;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * This Fragment used to handle the list of hot deal items
 * using {@link RecyclerView} with a {@link GridLayoutManager} with header on top.
 */
public class HotDealsListFragment extends Fragment implements HotDealsAdapter.ClickListener {

    private static final String LOG_TAG = HotDealsListFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "hot_deals_list";
    private RecyclerView hotRecyclerView;
    private static final int SPAN_COUNT = 2;
    private HotDealsAdapter hotDealsAdapter;
    ArrayList<MenuItems> hotDealsList;
    private static final String ITEMS_STATE = "items_state";
    private TextView headerText;


    public static HotDealsListFragment newInstance() {
        return new HotDealsListFragment();
    }

    public HotDealsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hot_deals_list, container, false);

        hotRecyclerView = (RecyclerView) rootView.findViewById(R.id.hot_recyclerView);
        hotRecyclerView.setHasFixedSize(true);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        hotRecyclerView.setLayoutManager(manager);
        if (savedInstanceState != null) {
            // We will restore the state of data list when the activity is re-created
            hotDealsList = savedInstanceState.getParcelableArrayList(ITEMS_STATE);
        } else {
            hotDealsList = getHotDealsList();

        }
        // Inflate the layout header
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.hot_deals_grid_header, hotRecyclerView, false);
        // set Custom font to header text
        headerText = (TextView) header.findViewById(R.id.header_text);
        headerText.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        // Set header not clickable
        header.setEnabled(false);
        header.setOnClickListener(null);
        // Initialize the adapter with two view types, one for the header and one for the items
        hotDealsAdapter = new HotDealsAdapter(header, hotDealsList);
        hotRecyclerView.setAdapter(hotDealsAdapter);
        // Override setSpanSizeLookup in GridLayoutManager to return the span count as the span size for the header
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return hotDealsAdapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });
        hotDealsAdapter.setClickListener(this);

        return rootView;


    }

    // Before the activity is destroyed, onSaveInstanceState() gets called.
    // The onSaveInstanceState() method saves the list of data.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ITEMS_STATE, hotDealsList);
    }

    @Override
    public void itemClicked(View view, int position) {
        MenuItems menuItems = hotDealsList.get(position - 1);
        Log.i(LOG_TAG, menuItems.getItemName() + " clicked. Replacing fragment.");
        // We start the fragment transaction here. It is just an ordinary fragment transaction.
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.hot_content_fragment,
                        HotDealsDetailFragment.newInstance(menuItems,
                                (int) view.getX(), (int) view.getY(),
                                view.getWidth(), view.getHeight())
                )
                // We push the fragment transaction to back stack. User can go back to the
                // previous fragment by pressing back button.
                .addToBackStack("detail")
                .commit();

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

    /**
     * Generates data for RecyclerView's adapter, this data would usually come from a local content provider
     * or remote server.
     *
     * @return items list
     */
    private ArrayList<MenuItems> getHotDealsList() {
        ArrayList<MenuItems> menuItems = new ArrayList<MenuItems>();
        menuItems.add(new MenuItems(getString(R.string.deal_1), R.drawable.deal1, 9.50, getString(R.string.deal_description)));
        menuItems.add(new MenuItems(getString(R.string.deal_2), R.drawable.deal2, 18.00, getString(R.string.deal_description)));
        menuItems.add(new MenuItems(getString(R.string.deal_3), R.drawable.deal3, 10.50, getString(R.string.deal_description)));
        menuItems.add(new MenuItems(getString(R.string.deal_4), R.drawable.deal4, 25.00, getString(R.string.deal_description)));
        menuItems.add(new MenuItems(getString(R.string.deal_5), R.drawable.deal5, 8.25, getString(R.string.deal_description)));
        menuItems.add(new MenuItems(getString(R.string.deal_6), R.drawable.deal6, 30.50, getString(R.string.deal_description)));


        return menuItems;
    }


}
