package moun.com.deli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import moun.com.deli.R;
import moun.com.deli.helper.DateHelper;
import moun.com.deli.model.Orders;
import moun.com.deli.util.AppUtils;

/**
 * Provide view to Order history RecyclerView with data from MenuItems object.
 */
public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.ViewHolder> {
    private static final String LOG_TAG = OrdersHistoryAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<Orders> ordersList;
    private List<String> numbersList;
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);
    DateFormat fullDateFormat = DateFormat.getDateInstance(DateFormat.FULL);
    private ClickListener clickListener;
    private Context context;

    /**
     * Create a new instance of {@link OrdersHistoryAdapter}.
     * @param context host Activity.
     */
    public OrdersHistoryAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOrdersList(ArrayList<Orders> ordersList, int orderNumbers) {
        this.ordersList = ordersList;
        this.numbersList = new ArrayList<String>(orderNumbers);
        // Generates numbers for RecyclerView's adapter
        for (int i = 0; i < orderNumbers; ++i) {
            numbersList.add(String.valueOf(i));
        }
        //    notifyDataSetChanged();
        notifyItemInserted(ordersList.size());

    }

    // Create new view (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.single_row_orders_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final String numbers = numbersList.get(position);
        viewHolder.orderNumber.setText(numbers);
        // Get element from Orders object at this position and replace the contents of the view
        // with that element
        Orders orders = ordersList.get(position);
        viewHolder.orderFullDate.setText(fullDateFormat.format(orders.getDate_created()));
        viewHolder.orderPrettyDate.setText(DateHelper.getGridDate(context, orders.getDate_created()));

    }

    // Return the size of ordersList (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order;
        public TextView orderNumber;
        public TextView orderFullDate;
        public TextView orderPrettyDate;

        public ViewHolder(final View itemView) {
            super(itemView);
            order = (TextView) itemView.findViewById(R.id.order);
            this.order.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            orderNumber = (TextView) itemView.findViewById(R.id.orders_history_numb);
            this.orderNumber.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            orderFullDate = (TextView) itemView.findViewById(R.id.full_history_date);
            this.orderFullDate.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            orderPrettyDate = (TextView) itemView.findViewById(R.id.pretty_history_date);
            this.orderPrettyDate.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemView.setOnClickListener(new View.OnClickListener(){
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

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }

    // An interface to Define click listener for the ViewHolder's View from any where.
    public interface ClickListener{
        public void itemClicked(View view, int position);

    }
}
