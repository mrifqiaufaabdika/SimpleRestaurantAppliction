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
public class MenuPizzaFragment extends Fragment implements MenuListAdapter.ClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuListAdapter menuListAdapter;
    List<MenuItems> rowListItem;
    private AlphaInAnimationAdapter alphaAdapter;
    private ItemsDAO itemDAO;
    private AddItemTask task;
    private MenuItems menuItemsFavorite = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rowListItem = getPizzaMenuList();
        itemDAO = new ItemsDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_items_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.sandwich_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        menuListAdapter = new MenuListAdapter(getActivity(), rowListItem, inflater, R.layout.menu_list_single_row);
        alphaAdapter = new AlphaInAnimationAdapter(menuListAdapter);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        menuListAdapter.setClickListener(this);

        return rootView;
    }

    @Override
    public void itemClicked(View view, int position, boolean isLongClick) {
        MenuItems menuItems = getPizzaMenuList().get(position);
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

    private List<MenuItems> getPizzaMenuList(){

        List<MenuItems> menuItems = new ArrayList<MenuItems>();
        menuItems.add(new MenuItems(getString(R.string.cheeze), R.drawable.items1, 11.50, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.margherita), R.drawable.items2, 12.25, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.vegetarian), R.drawable.items3, 10.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.supteme), R.drawable.items4, 15.50, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.pepperoni), R.drawable.items5, 13.20, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.bbq), R.drawable.items6, 16.75, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.hot), R.drawable.items7, 14.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.greek), R.drawable.items8, 18.50, getString(R.string.short_lorem)));

        return menuItems;
    }
}
