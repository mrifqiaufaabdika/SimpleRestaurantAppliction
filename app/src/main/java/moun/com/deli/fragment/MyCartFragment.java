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
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import moun.com.deli.R;
import moun.com.deli.adapter.MyCartListAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;

/**
 * Created by Mounzer on 12/6/2015.
 */
public class MyCartFragment extends Fragment implements MyCartListAdapter.ButtonClickListener{

    private static final String LOG_TAG = MyCartFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "cart_list";
    private RecyclerView cartRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyCartListAdapter myCartListAdapter;
    private ArrayList<MenuItems> itemsCartList;
    private ItemsDAO itemsDAO;
    private GetItemsCartTask task;


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

        task = new GetItemsCartTask(getActivity());
        task.execute((Void) null);

        return rootView;

    }

    @Override
    public void deleteClicked(View view, int position) {
        MenuItems menuItems = (MenuItems) itemsCartList.get(position);
        itemsDAO.deleteFromCart(menuItems);
        myCartListAdapter.removeAt(position);

    }

    @Override
    public void editClicked(View view, int position) {

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

                        myCartListAdapter.setItemsList(cartList);
                        cartRecyclerView.setAdapter(myCartListAdapter);

                    } else {

                    }
                }

            }
        }
    }
}
