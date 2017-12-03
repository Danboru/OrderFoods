package id.eightstudio.www.orderfoods.ViewHolder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.eightstudio.www.orderfoods.Database.OpenHelper;
import id.eightstudio.www.orderfoods.Interface.ItemClickListener;
import id.eightstudio.www.orderfoods.Model.Order;
import id.eightstudio.www.orderfoods.R;
import in.goodiebag.carouselpicker.CarouselPicker;

class CartViewHolder extends RecyclerView.ViewHolder {

    TextView txt_cart_name, txt_price;
    ImageView img_cart_count;

    public CartViewHolder(View itemView) {
        super(itemView);

        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);

    }

}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listOrder = new ArrayList<>();
    Context context;
    OpenHelper openHelper;

    public CartAdapter(List<Order> listOrder, Context context) {
        this.listOrder = listOrder;
        this.context = context;
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

        Locale locale = new Locale("en", "US");

        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listOrder.get(position).getPrice())) * (Integer.parseInt(listOrder.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listOrder.get(position).getProductName());

        //Menampilkan popup edit item cart
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditItemCart(v.getContext(), listOrder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    //Click event item cart (Delete, Upadate item cart)
    private void showEditItemCart(final Context context, final List<Order> orderList, final int position) {
        final Dialog dialog = new Dialog(context);

        //Set layout
        dialog.setContentView(R.layout.popup_item_cart);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(true);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Init View
        TextView tester = dialog.findViewById(R.id.txtTester);
        CarouselPicker carouselPicker = dialog.findViewById(R.id.carousel);
        Button updateItem = dialog.findViewById(R.id.btnUpdateItemCart);
        Button deleteItem = dialog.findViewById(R.id.btnDeleteItemCart);

        //Populate data
        final List<CarouselPicker.PickerItem> textItems = new ArrayList<>();

        //20 here represents the textSize in dp, change it to the value you want.
        for (int i = 0; i < 20; i++) {
            textItems.add(new CarouselPicker.TextItem("" + (i + 1), 20));
        }

        //Library link https://github.com/GoodieBag/CarouselPicker
        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(context, textItems, 0);
        carouselPicker.setAdapter(textAdapter);

        final int[] jumlah = {1};
        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
                jumlah[0] = Integer.parseInt(textItems.get(position).getText());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        //Inisialisasi
        tester.setText(orderList.get(position).getProductName());

        //Delete item
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelper.deleteOrder(orderList.get(position).getProductName());
                Toast.makeText(context, "Refresh List", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        //Update item
        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelper.updateOrder(new Order(String.valueOf(jumlah[0])), orderList.get(position).getProductName());
                Toast.makeText(context, "Refresh List", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
