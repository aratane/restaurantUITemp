package moun.com.deli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import moun.com.deli.R;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Provide view to Favorites RecyclerView with data from MenuItems object.
 */
public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {
    private static final String LOG_TAG = FavoritesListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<MenuItems> itemsList;
    private ClickListener clickListener;

    /**
     * Create a new instance of {@link FavoritesListAdapter}.
     * @param context host Activity.
     */
    public FavoritesListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * This method to set data from itemList.
     * @param itemsList The list of data from MenuItems object.
     */
    public void setItemsList(ArrayList<MenuItems> itemsList) {
        this.itemsList = itemsList;
        notifyItemInserted(itemsList.size());
    }

    /**
     * Remove items from the list adapter and notify changes.
     * @param position the position of item removed.
     */
    public void removeAt(int position) {
        itemsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemsList.size());
    }

    // Create new view (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.single_row_favorites, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MenuItems menuItems = itemsList.get(position);
        // Get element from MenuItems object at this position and replace the contents of the view
        // with that element
        viewHolder.itemTitle.setText(menuItems.getItemName());
        viewHolder.itemPrice.setText("$" + menuItems.getItemPrice());
        viewHolder.itemImage.setImageResource(menuItems.getItemImage());
    }

    // Return the size of itemsList (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemPrice;
        public ImageView itemImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.favorite_item_title);
            this.itemTitle.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            itemPrice = (TextView) itemView.findViewById(R.id.favorite_item_price);
            this.itemPrice.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            itemImage = (ImageView) itemView.findViewById(R.id.favorite_item_image);

            itemView.findViewById(R.id.favorite_item_detail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(itemView, getAdapterPosition());
                    }
                }
            });

            itemView.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemdeleted(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    // An interface to Define click listener for the ViewHolder's View from any where such as add
    // to cart or delete.
    public interface ClickListener {
        public void itemClicked(View view, int position);

        public void itemdeleted(View view, int position);
    }
}
