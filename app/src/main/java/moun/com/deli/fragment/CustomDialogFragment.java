package moun.com.deli.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import moun.com.deli.R;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.database.OrdersDAO;
import moun.com.deli.model.Cart;
import moun.com.deli.model.MenuItems;
import moun.com.deli.model.Orders;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/4/2015.
 */
public class CustomDialogFragment extends DialogFragment {

    public static final String ARG_ITEM_ID = "custom_dialog_fragment";
    private TextView itemTitle;
    private TextView description;
    private TextView itemDescription;
    private TextView totalPrice;
    private MenuItems menuItems;
    private Cart menuItemsCart;
    private Spinner qtySpinner;
    private ItemsDAO itemDAO;
    private OrdersDAO ordersDAO;
    private Orders orders;
    private AddItemTask task;
    private AddOrderTask orderTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDAO = new ItemsDAO(getActivity());
        ordersDAO = new OrdersDAO(getActivity());
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        menuItems = bundle.getParcelable("selectedItem");

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


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
                totalPrice.setText(String.valueOf("$" + qty * menuItems.getItemPrice()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                qtySpinner.setSelection(0);
            }
        });
        if(ordersDAO.getOrder(0) == null) {
            orders = new Orders();
            orders.setOrdered(false);
            orders.setDate_created(System.currentTimeMillis());
            orderTask = new AddOrderTask(getActivity());
            orderTask.execute((Void) null);
        }




        // Add to cart
        dialog.findViewById(R.id.order_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getItemsData();
                task = new AddItemTask(getActivity());
                task.execute((Void) null);
                dismiss();
            }

        });

        // Update
        dialog.findViewById(R.id.update_button).setVisibility(View.GONE);

        // Close
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }

        });
        return dialog;

    }

    private void setItemsData(){
        if(menuItems != null){
            itemTitle.setText(menuItems.getItemName());
            itemTitle.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
            description.setText(getString(R.string.description));
            description.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
            itemDescription.setText(menuItems.getItemDescription());
            itemDescription.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOOK));
            Integer[] quantity = new Integer[] { 1, 2, 3, 4, 5, 6 };
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),
                    android.R.layout.simple_spinner_item, quantity);
            qtySpinner.setAdapter(adapter);
        }
    }

    private void getItemsData(){
        menuItemsCart = new Cart();
        menuItemsCart.setItemName(menuItems.getItemName());
        menuItemsCart.setItemDescription(menuItems.getItemDescription());
        menuItemsCart.setItemImage(menuItems.getItemImage());
        menuItemsCart.setItemPrice(menuItems.getItemPrice());
        menuItemsCart.setItemQuantity(Integer.parseInt(qtySpinner.getSelectedItem().toString()));



            Orders orders = (Orders) ordersDAO.getOrder(0);
            menuItemsCart.setOrders(orders);
            Log.d("Already order Added: ", orders.toString());
            Log.d("THE ORDERS: ", ordersDAO.getOrders().toString());





    }

    public class AddItemTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;

        public AddItemTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = itemDAO.saveToCartTable(menuItemsCart);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    AppUtils.CustomToast(activityWeakRef.get(), "Added to cart");
                Log.d("ITEMS Cart: ", menuItemsCart.toString());
            }
        }
    }

    public class AddOrderTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;

        public AddOrderTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = ordersDAO.saveOrder(orders);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    AppUtils.CustomToast(activityWeakRef.get(), "Added to order");
                Log.d("ORDERS: ", ordersDAO.getOrders().toString());
            }
        }
    }
}
