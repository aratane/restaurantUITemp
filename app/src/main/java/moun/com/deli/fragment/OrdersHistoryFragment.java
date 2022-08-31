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
import moun.com.deli.adapter.OrdersHistoryAdapter;
import moun.com.deli.database.OrdersDAO;
import moun.com.deli.model.Orders;
import moun.com.deli.util.AppUtils;

/**
 * This Fragment used to handle the list of history orders fetched from SQLite database
 * (orders table) using {@link RecyclerView} with a {@link LinearLayoutManager}.
 */
public class OrdersHistoryFragment extends Fragment implements OrdersHistoryAdapter.ClickListener{
    private static final String LOG_TAG = OrdersHistoryFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "history_list";
    private RecyclerView historyRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrdersHistoryAdapter ordersHistoryAdapter;
    private OrdersDAO ordersDAO;
    private GetOrdersHistoryTask task;
    private ArrayList<Orders> ordersHistoryList;
    private TextView emtyHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersDAO = new OrdersDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders_history, container, false);

        historyRecyclerView = (RecyclerView) rootView.findViewById(R.id.history_recyclerView);
        emtyHistory = (TextView) rootView.findViewById(R.id.empty);
        emtyHistory.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        historyRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        historyRecyclerView.setLayoutManager(mLayoutManager);
        ordersHistoryAdapter = new OrdersHistoryAdapter(getActivity());
        ordersHistoryAdapter.setClickListener(this);

        task = new GetOrdersHistoryTask(getActivity());
        task.execute((Void) null);

        return rootView;

    }

    @Override
    public void itemClicked(View view, int position) {
        Orders orders = ordersHistoryList.get(position);
        if (orders != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedItem", orders);
            OrdersHistoryDialogFragment ordersHistoryDialogFragment = new OrdersHistoryDialogFragment();
            ordersHistoryDialogFragment.setArguments(arguments);
            ordersHistoryDialogFragment.show(getFragmentManager(),
                    OrdersHistoryDialogFragment.ARG_ITEM_ID);
        }
    }

    public class GetOrdersHistoryTask extends AsyncTask<Void, Void, ArrayList<Orders>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetOrdersHistoryTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Orders> doInBackground(Void... arg0) {
            ArrayList<Orders> historyList = ordersDAO.getOrders();
            return historyList;
        }

        @Override
        protected void onPostExecute(ArrayList<Orders> historyList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                Log.d("orders", historyList.toString());
                ordersHistoryList = historyList;
                if (historyList != null) {
                    if (historyList.size() != 0) {
                        emtyHistory.setVisibility(View.GONE);
                        ordersHistoryAdapter.setOrdersList(historyList, historyList.size());
                        historyRecyclerView.setAdapter(ordersHistoryAdapter);

                    } else {
                        emtyHistory.setVisibility(View.VISIBLE);

                    }
                }

            }
        }
    }
}
