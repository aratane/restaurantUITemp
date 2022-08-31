package moun.com.deli.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import moun.com.deli.MyCartActivity;
import moun.com.deli.R;
import moun.com.deli.adapter.MyCartListAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.Items;
import moun.com.deli.util.AppUtils;
import moun.com.deli.util.SessionManager;

/**
 * This Fragment used to handle the list of items cart from items table using
 * {@link RecyclerView} with a {@link LinearLayoutManager}.
 */
public class MyCartFragment extends Fragment implements MyCartListAdapter.ButtonClickListener, View.OnClickListener {

    private static final String LOG_TAG = MyCartFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "cart_list";
    private RecyclerView cartRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyCartListAdapter myCartListAdapter;
    private ArrayList<Items> itemsCartList;
    private ItemsDAO itemsDAO;
    private GetItemsCartTask task;
    NumberOfItemChangedListener numberOfItemChangedListener;
    private TextView totalPrice;
    private Button checkoutBtn;
    private TextView emtyCart;
    MyCartCheckoutFragment myCartCheckoutFragment;
    private SessionManager session;


    /**
     * Callback used to communicate with MyCartFragment to display the number of items in cart.
     * MyCartActivity implements this interface and communicates with MyCartFragment.
     */
    public interface NumberOfItemChangedListener {
        void onNumberChanged();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            numberOfItemChangedListener = (NumberOfItemChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Listeners!!");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsDAO = new ItemsDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = (RecyclerView) rootView.findViewById(R.id.cart_recyclerView);
        cartRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        cartRecyclerView.setLayoutManager(mLayoutManager);
        myCartListAdapter = new MyCartListAdapter(getActivity());
        myCartListAdapter.setClickListener(this);
        emtyCart = (TextView) rootView.findViewById(R.id.empty);
        emtyCart.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        totalPrice = (TextView) rootView.findViewById(R.id.total_price);
        totalPrice.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        checkoutBtn = (Button) rootView.findViewById(R.id.checkout_button);
        checkoutBtn.setOnClickListener(this);

        // Use AsyncTask to get cart items from database
        task = new GetItemsCartTask(getActivity());
        task.execute((Void) null);

        session = new SessionManager(getActivity());

        return rootView;

    }

    @Override
    public void deleteClicked(View view, int position) {
        Items cartItems = (Items) itemsCartList.get(position);
        itemsDAO.deleteFromItemsTable(cartItems);
        myCartListAdapter.removeAt(position);
        numberOfItemChangedListener.onNumberChanged();
        MyCartActivity myCartActivity = (MyCartActivity) getActivity();
        myCartActivity.addItemsNumber();
        if (itemsCartList.size() == 0) {
            emtyCart.setVisibility(View.VISIBLE);
            totalPrice.setText("ORDER TOTAL: $" + Double.toString(0.0));
        }

    }

    @Override
    public void editClicked(View view, int position) {
        Items cartItems = (Items) itemsCartList.get(position);

        if (cartItems != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedItem", cartItems);
            EditCartCustomDialogFragment editCartCustomDialogFragment = new EditCartCustomDialogFragment();
            editCartCustomDialogFragment.setArguments(arguments);
            editCartCustomDialogFragment.show(getFragmentManager(),
                    editCartCustomDialogFragment.ARG_ITEM_ID);
        }

    }

    @Override
    public void onClick(View v) {

        // Check if cart is empty
        if (itemsCartList.size() == 0) {
            dialogMessage("Oops!", "Your cart is empty, Start your order now.");

        } else {
            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                MyCartActivity myCartActivity = (MyCartActivity) getActivity();
                myCartCheckoutFragment = new MyCartCheckoutFragment();
                myCartActivity.switchContent(myCartCheckoutFragment, MyCartCheckoutFragment.ARG_ITEM_ID);
            } else {
                dialogMessage("Oops!", "You must to sign in to your account before making orders.");

            }
        }

    }

    /**
     * get items cart from items table asynchronously.
     */
    public class GetItemsCartTask extends AsyncTask<Void, Void, ArrayList<Items>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetItemsCartTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Items> doInBackground(Void... arg0) {
            ArrayList<Items> itemsList = itemsDAO.getCartItemsNotOrdered();
            return itemsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Items> cartList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                Log.d("items", cartList.toString());
                itemsCartList = cartList;
                if (cartList != null) {
                    if (cartList.size() != 0) {
                        emtyCart.setVisibility(View.GONE);
                        myCartListAdapter.setItemsList(cartList);
                        cartRecyclerView.setAdapter(myCartListAdapter);
                        getTotalPrice();

                    } else {
                        emtyCart.setVisibility(View.VISIBLE);

                    }
                }

            }
        }
    }

    /*
 * This method is invoked from MyCartActitvity onFinishDialog() method. It is
 * called from EditCartCustomDialogFragment when an item record is updated.
 * This is used for communicating between fragments.
 */
    public void updateView() {
        task = new GetItemsCartTask(getActivity());
        task.execute((Void) null);
    }

    /**
     * Count the total price of all items and display the number on screen.
     */
    public void getTotalPrice() {
        Double sum = 0.0;
        for (int i = 0; i < itemsCartList.size(); i++) {
            Double itemPrice = Double.parseDouble(String.valueOf(itemsCartList.get(i).getItemPrice()));
            sum += itemsCartList.get(i).getItemQuantity() * itemPrice;
        }
        if (totalPrice != null) {
            totalPrice.setText("TOTAL PRICE: $" + Double.toString(sum));
        }

    }


    /**
     * Custom dialog fragment using SimpleDialogFragment library
     *
     * @param title   title of the message.
     * @param message the message you want to display.
     */
    private void dialogMessage(String title, String message) {
        SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonText(getString(R.string.ok))
                .setCancelable(false)
                .show();
    }
}
