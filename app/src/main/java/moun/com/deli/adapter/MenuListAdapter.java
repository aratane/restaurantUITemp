package moun.com.deli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moun.com.deli.R;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Provide view to Menu list RecyclerView with data from MenuItems object.
 */
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder>{
    private static final String LOG_TAG = MenuListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private int mResourceId;
    private List<MenuItems> itemList;
    private Context context;
    private ClickListener clickListener;
    private ItemsDAO itemsDAO;

    /**
     * Create a new instance of {@link MenuListAdapter}.
     *
     * @param context    host Activity.
     * @param itemList   List of data.
     * @param inflater   The layout inflater.
     * @param resourceId The resource ID for the layout to be used. The layout should contain an
     *                   ImageView with ID of "meat_image" and a TextView with ID of "meat_title".
     */
    public MenuListAdapter(Context context, List<MenuItems> itemList, LayoutInflater inflater, int resourceId) {
        this.itemList = itemList;
        this.context = context;
        mLayoutInflater = inflater;
        mResourceId = resourceId;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView price;
        private ImageView heart;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.menu_title);
            this.title.setTypeface(AppUtils.getTypeface(v.getContext(), AppUtils.FONT_BOLD));
            image = (ImageView) v.findViewById(R.id.menu_image);
            price = (TextView) v.findViewById(R.id.menu_price);
            this.price.setTypeface(AppUtils.getTypeface(v.getContext(), AppUtils.FONT_BOLD));
            heart = (ImageView) v.findViewById(R.id.heart);
            itemsDAO = new ItemsDAO(v.getContext());


            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(v, getAdapterPosition(), false);
                        Log.d(LOG_TAG, "Element " + getAdapterPosition() + " clicked.");
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(v, getAdapterPosition(), true);
                    }

                    return true;
                }
            });
        }
    }

    // Create new view (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(mResourceId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //    Log.d(LOG_TAG, "Element " + position + " set.");

        MenuItems menuItems = itemList.get(position);
        // Get element from MenuItems object at this position and replace the contents of the view
        // with that element
        viewHolder.image.setImageResource(menuItems.getItemImage());
        viewHolder.title.setText(menuItems.getItemName());
        viewHolder.price.setText("$" + Double.parseDouble(String.valueOf(menuItems.getItemPrice())));

        // If an item exists in favorite table then set heart_red drawable
        if(itemsDAO.getItemFavorite(menuItems.getItemName()) == null){
            viewHolder.heart.setImageResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            viewHolder.heart.setImageResource(R.mipmap.ic_favorite_red_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }

    // An interface to Define click listener for the ViewHolder's View from any where.
    public interface ClickListener{
        public void itemClicked(View view, int position, boolean isLongClick);
    }
}
