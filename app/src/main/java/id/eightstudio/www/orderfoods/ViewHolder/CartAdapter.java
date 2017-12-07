package id.eightstudio.www.orderfoods.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.eightstudio.www.orderfoods.Database.OpenHelper;
import id.eightstudio.www.orderfoods.Interface.RecyclerViewClickListener;
import id.eightstudio.www.orderfoods.Model.Order;
import id.eightstudio.www.orderfoods.R;


//Taken from https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
//Adapter
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Order> listOrder = new ArrayList<>();
    Context context;
    OpenHelper openHelper;
    RecyclerView currentRecyclerView;
    RecyclerViewClickListener itemListener;


    public CartAdapter(List<Order> listOrder, Context context, RecyclerViewClickListener itemListener) {
        this.listOrder = listOrder;
        this.context = context;
        this.itemListener = itemListener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.cart_layout, parent, false);

        openHelper = new OpenHelper(parent.getContext());

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + listOrder.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("id", "ID");

        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listOrder.get(position).getPrice())) * (Integer.parseInt(listOrder.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listOrder.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    //Taken from https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
    //ViewHolder
    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView txt_cart_name, txt_price;
        ImageView img_cart_count;


        public CartViewHolder(View itemView) {
            super(itemView);

            txt_cart_name = itemView.findViewById(R.id.cart_item_name);
            txt_price = itemView.findViewById(R.id.cart_item_price);
            img_cart_count = itemView.findViewById(R.id.cart_item_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getPosition());
        }
    }


}
