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
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import moun.com.deli.R;
import moun.com.deli.adapter.FavoritesListAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * This Fragment used to handle the list of favorite items fetched from SQLite database
 * (favorite table) using {@link RecyclerView} with a {@link LinearLayoutManager}.
 */
public class FavoritesFragment extends Fragment implements FavoritesListAdapter.ClickListener {

    private static final String LOG_TAG = FavoritesFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "favorite_list";
    private RecyclerView favoritesRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FavoritesListAdapter favoritesListAdapter;
    private ItemsDAO itemsDAO;
    private GetItemsCartTask task;
    private ArrayList<MenuItems> itemsFavoritesList;
    private TextView emtyFavorites;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsDAO = new ItemsDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        favoritesRecyclerView = (RecyclerView) rootView.findViewById(R.id.favorites_recyclerView);
        emtyFavorites = (TextView) rootView.findViewById(R.id.empty);
        emtyFavorites.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        favoritesRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(mLayoutManager);
        favoritesListAdapter = new FavoritesListAdapter(getActivity());
        favoritesListAdapter.setClickListener(this);

        task = new GetItemsCartTask(getActivity());
        task.execute((Void) null);

        return rootView;

    }

    @Override
    public void itemClicked(View view, int position) {
        MenuItems menuItems = itemsFavoritesList.get(position);
        if (menuItems != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedItem", menuItems);
            CustomDialogFragment customDialogFragment = new CustomDialogFragment();
            customDialogFragment.setArguments(arguments);
            customDialogFragment.show(getFragmentManager(),
                    CustomDialogFragment.ARG_ITEM_ID);
        }
    }

    @Override
    public void itemdeleted(View view, int position) {
        MenuItems menuItems = (MenuItems) itemsFavoritesList.get(position);
        itemsDAO.deleteFromFavorites(menuItems);
        favoritesListAdapter.removeAt(position);

        if (itemsFavoritesList.size() == 0) {
            emtyFavorites.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Get favorite items from Favorite table asynchronously.
     */
    public class GetItemsCartTask extends AsyncTask<Void, Void, ArrayList<MenuItems>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetItemsCartTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<MenuItems> doInBackground(Void... arg0) {
            ArrayList<MenuItems> itemsList = itemsDAO.getFavoriteItems();
            return itemsList;
        }

        @Override
        protected void onPostExecute(ArrayList<MenuItems> favoriteList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                Log.d("items", favoriteList.toString());
                itemsFavoritesList = favoriteList;
                if (favoriteList != null) {
                    if (favoriteList.size() != 0) {
                        emtyFavorites.setVisibility(View.GONE);
                        favoritesListAdapter.setItemsList(favoriteList);
                        favoritesRecyclerView.setAdapter(favoritesListAdapter);

                    } else {
                        emtyFavorites.setVisibility(View.VISIBLE);

                    }
                }

            }
        }
    }
}
