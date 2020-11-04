package com.umedic.pharm.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.umedic.pharm.Interface.ItemClickListener;
import com.umedic.pharm.R;

public class CartView extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView textItemName, textItemPrice, textItemQuantity;
    private ItemClickListener itemClickListener;

    public CartView(View itemView)
    {
        super(itemView);

        textItemName = itemView.findViewById(R.id.cart_item_name);
        textItemPrice = itemView.findViewById(R.id.cart_item_price);
        textItemQuantity = itemView.findViewById(R.id.cart_item_quantity);
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
