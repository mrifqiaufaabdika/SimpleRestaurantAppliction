package moun.com.deli.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moun.com.deli.R;

/**
 * Created by Mounzer on 12/6/2015.
 */
public class MyCartCheckoutFragment extends Fragment {
    private static final String LOG_TAG = MyCartCheckoutFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "cart_checkout";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        return rootView;

    }
}
