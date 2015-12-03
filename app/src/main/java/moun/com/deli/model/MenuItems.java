package moun.com.deli.model;

import java.util.ArrayList;
import java.util.List;

import moun.com.deli.R;

/**
 * Created by Mounzer on 12/1/2015.
 */
public class MenuItems {
/**
    public int resourceId;
    public int titleId;

    public MenuItems(int resourceId, int titleId) {
        this.resourceId = resourceId;
        this.titleId = titleId;
    }

    public static final MenuItems[] ITEMS = {
            new MenuItems(R.drawable.menu_items01, R.string.breakfast),
            new MenuItems(R.drawable.menu_items02, R.string.breakfast),
            new MenuItems(R.drawable.menu_items03, R.string.breakfast),
            new MenuItems(R.drawable.menu_items04, R.string.breakfast),
            new MenuItems(R.drawable.menu_items05, R.string.breakfast),
            new MenuItems(R.drawable.menu_items06, R.string.breakfast),
            new MenuItems(R.drawable.menu_items07, R.string.breakfast),
            new MenuItems(R.drawable.menu_items08, R.string.breakfast),

    };
*/
    private int wonderImage;
    private String wonderName;


    public MenuItems(String wonderName, int wonderImage) {
        this.wonderName = wonderName;
        this.wonderImage = wonderImage;
    }

    public int getWonderImage() {
        return wonderImage;
    }

    public void setWonderImage(int wonderImage) {
        this.wonderImage = wonderImage;
    }

    public String getWonderName() {
        return wonderName;
    }

    public void setWonderName(String wonderName) {
        this.wonderName = wonderName;
    }



}
