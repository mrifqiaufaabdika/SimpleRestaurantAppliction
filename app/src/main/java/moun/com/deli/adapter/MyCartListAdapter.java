package moun.com.deli.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import moun.com.deli.R;
import moun.com.deli.model.Cart;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/6/2015.
 */
public class MyCartListAdapter extends RecyclerView.Adapter<MyCartListAdapter.ViewHolder>{
    private static final String LOG_TAG = MyCartListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<Cart> itemsList;
    private ButtonClickListener clickListener;

    public MyCartListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

    }

    public void setItemsList(ArrayList<Cart> itemsList) {
        this.itemsList = itemsList;
        //    notifyDataSetChanged();
        notifyItemInserted(itemsList.size());

    }

    public void removeAt(int position) {
        itemsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemsList.size());
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.cart_single_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Cart cartItems = itemsList.get(position);
        viewHolder.itemTitle.setText(cartItems.getItemName());
        viewHolder.itemQuantity.setText("Quantity: " + cartItems.getItemQuantity());
        viewHolder.itemTotal.setText("Total: $" + cartItems.getItemPrice() * cartItems.getItemQuantity());
        viewHolder.itemImage.setImageResource(cartItems.getItemImage());

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemQuantity;
        public TextView itemTotal;
        public ImageButton itemDelete;
        public ImageButton itemEdite;
        public ImageView itemImage;



        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.cart_item_title);
            this.itemTitle.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOLD));
            itemQuantity = (TextView) itemView.findViewById(R.id.cart_item_qty);
            this.itemQuantity.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemTotal = (TextView) itemView.findViewById(R.id.cart_item_total);
            this.itemTotal.setTypeface(AppUtils.getTypeface(itemView.getContext(), AppUtils.FONT_BOOK));
            itemDelete = (ImageButton) itemView.findViewById(R.id.cart_delete_btn);
            itemEdite = (ImageButton) itemView.findViewById(R.id.cart_edit_btn);
            itemImage = (ImageView) itemView.findViewById(R.id.cart_item_image);




            itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.deleteClicked(v, getAdapterPosition());
                    }
                }


            });
            itemEdite.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.editClicked(v, getAdapterPosition());
                    }
                }
            });

        }

    }

    public void setClickListener(ButtonClickListener clickListener){
        this.clickListener = clickListener;

    }

    public interface ButtonClickListener{
        public void deleteClicked(View view, int position);
        public void editClicked(View view, int position);
    }
}
