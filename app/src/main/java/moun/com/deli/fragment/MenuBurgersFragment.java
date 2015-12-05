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
public class MenuBurgersFragment extends Fragment implements MenuListAdapter.ClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuListAdapter menuListAdapter;
    List<MenuItems> rowListItem;
    private AlphaInAnimationAdapter alphaAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rowListItem = getBurgerMenuList();
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
            MenuItems menuItems = getBurgerMenuList().get(position);
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

    private List<MenuItems> getBurgerMenuList(){

        List<MenuItems> menuItems = new ArrayList<MenuItems>();
        menuItems.add(new MenuItems(getString(R.string.major), R.drawable.items1, 6.25, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.fair), R.drawable.items2, 7.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.jack), R.drawable.items3, 10.50, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.black), R.drawable.items4, 8.50, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.endeavor), R.drawable.items5, 9.30, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.mexican), R.drawable.items6, 11.00, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.pepperjack), R.drawable.items7, 9.50, getString(R.string.short_lorem)));
        menuItems.add(new MenuItems(getString(R.string.texas), R.drawable.items8, 8.00, getString(R.string.short_lorem)));

        return menuItems;
    }


}
