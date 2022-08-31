package moun.com.deli.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import moun.com.deli.R;
import moun.com.deli.adapter.ItemsOrderHistoryAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.database.OrdersDAO;
import moun.com.deli.model.Items;
import moun.com.deli.model.Orders;
import moun.com.deli.util.AppUtils;

/**
 * Custom dialog fragment that handle the list of items
 */
public class OrdersHistoryDialogFragment extends DialogFragment {
    public static final String ARG_ITEM_ID = "custom_dialog_fragment";
    private RecyclerView itemsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemsOrderHistoryAdapter itemsOrderHistoryAdapter;
    private TextView orderDate;
    private TextView orderTotal;
    private OrdersDAO ordersDAO;
    private ItemsDAO itemsDAO;
    private Orders orders;
    private GetOrdersHistoryTask task;
    private ArrayList<Items> itemsOrderList;

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);
    DateFormat fullDateFormat = DateFormat.getDateInstance(DateFormat.FULL);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersDAO = new OrdersDAO(getActivity());
        itemsDAO = new ItemsDAO(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        orders = bundle.getParcelable("selectedItem");
        Log.d("THE ORDER: ", orders.toString());

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_orders_history);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        itemsRecyclerView = (RecyclerView) dialog.findViewById(R.id.order_recyclerView);
        itemsRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        itemsRecyclerView.setLayoutManager(mLayoutManager);
        itemsOrderHistoryAdapter = new ItemsOrderHistoryAdapter(getActivity());

        orderDate = (TextView) dialog.findViewById(R.id.item_title);
        orderDate.setText(fullDateFormat.format(orders.getDate_created()));
        orderDate.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));

        orderTotal = (TextView) dialog.findViewById(R.id.history_order_total);
        orderTotal.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));

        task = new GetOrdersHistoryTask(getActivity());
        task.execute((Void) null);

        // Close
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }

        });

        return dialog;

    }

    public void getTotalPrice() {
        Double sum = 0.0;
        for (int i = 0; i < itemsOrderList.size(); i++) {
            Double itemPrice = Double.parseDouble(String.valueOf(itemsOrderList.get(i).getItemPrice()));
            sum += itemsOrderList.get(i).getItemQuantity() * itemPrice;
        }
        if (orderTotal != null) {
            orderTotal.setText("ORDER TOTAL: $" + Double.toString(sum));
        }

    }

    public class GetOrdersHistoryTask extends AsyncTask<Void, Void, ArrayList<Items>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetOrdersHistoryTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Items> doInBackground(Void... arg0) {
            ArrayList<Items> historyList = itemsDAO.getItemsOrderHistory(orders.getId());
            return historyList;
        }

        @Override
        protected void onPostExecute(ArrayList<Items> historyList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                Log.d("orders", historyList.toString());
                itemsOrderList = historyList;
                if (historyList != null) {
                    if (historyList.size() != 0) {
                        itemsOrderHistoryAdapter.setItemsList(historyList);
                        itemsRecyclerView.setAdapter(itemsOrderHistoryAdapter);
                        getTotalPrice();

                    }
                }

            }
        }
    }

}
