package moun.com.deli.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import moun.com.deli.R;
import moun.com.deli.adapter.HomeMenuCustomAdapter;
import moun.com.deli.adapter.MenuListAdapter;
import moun.com.deli.model.MenuItems;

/**
 * Created by Mounzer on 12/3/2015.
 */
public class MenuDrinksFragment extends Fragment implements MenuListAdapter.ClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuListAdapter menuListAdapter;
    List<MenuItems> rowListItem;
    private AlphaInAnimationAdapter alphaAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rowListItem = getDrinkMenuList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_sandwich, container, false);

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
        if (isLongClick) {

        } else {
            MenuItems menuItems = getDrinkMenuList().get(position);
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

    private List<MenuItems> getDrinkMenuList(){

        List<MenuItems> menuItems = new ArrayList<MenuItems>();
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
