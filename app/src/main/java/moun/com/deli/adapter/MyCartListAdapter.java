package moun.com.deli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import moun.com.deli.R;
import moun.com.deli.model.Items;
import moun.com.deli.util.AppUtils;

/**
 * Provide view to Items RecyclerView with data from Items object.
 */
public class MyCartListAdapter extends RecyclerView.Adapter<MyCartListAdapter.ViewHolder>{
    private static final String LOG_TAG = MyCartListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<Items> itemsList;
    private ButtonClickListener clickListener;

    /**
     * Create a new instance of {@link MyCartListAdapter}.
     * @param context host Activity.
     */
    public MyCartListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

    }

    public void setItemsList(ArrayList<Items> itemsList) {
        this.itemsList = itemsList;
        //    notifyDataSetChanged();
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
        View view = mLayoutInflater.inflate(R.layout.single_row_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Items cartItems = itemsList.get(position);
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
        public ImageButton itemDelete;
        public ImageButton itemEdite;
        public ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.cart_item_title);
            this.itemTitle.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            itemQuantity = (TextView) itemView.findViewById(R.id.cart_item_qty);
            this.itemQuantity.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemTotal = (TextView) itemView.findViewById(R.id.cart_item_total);
            this.itemTotal.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemDelete = (ImageButton) itemView.findViewById(R.id.cart_delete_btn);
            itemEdite = (ImageButton) itemView.findViewById(R.id.cart_edit_btn);
            itemImage = (ImageView) itemView.findViewById(R.id.cart_item_image);

            itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.deleteClicked(v, getAdapterPosition());
                    }
                }


            });
            itemEdite.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.editClicked(v, getAdapterPosition());
                    }
                }
            });
        }

    }

    public void setClickListener(ButtonClickListener clickListener){
        this.clickListener = clickListener;

    }

    // An interface to Define click listener for the ViewHolder's View from any where.
    public interface ButtonClickListener{
        public void deleteClicked(View view, int position);
        public void editClicked(View view, int position);
    }
}
