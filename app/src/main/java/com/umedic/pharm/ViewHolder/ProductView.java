package com.umedic.pharm.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.umedic.pharm.R;
import com.umedic.pharm.Interface.ItemClickListener;

public class ProductView extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView textProductName, textProductDesc, textProductPrice;
    public ImageView productImg;

    public ItemClickListener listener;

    public ProductView (View itemView)
    {
        super(itemView);

        textProductName = itemView.findViewById(R.id.product_name);
        textProductDesc = itemView.findViewById(R.id.product_description);
        textProductPrice = itemView.findViewById(R.id.product_price);

        productImg = itemView.findViewById(R.id.product_image);
    }

    public void setItemClickListener (ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
