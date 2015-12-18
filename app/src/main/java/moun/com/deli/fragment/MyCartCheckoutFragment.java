package moun.com.deli.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;

import moun.com.deli.R;
import moun.com.deli.database.UserDAO;
import moun.com.deli.model.User;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/6/2015.
 */
public class MyCartCheckoutFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = MyCartCheckoutFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "cart_checkout";
    private EditText userEmailCheckout;
    private EditText userAddressCheckout;
    private EditText userPhoneCheckout;
    private EditText userExtraInfo;
    private TextView userEmail;
    private TextView userAddress;
    private TextView userPhone;
    private TextView info;
    private Button orderNowBtn;
    private UserDAO userDAO;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        userEmailCheckout = (EditText) rootView.findViewById(R.id.checkout_user_email);
        userAddressCheckout = (EditText) rootView.findViewById(R.id.checkout_shipping_address);
        userPhoneCheckout = (EditText) rootView.findViewById(R.id.checkout_user_phone);
        userExtraInfo = (EditText) rootView.findViewById(R.id.checkout_info_notes);
        userEmail = (TextView) rootView.findViewById(R.id.checkout_email);
        userAddress = (TextView) rootView.findViewById(R.id.checkout_address);
        userPhone = (TextView) rootView.findViewById(R.id.checkout_phone);
        info = (TextView) rootView.findViewById(R.id.checkout_notes);
        orderNowBtn = (Button) rootView.findViewById(R.id.order_now);
        userEmail.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        userAddress.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        userPhone.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        info.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
        // user informations will be fetched from SQLite database and displaying it on the screen
        userDAO = new UserDAO(getActivity());
        user = userDAO.getUserDetails();
        userEmailCheckout.setText(user.getEmail());
        userAddressCheckout.setText(user.getAddress());
        userPhoneCheckout.setText(user.getPhone());

        orderNowBtn.setOnClickListener(this);



        return rootView;

    }


    @Override
    public void onClick(View v) {
        dialogMessage("Congrats!", "We'll send you an email just your order will be shipped." );

    }

    // Custom dialog fragment using SimpleDialogFragment library
    private void dialogMessage(String title, String message){
        SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonText(getString(R.string.ok))
                .setCancelable(false)
                .show();
    }
}
