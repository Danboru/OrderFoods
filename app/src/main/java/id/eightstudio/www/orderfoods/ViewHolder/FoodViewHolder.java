package id.eightstudio.www.orderfoods.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.eightstudio.www.orderfoods.Interface.ItemClickListener;
import id.eightstudio.www.orderfoods.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView food_image;
    public TextView food_name;
    ItemClickListener onClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_image = itemView.findViewById(R.id.food_image);
        food_name = itemView.findViewById(R.id.food_name);

        itemView.setOnClickListener(this);

    }

    public void setOnClickListener(ItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        onClickListener.onClick(v, getAdapterPosition(), false);
    }

}
