package moun.com.deli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import moun.com.deli.R;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/9/2015.
 */
public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder>{
    private static final String LOG_TAG = FavoritesListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<MenuItems> itemsList;
    private ClickListener clickListener;

    public FavoritesListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

    }

    public void setItemsList(ArrayList<MenuItems> itemsList) {
        this.itemsList = itemsList;
        notifyItemInserted(itemsList.size());

    }


    public void removeAt(int position) {
        itemsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemsList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.favorites_single_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MenuItems menuItems = itemsList.get(position);
        viewHolder.itemTitle.setText(menuItems.getItemName());
        viewHolder.itemPrice.setText("$" + menuItems.getItemPrice());
        viewHolder.itemImage.setImageResource(menuItems.getItemImage());



    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemPrice;
        public ImageView itemImage;



        public ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.favorite_item_title);
            this.itemTitle.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            itemPrice = (TextView) itemView.findViewById(R.id.favorite_item_price);
            this.itemPrice.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemImage = (ImageView) itemView.findViewById(R.id.favorite_item_image);

            itemView.findViewById(R.id.favorite_item_detail).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemClicked(itemView, getAdapterPosition());

                    }

                }
            });

            itemView.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.itemdeleted(itemView, getAdapterPosition());

                    }

                }
            });

        }

    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
        public void itemdeleted(View view, int position);
    }
}
