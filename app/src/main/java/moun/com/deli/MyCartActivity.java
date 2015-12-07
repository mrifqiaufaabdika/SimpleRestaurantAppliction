package moun.com.deli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import moun.com.deli.fragment.EditCartCustomDialogFragment;
import moun.com.deli.fragment.MainFragment;
import moun.com.deli.fragment.MyCartCheckoutFragment;
import moun.com.deli.fragment.MyCartFragment;

/**
 * Created by Mounzer on 12/6/2015.
 */
public class MyCartActivity extends AppCompatActivity implements EditCartCustomDialogFragment.EditCartDialogFragmentListener{

    private Toolbar mToolbar;
    private Fragment contentFragment;
    private MyCartFragment myCartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_cart);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("content")) {
                String content = savedInstanceState.getString("content");
                if (content.equals(MyCartFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(MyCartFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(MyCartFragment.ARG_ITEM_ID);
                    }
                }
                if (content.equals(MyCartCheckoutFragment.ARG_ITEM_ID)) {
                    if (fragmentManager
                            .findFragmentByTag(MyCartCheckoutFragment.ARG_ITEM_ID) != null) {

                        contentFragment = fragmentManager
                                .findFragmentByTag(MyCartCheckoutFragment.ARG_ITEM_ID);
                    }
                }

            }


        } else {
            myCartFragment = new MyCartFragment();
            switchContent(myCartFragment, MyCartFragment.ARG_ITEM_ID);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof MyCartFragment) {
            outState.putString("content", MyCartFragment.ARG_ITEM_ID);
        } else if (contentFragment instanceof MyCartCheckoutFragment){
            outState.putString("content", MyCartCheckoutFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate());

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.add(R.id.content_fragment_cart, fragment, tag);

            if (!(fragment instanceof MyCartFragment)) {
                transaction.addToBackStack(tag);
            }

            transaction.commit();
            contentFragment = fragment;
        }
    }


    /*
     * Callback used to communicate with MyCartFragment to notify the list adapter.
     * Communication between fragments goes via their Activity class.
     */
    @Override
    public void onFinishDialog() {
        if (myCartFragment != null) {
            myCartFragment.updateView();
        }

    }
}
