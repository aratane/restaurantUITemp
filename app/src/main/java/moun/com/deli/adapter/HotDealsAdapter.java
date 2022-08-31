package moun.com.deli.adapter;

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
 * Provide view to Hot Deals RecyclerView with data from MenuItems object.
 */
public class HotDealsAdapter extends RecyclerView.Adapter<HotDealsAdapter.ViewHolder> {
    private static final String LOG_TAG = HotDealsAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<MenuItems> itemsList;
    private ClickListener clickListener;
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private final View header;


    public HotDealsAdapter(View header, ArrayList<MenuItems> itemsList) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.itemsList = itemsList;
    }

    public void setItemsList(ArrayList<MenuItems> itemsList) {
        this.itemsList = itemsList;
        notifyItemInserted(itemsList.size());

    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new ViewHolder(header);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_hot_grid, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (isHeader(position)) {
            return;
        }
        // Subtract 1 for header
        MenuItems menuItems = itemsList.get(position - 1);
        viewHolder.itemTitle.setText(menuItems.getItemName());
        viewHolder.itemImage.setImageResource(menuItems.getItemImage());
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return itemsList.size() + 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public ImageView itemImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.hot_deal_title);
            try {
                this.itemTitle.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemImage = (ImageView) itemView.findViewById(R.id.hot_deaL_image);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }


    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
