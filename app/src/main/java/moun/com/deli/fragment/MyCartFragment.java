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

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import moun.com.deli.MyCartActivity;
import moun.com.deli.R;
import moun.com.deli.adapter.MyCartListAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/6/2015.
 */
public class MyCartFragment extends Fragment implements MyCartListAdapter.ButtonClickListener, View.OnClickListener {

    private static final String LOG_TAG = MyCartFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "cart_list";
    private RecyclerView cartRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyCartListAdapter myCartListAdapter;
    private ArrayList<MenuItems> itemsCartList;
    private ItemsDAO itemsDAO;
    private GetItemsCartTask task;
    NumberOfItemChangedListener numberOfItemChangedListener;
    private TextView totalPrice;
    private Button checkoutBtn;
    private TextView emtyCart;
    MyCartCheckoutFragment myCartCheckoutFragment;




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

        task = new GetItemsCartTask(getActivity());
        task.execute((Void) null);

        return rootView;

    }

    @Override
    public void deleteClicked(View view, int position) {
        MenuItems menuItems = (MenuItems) itemsCartList.get(position);
        itemsDAO.deleteFromCart(menuItems);
        myCartListAdapter.removeAt(position);
        numberOfItemChangedListener.onNumberChanged();
        MyCartActivity myCartActivity = (MyCartActivity) getActivity();
        myCartActivity.addItemsNumber();
        if(itemsCartList.size() == 0){
            emtyCart.setVisibility(View.VISIBLE);
            totalPrice.setText("TOTAL PRICE: $" + Double.toString(0.0));
        }

    }

    @Override
    public void editClicked(View view, int position) {
        MenuItems menuItems = (MenuItems) itemsCartList.get(position);

        if (menuItems != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedItem", menuItems);
            EditCartCustomDialogFragment editCartCustomDialogFragment = new EditCartCustomDialogFragment();
            editCartCustomDialogFragment.setArguments(arguments);
            editCartCustomDialogFragment.show(getFragmentManager(),
                    editCartCustomDialogFragment.ARG_ITEM_ID);
        }

    }

    @Override
    public void onClick(View v) {
        MyCartActivity myCartActivity = (MyCartActivity) getActivity();
        myCartCheckoutFragment = new MyCartCheckoutFragment();
        myCartActivity.switchContent(myCartCheckoutFragment, MyCartCheckoutFragment.ARG_ITEM_ID);

    }

    public class GetItemsCartTask extends AsyncTask<Void, Void, ArrayList<MenuItems>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetItemsCartTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<MenuItems> doInBackground(Void... arg0) {
            ArrayList<MenuItems> itemsList = itemsDAO.getCartItems();
            return itemsList;
        }

        @Override
        protected void onPostExecute(ArrayList<MenuItems> cartList) {
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
}
