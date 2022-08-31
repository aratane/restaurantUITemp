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
import moun.com.deli.model.Items;
import moun.com.deli.util.AppUtils;

/**
 * Provide view to Items RecyclerView with data from Items object.
 */
public class ItemsOrderHistoryAdapter extends RecyclerView.Adapter<ItemsOrderHistoryAdapter.ViewHolder> {
    private static final String LOG_TAG = ItemsOrderHistoryAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<Items> itemsList;

    /**
     * Create a new instance of {@link ItemsOrderHistoryAdapter}.
     * @param context host Activity.
     */
    public ItemsOrderHistoryAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

    }

    public void setItemsList(ArrayList<Items> itemsList) {
        this.itemsList = itemsList;
        //    notifyDataSetChanged();
        notifyItemInserted(itemsList.size());

    }

    // Create new view (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.single_row_items_order_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Items cartItems = itemsList.get(position);
        // Get element from Items object at this position and replace the contents of the view
        // with that element
        viewHolder.itemTitle.setText(cartItems.getItemName());
        viewHolder.itemQuantity.setText("Quantity: " + cartItems.getItemQuantity());
        viewHolder.itemTotal.setText("Total: $" + cartItems.getItemPrice() * cartItems.getItemQuantity());
        viewHolder.itemImage.setImageResource(cartItems.getItemImage());

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
        public TextView itemQuantity;
        public TextView itemTotal;
        public ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.history_item_title);
            this.itemTitle.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            itemQuantity = (TextView) itemView.findViewById(R.id.history_item_qty);
            this.itemQuantity.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemTotal = (TextView) itemView.findViewById(R.id.history_item_total);
            this.itemTotal.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemImage = (ImageView) itemView.findViewById(R.id.history_item_image);
        }

    }
}
