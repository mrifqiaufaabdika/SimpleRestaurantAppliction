package moun.com.deli.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import moun.com.deli.R;

/**
 * Created by Mounzer on 12/3/2015.
 */
public class AppUtils {

    public static final String FONT_BOOK = "bebas_beue_book.otf";
    public static final String FONT_BOLD = "bebas_beue_bold.otf";



    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    // A custom toast with customized shape including an image
    public static void CustomToast(Activity context, String text){
        LayoutInflater inflater = context.getLayoutInflater();
        View layouttoast = inflater.inflate(R.layout.custom_toast, (ViewGroup)context.findViewById(R.id.toast_custom));
        ((TextView) layouttoast.findViewById(R.id.text_toast)).setText(text);
        Toast mytoast = new Toast(context.getBaseContext());
        mytoast.setView(layouttoast);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.show();
    }
}
