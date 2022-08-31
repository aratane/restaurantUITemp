package moun.com.deli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Provide views to RecyclerView with data from MenuItems object.
 */
public class HomeMenuCustomAdapter extends RecyclerView.Adapter<HomeMenuCustomAdapter.ViewHolder> {
    private static final String LOG_TAG = HomeMenuCustomAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private int mResourceId;
    private ArrayList<MenuItems> itemList;
    private Context context;
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private final View header;
    private ClickListener clickListener;

    /**
     * Create a new instance of {@link HomeMenuCustomAdapter}.
     *
     * @param context    host Activity.
     * @param header     The view of header
     * @param itemList   List of data.
     * @param inflater   The layout inflater.
     * @param resourceId The resource ID for the layout to be used. The layout should contain an
     *                   ImageView with ID of "meat_image" and a TextView with ID of "meat_title".
     */
    public HomeMenuCustomAdapter(Context context, View header, ArrayList<MenuItems> itemList, LayoutInflater inflater, int resourceId) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.itemList = itemList;
        this.context = context;
        mLayoutInflater = inflater;
        mResourceId = resourceId;
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title;

        public ViewHolder(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.menu_title);
            try {
                this.title.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            } catch (Exception e) {
                e.printStackTrace();
            }
            image = (ImageView) itemView.findViewById(R.id.menu_image);
            // Define click listener for the ViewHolder's View.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(itemView, getAdapterPosition());
                        Log.d(LOG_TAG, "Position " + getAdapterPosition() + " clicked.");
                    }
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new ViewHolder(header);
        }
        // Create a new view.
        View view = mLayoutInflater.inflate(mResourceId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //    Log.d(LOG_TAG, "Element " + position + " set.");
        if (isHeader(position)) {
            return;
        }
        // Subtract 1 for header
        MenuItems menuItems = itemList.get(position - 1);
        // Get element from MenuItems object at this position and replace the contents of the view
        // with that element
        viewHolder.image.setImageResource(menuItems.getItemImage());
        viewHolder.title.setText(menuItems.getItemName());

    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    // Return the size of menuItems (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.itemList.size() + 1;
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    // An interface to Define click listener for the ViewHolder's View from any where.
    public interface ClickListener {
        public void itemClicked(View view, int position);

    }
}
