package id.eightstudio.www.orderfoods;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.eightstudio.www.orderfoods.Common.Common;
import id.eightstudio.www.orderfoods.Database.Database;
import id.eightstudio.www.orderfoods.Model.Order;
import id.eightstudio.www.orderfoods.Model.Request;
import id.eightstudio.www.orderfoods.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set screen agar tidak memiliki toobar dan title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Set aplikasi ke dalam keadaan fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog(Cart.this);
            }
        });
        
        loadListFood();

    }

    private void showAlertDialog(final Context context) {

        final Dialog dialog = new Dialog(context);

        //Set layout
        dialog.setContentView(R.layout.place_order);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(true);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Init View
        final Button btnNo = dialog.findViewById(R.id.btnNo);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final EditText inputAdress = dialog.findViewById(R.id.txtAdress);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Order Di Batalkan", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cek alamat kosong ?
                if (TextUtils.isEmpty(inputAdress.getText().toString())) {
                    Toast.makeText(context, "Isikan Alamat", Toast.LENGTH_SHORT).show();
                } else {

                    Request request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            inputAdress.getText().toString(),
                            txtTotalPrice.getText().toString(),
                            cart
                    );

                    requests.child(requests.push().getKey()).setValue(request);
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this, "Terimakasih", Toast.LENGTH_SHORT).show();
                    finish();
                    
                }

            }
        });

        dialog.show();
    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        cartAdapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(cartAdapter);

        //Calculate total
        int total = 0;
        for ( Order order : cart ) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }

        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(numberFormat.format(total));
        //txtTotalPrice.setText("$ "  + String.valueOf(total) );

    }
}