package id.eightstudio.www.orderfoods.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.eightstudio.www.orderfoods.Interface.OnClickListener;
import id.eightstudio.www.orderfoods.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;
    public TextView txtMenuName;
    OnClickListener onClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.menu_image);
        txtMenuName = itemView.findViewById(R.id.menu_name);

        itemView.setOnClickListener(this);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        onClickListener.onClick(v, getAdapterPosition(), false);
    }

}
