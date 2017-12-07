package id.eightstudio.www.orderfoods;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.eightstudio.www.orderfoods.Common.Common;
import id.eightstudio.www.orderfoods.Interface.OnClickListener;
import id.eightstudio.www.orderfoods.Model.Request;
import id.eightstudio.www.orderfoods.ViewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set aplikasi ke dalam keadaan fullscreen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_status);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        requests.keepSynced(true);

        layoutManager = new LinearLayoutManager(OrderStatus.this);
        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());

    }

    //Load order berdasarkan phone
    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                String status = model.getStatus();

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());

                //Set status berdasarkan id
                if (status.equals("0")){
                    viewHolder.txtOrderStatus.setTextColor(getColor(R.color.colorMenunggu));
                } else if (status.equals("1")){
                    viewHolder.txtOrderStatus.setTextColor(getColor(R.color.colorDiProses));
                } else if (status.equals("2")){
                    viewHolder.txtOrderStatus.setTextColor(getColor(R.color.colorPengiriman));
                } else {
                    viewHolder.txtOrderStatus.setTextColor(getColor(R.color.colorDiTerima));
                }

                viewHolder.txtOrderStatus.setText(convertCodeStatus(model.getStatus()));

                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                
                viewHolder.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLonggerPress) {
                        //Action
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    private String convertCodeStatus(String status) {

        if (status.equals("0"))
            return "Menunggu";
        else if (status.equals("1"))
            return "Di Proses";
        else if (status.equals("2"))
            return "Pengiriman";
        else
            return "Di Terima";

    }

    //Info order
    public void infoOrder(View view) {
        showDialogInfo(this);
    }

    //Dialog info order
    private void showDialogInfo(Context context) {

        final Dialog dialog = new Dialog(context);

        //Set layout
        dialog.setContentView(R.layout.info_order);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(true);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }

}
