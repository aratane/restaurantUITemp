package moun.com.deli.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import moun.com.deli.R;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;


public class HotDealsDetailFragment extends Fragment implements Animation.AnimationListener {

    private static final String LOG_TAG = HotDealsDetailFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "hot_deals_detail";

    private static final String ARG_X = "x";
    private static final String ARG_Y = "y";
    private static final String ARG_WIDTH = "width";
    private static final String ARG_HEIGHT = "height";
    private MenuItems menuHotItems;
    private ItemsDAO itemDAO;
    private MenuItems menuItemsFavorite = null;
    private AddItemTask task;
    ImageView heart;
    ImageButton dealFavorite;
    private ShareActionProvider mShareActionProvider;

    /**
     * Create a new instance of DetailFragment.
     *
     * @param menuItems All data of the items
     * @param x         The horizontal position of the grid item in pixel
     * @param y         The vertical position of the grid item in pixel
     * @param width     The width of the grid item in pixel
     * @param height    The height of the grid item in pixel
     * @return a new instance of HotDealsDetailFragment
     */
    public static HotDealsDetailFragment newInstance(MenuItems menuItems,
                                                     int x, int y, int width, int height) {
        HotDealsDetailFragment hotDealsDetailFragment = new HotDealsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedItem", menuItems);
        bundle.putInt(ARG_X, x);
        bundle.putInt(ARG_Y, y);
        bundle.putInt(ARG_WIDTH, width);
        bundle.putInt(ARG_HEIGHT, height);
        hotDealsDetailFragment.setArguments(bundle);
        return hotDealsDetailFragment;
    }

    public HotDealsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuHotItems = (MenuItems) getArguments().getParcelable("selectedItem");
        itemDAO = new ItemsDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_deals_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FrameLayout root = (FrameLayout) view;
        Context context = view.getContext();
        assert context != null;
        // This is how the fragment looks at first. Since the transition is one-way, we don't need to make
        // this a Scene.
        View item = LayoutInflater.from(context).inflate(R.layout.single_row_hot_grid, root, false);
        assert item != null;
        bind(item);
        // We adjust the position of the initial image with LayoutParams using the values supplied
        // as the fragment arguments.
        Bundle args = getArguments();
        FrameLayout.LayoutParams params = null;
        if (args != null) {
            params = new FrameLayout.LayoutParams(
                    args.getInt(ARG_WIDTH), args.getInt(ARG_HEIGHT));
            params.topMargin = args.getInt(ARG_Y);
            params.leftMargin = args.getInt(ARG_X);
        }
        root.addView(item, params);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Bind the views inside of parent with the fragment arguments.
     *
     * @param view The parent of views to bind.
     */
    private void bind(View view) {

        ImageView dealImage = (ImageView) view.findViewById(R.id.hot_deaL_image);
        dealImage.setImageResource(menuHotItems.getItemImage());
        TextView dealTitletitle = (TextView) view.findViewById(R.id.hot_deal_title);
        dealTitletitle.setText(menuHotItems.getItemName());
        dealTitletitle.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
        // We bind a listener for the fragment transaction. We only bind it when
        // this fragment is entering.
        if (animation != null && enter) {
            animation.setAnimationListener(this);
        }
        return animation;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // This method is called at the end of the animation for the fragment transaction.
        // There is nothing we need to do in this sample.
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onAnimationEnd(Animation animation) {
        // This method is called at the end of the animation for the fragment transaction,
        // which is perfect time to start our Transition.
        Log.i(LOG_TAG, "Fragment animation ended. Starting a Transition.");
        final Scene scene = Scene.getSceneForLayout((ViewGroup) getView(),
                R.layout.fragment_hot_deals_content, getActivity());
        TransitionManager.go(scene);
        // Note that we need to bind views with data after we call TransitionManager.go().
        bind(scene.getSceneRoot());
        TextView dealDescription = (TextView) getView().findViewById(R.id.hot_deal_description);
        dealDescription.setText(menuHotItems.getItemDescription());
        dealDescription.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOOK));
        TextView dealPrice = (TextView) getView().findViewById(R.id.hot_deal_price);
        dealPrice.setText("$" + menuHotItems.getItemPrice());
        dealPrice.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        ImageButton dealOrder = (ImageButton) getView().findViewById(R.id.hot_deal_order);
        dealOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuHotItems != null) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable("selectedItem", menuHotItems);
                    CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                    customDialogFragment.setArguments(arguments);
                    customDialogFragment.show(getFragmentManager(),
                            CustomDialogFragment.ARG_ITEM_ID);
                }
            }
        });
        dealFavorite = (ImageButton) getView().findViewById(R.id.hot_deal_favorite);
        //    heart = (ImageView) getView().findViewById(R.id.hot_deal_favorite);
        if (itemDAO.getItemFavorite(menuHotItems.getItemName()) == null) {
            dealFavorite.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        } else {
            dealFavorite.setImageResource(R.mipmap.ic_favorite_white_24dp);
        }
        dealFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuHotItems != null) {
                    if (itemDAO.getItemFavorite(menuHotItems.getItemName()) == null) {
                        menuItemsFavorite = new MenuItems();
                        menuItemsFavorite.setItemName(menuHotItems.getItemName());
                        menuItemsFavorite.setItemDescription(menuHotItems.getItemDescription());
                        menuItemsFavorite.setItemImage(menuHotItems.getItemImage());
                        menuItemsFavorite.setItemPrice(menuHotItems.getItemPrice());
                        task = new AddItemTask(getActivity());
                        task.execute((Void) null);

                        dealFavorite.setImageResource(R.mipmap.ic_favorite_white_24dp);
                    } else {
                        AppUtils.CustomToast(getActivity(), getString(R.string.already_added_to_favorites));
                    }
                }
            }
        });

        ImageButton dealShare = (ImageButton) getView().findViewById(R.id.hot_deal_share);
        dealShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.share_format,
                        menuHotItems.getItemName(),
                        menuHotItems.getItemDescription());
                //    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // This method is never called in this sample because the animation doesn't repeat.
    }


    /**
     * Save item to Favorite table asynchronously.
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
                Log.d("ITEM FAVORITE: ", menuItemsFavorite.toString());
            }
        }
    }
}
