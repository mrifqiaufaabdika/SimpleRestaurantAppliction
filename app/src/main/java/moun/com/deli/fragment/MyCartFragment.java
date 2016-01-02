package moun.com.deli.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import moun.com.deli.MyCartActivity;
import moun.com.deli.R;
import moun.com.deli.adapter.MyCartListAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.Cart;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;
import moun.com.deli.util.SessionManager;

/**
 * Created by Mounzer on 12/6/2015.
 */
public class MyCartFragment extends Fragment implements MyCartListAdapter.ButtonClickListener, View.OnClickListener {

    private static final String LOG_TAG = MyCartFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "cart_list";
    private RecyclerView cartRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyCartListAdapter myCartListAdapter;
    private ArrayList<Cart> itemsCartList;
    private ItemsDAO itemsDAO;
    private GetItemsCartTask task;
    NumberOfItemChangedListener numberOfItemChangedListener;
    private TextView totalPrice;
    private Button checkoutBtn;
    private TextView emtyCart;
    MyCartCheckoutFragment myCartCheckoutFragment;
    private SessionManager session;




    public interface NumberOfItemChangedListener{
        void onNumberChanged();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            numberOfItemChangedListener = (NumberOfItemChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
        Cart cartItems = (Cart) itemsCartList.get(position);
        itemsDAO.deleteFromCart(cartItems);
        myCartListAdapter.removeAt(position);
        numberOfItemChangedListener.onNumberChanged();
        MyCartActivity myCartActivity = (MyCartActivity) getActivity();
        myCartActivity.addItemsNumber();
        if(itemsCartList.size() == 0){
            emtyCart.setVisibility(View.VISIBLE);
            totalPrice.setText("ORDER TOTAL: $" + Double.toString(0.0));
        }

    }

    @Override
    public void editClicked(View view, int position) {
        Cart cartItems = (Cart) itemsCartList.get(position);

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
        if(itemsCartList.size() == 0){
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

    public class GetItemsCartTask extends AsyncTask<Void, Void, ArrayList<Cart>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetItemsCartTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Cart> doInBackground(Void... arg0) {
            ArrayList<Cart> itemsList = itemsDAO.getCartItemsNotOrdered();
            return itemsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Cart> cartList) {
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

    // Custom dialog fragment using SimpleDialogFragment library
    private void dialogMessage(String title, String message){
        SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonText(getString(R.string.ok))
                .setCancelable(false)
                .show();
    }
}
