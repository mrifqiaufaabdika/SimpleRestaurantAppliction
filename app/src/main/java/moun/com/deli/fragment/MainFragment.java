package moun.com.deli.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import moun.com.deli.R;
import moun.com.deli.adapter.HomeMenuCustomAdapter;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/1/2015.
 */
public class MainFragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 3;
    private HomeMenuCustomAdapter homeMenuCustomAdapter;
    List<MenuItems> rowListItem;
    private boolean mLinearShown;
    LayoutInflater inflater;
    private SliderLayout mImageSlider;
    private TextView startOrder;
    private AlphaInAnimationAdapter alphaAdapter;



    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rowListItem = getMenuList();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.inflater = inflater;

        // Image Slider: Initialize and set Functionality
        mImageSlider = (SliderLayout)rootView.findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.items1);
        file_maps.put("Big Bang Theory", R.drawable.items2);
        file_maps.put("House of Cards", R.drawable.items3);
        file_maps.put("Game of Thrones", R.drawable.items4);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mImageSlider.addSlider(textSliderView);
        }
        mImageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mImageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mImageSlider.setCustomAnimation(new DescriptionAnimation());
        mImageSlider.setDuration(4000);
        mImageSlider.addOnPageChangeListener(this);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    //    mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        startOrder = (TextView) rootView.findViewById(R.id.start_order_text);
        startOrder.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        homeMenuCustomAdapter = new HomeMenuCustomAdapter(getActivity(), rowListItem, inflater, R.layout.grid_layout_row);
        alphaAdapter = new AlphaInAnimationAdapter(homeMenuCustomAdapter);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return rootView;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, we call stopAutoCycle() on the slider before activity or fragment is destroyed
        mImageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu_transition, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // We change the look of the icon every time the user toggles between list and grid.
        MenuItem item = menu.findItem(R.id.action_toggle);
    //    item.setIcon(mLogShown ? R.mipmap.ic_grid_on_white_24dp : R.mipmap.ic_view_list_white_24dp);
        /**
        if (null != item) {
            if (LayoutManagerType.GRID_LAYOUT_MANAGER != null) {
                item.setIcon(R.mipmap.ic_grid_on_white_24dp);
                item.setTitle(R.string.show_as_grid);
            } else if (LayoutManagerType.LINEAR_LAYOUT_MANAGER != null){
                item.setIcon(R.mipmap.ic_view_list_white_24dp);
                item.setTitle(R.string.show_as_list);
            }
        }
         */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // We change the look of the icon every time the user toggles between list and grid.
        switch (item.getItemId()) {
            case R.id.action_toggle: {
                mLinearShown = !mLinearShown;
                if (mLinearShown) {
                    setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
                    homeMenuCustomAdapter = new HomeMenuCustomAdapter(getActivity(), rowListItem, inflater, R.layout.linear_layout_row);
                    alphaAdapter = new AlphaInAnimationAdapter(homeMenuCustomAdapter);
                    mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    item.setIcon(R.mipmap.ic_grid_on_white_24dp);

                } else {
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
                    homeMenuCustomAdapter = new HomeMenuCustomAdapter(getActivity(), rowListItem, inflater, R.layout.grid_layout_row);
                    alphaAdapter = new AlphaInAnimationAdapter(homeMenuCustomAdapter);
                    mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    item.setIcon(R.mipmap.ic_view_list_white_24dp);

                }

                return true;
            }
        }
        return false;
    }



    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private List<MenuItems> getMenuList(){

        List<MenuItems> menuItems = new ArrayList<MenuItems>();
        menuItems.add(new MenuItems(getString(R.string.breakfast), R.drawable.items1));
        menuItems.add(new MenuItems(getString(R.string.sandwich), R.drawable.items2));
        menuItems.add(new MenuItems(getString(R.string.burgers), R.drawable.items3));
        menuItems.add(new MenuItems(getString(R.string.pizza), R.drawable.items4));
        menuItems.add(new MenuItems(getString(R.string.salads), R.drawable.items5));
        menuItems.add(new MenuItems(getString(R.string.drinks), R.drawable.items6));
        menuItems.add(new MenuItems(getString(R.string.sweets), R.drawable.items7));
        menuItems.add(new MenuItems(getString(R.string.hot_deals), R.drawable.items8));

        return menuItems;
    }
}
