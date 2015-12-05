package moun.com.deli.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Mounzer on 12/3/2015.
 */
public class AppUtils {

    public static final String FONT_BOOK = "bebas_beue_book.otf";
    public static final String FONT_BOLD = "bebas_beue_bold.otf";


    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
