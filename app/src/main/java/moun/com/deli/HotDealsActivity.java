package moun.com.deli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import moun.com.deli.fragment.HotDealsDetailFragment;
import moun.com.deli.fragment.HotDealsListFragment;
import moun.com.deli.fragment.MyCartCheckoutFragment;
import moun.com.deli.fragment.MyCartFragment;

/**
 * Created by Mounzer on 12/9/2015.
 */
public class HotDealsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Fragment contentFragment;
    private HotDealsListFragment hotDealsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_deals);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("content")) {
                String content = savedInstanceState.getString("content");
                if (content.equals(HotDealsListFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(HotDealsListFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(HotDealsListFragment.ARG_ITEM_ID);
                    }
                }
                if (content.equals(HotDealsDetailFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(HotDealsDetailFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(HotDealsDetailFragment.ARG_ITEM_ID);
                    }
                }

            }


        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hotDealsListFragment = new HotDealsListFragment();
            transaction.replace(R.id.hot_content_fragment, hotDealsListFragment, HotDealsListFragment.ARG_ITEM_ID);
            transaction.commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof HotDealsListFragment) {
            outState.putString("content", HotDealsListFragment.ARG_ITEM_ID);
        } else if (contentFragment instanceof HotDealsDetailFragment){
            outState.putString("content", HotDealsDetailFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }


}
