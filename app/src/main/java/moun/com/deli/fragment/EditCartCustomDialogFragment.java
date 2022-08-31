package moun.com.deli.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import moun.com.deli.MyCartActivity;
import moun.com.deli.R;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.Items;
import moun.com.deli.util.AppUtils;

/**
 * Custom Dialog Fragment that prompts the user to update the quantity for items.
 */
public class EditCartCustomDialogFragment extends DialogFragment {

    public static final String ARG_ITEM_ID = "custom_dialog_fragment";
    private TextView itemTitle;
    private TextView description;
    private TextView itemDescription;
    private TextView totalPrice;
    private Items cartItems;
    private Spinner qtySpinner;
    private ItemsDAO itemDAO;
    private UpdateItemTask task;


    /*
	 * Callback used to communicate with MyCartFragment to notify the list adapter.
	 * MyCartActivity implements this interface and communicates with MyCartFragment.
	 */
    public interface EditCartDialogFragmentListener {
        void onFinishDialog();
    }

    public EditCartCustomDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDAO = new ItemsDAO(getActivity());
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        cartItems = bundle.getParcelable("selectedItem");

        // The only reason you might override this method is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        // set the layout for the dialog
        dialog.setContentView(R.layout.fragment_custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);

        itemTitle = (TextView) dialog.findViewById(R.id.item_title);
        description = (TextView) dialog.findViewById(R.id.description_title);
        itemDescription = (TextView) dialog.findViewById(R.id.item_description);
        qtySpinner = (Spinner) dialog.findViewById(R.id.spinner_qty);
        totalPrice = (TextView) dialog.findViewById(R.id.total_price);
        setItemsData();
        qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int qty = (int) qtySpinner.getSelectedItem();
                totalPrice.setText(String.valueOf("$" + qty * cartItems.getItemPrice()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Add to cart
        dialog.findViewById(R.id.order_button).setVisibility(View.GONE);

        // Update
        dialog.findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getItemsData();
                task = new UpdateItemTask(getActivity());
                task.execute((Void) null);
                MyCartActivity myCartActivity = (MyCartActivity) getActivity();
                myCartActivity.onFinishDialog();
                dismiss();
            }

        });

        // Close
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // User cancelled the dialog
                dismiss();
            }

        });
        return dialog;

    }

    private void setItemsData(){
        Integer[] quantity = new Integer[] { 1, 2, 3, 4, 5, 6 };
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_item, quantity);
        qtySpinner.setAdapter(adapter);
        int position = adapter.getPosition(cartItems.getItemQuantity());
        if(cartItems != null){
            itemTitle.setText(cartItems.getItemName());
            itemTitle.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
            description.setText(getString(R.string.description));
            description.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
            itemDescription.setText(cartItems.getItemDescription());
            itemDescription.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOOK));
            qtySpinner.setSelection(position);
        }
    }

    private void getItemsData(){
        cartItems.setItemName(cartItems.getItemName());
        cartItems.setItemDescription(cartItems.getItemDescription());
        cartItems.setItemImage(cartItems.getItemImage());
        cartItems.setItemPrice(cartItems.getItemPrice());
        cartItems.setItemQuantity(Integer.parseInt(qtySpinner.getSelectedItem().toString()));
    }

    public class UpdateItemTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;

        public UpdateItemTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = itemDAO.updateItemsTable(cartItems);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1) {
                    AppUtils.CustomToast(activityWeakRef.get(), "Item Updated");
                    Log.d("ITEM: ", cartItems.toString());
                }
            }
        }
    }
}
