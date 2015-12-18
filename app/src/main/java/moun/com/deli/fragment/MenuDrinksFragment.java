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
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import moun.com.deli.R;
import moun.com.deli.adapter.MenuListAdapter;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/3/2015.
 */
public class MenuDrinksFragment extends Fragment implements MenuListAdapter.ClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuListAdapter menuListAdapter;
    ArrayList<MenuItems> listItems;
    private static final String ITEMS_STATE = "items_state";
    private AlphaInAnimationAdapter alphaAdapter;
    private ItemsDAO itemDAO;
    private AddItemTask task;
    private MenuItems menuItemsFavorite = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        itemDAO = new ItemsDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_items_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.sandwich_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (savedInstanceState != null) {
            // We will restore the state of data list when the activity is re-created
            listItems = savedInstanceState.getParcelableArrayList(ITEMS_STATE);
        } else {
            listItems = getDrinkMenuList();

        }
        menuListAdapter = new MenuListAdapter(getActivity(), listItems, inflater, R.layout.menu_list_single_row);
        mRecyclerView.setAdapter(menuListAdapter);
        menuListAdapter.setClickListener(this);

        return rootView;
    }

    // Before the activity is destroyed, onSaveInstanceState() gets called.
    // The onSaveInstanceState() method saves the list of data.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ITEMS_STATE, listItems);
    }

    @Override
    public void itemClicked(View view, int position, boolean isLongClick) {
        MenuItems menuItems = getDrinkMenuList().get(position);
        if (isLongClick) {
            if(itemDAO.getItemFavorite(menuItems.getItemName()) == null) {
                menuItemsFavorite = new MenuItems();
                menuItemsFavorite.setItemName(menuItems.getItemName());
                menuItemsFavorite.setItemDescription(menuItems.getItemDescription());
                menuItemsFavorite.setItemImage(menuItems.getItemImage());
                menuItemsFavorite.setItemPrice(menuItems.getItemPrice());
                task = new AddItemTask(getActivity());
                task.execute((Void) null);
                ImageView heart = (ImageView) view.findViewById(R.id.heart);
                heart.setImageResource(R.mipmap.ic_favorite_red_24dp);
            } else {
                AppUtils.CustomToast(getActivity(), getString(R.string.already_added_to_favorites));
            }

        } else {

            if (menuItems != null) {
                Bundle arguments = new Bundle();
                arguments.putParcelable("selectedItem", menuItems);
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.setArguments(arguments);
                customDialogFragment.show(getFragmentManager(),
                        CustomDialogFragment.ARG_ITEM_ID);
            }
        }


    }

    public class AddItemTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;

        public AddItemTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = itemDAO.saveToFavoriteTable(menuItemsFavorite);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    AppUtils.CustomToast(getActivity(), getString(R.string.added_to_favorites));
                Log.d("READ ITEM DATA FROM DB: ", menuItemsFavorite.toString());
            }
        }
    }

    private ArrayList<MenuItems> getDrinkMenuList(){

        ArrayList<MenuItems> menuItems = new ArrayList<MenuItems>();
        menuItems.add(new MenuItems(getString(R.string.orange), R.drawable.items1, 2.25, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.earl), R.drawable.items2, 1.50, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.iced_tead), R.drawable.items3, 3.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.coffee), R.drawable.items4, 3.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.hot_tea), R.drawable.items5, 2.25, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.bottle), R.drawable.items6, 2.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.water), R.drawable.items7, 1.00, getString(R.string.short_lorem)));


        return menuItems;
    }
}
