package id.eightstudio.www.orderfoods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import id.eightstudio.www.orderfoods.Common.Common;
import id.eightstudio.www.orderfoods.Database.OpenHelper;
import id.eightstudio.www.orderfoods.Model.Food;
import id.eightstudio.www.orderfoods.Model.Order;

public class FoodDetail extends AppCompatActivity {
    private static final String TAG = "FoodDetail";

    TextView food_name, food_price, food_description;
    ImageView food_image;
    ElegantNumberButton numberButton;

    String foodId = "";

    FirebaseDatabase database;
    DatabaseReference foods;
    OpenHelper openHelper;

    private Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //SQLite
        openHelper = new OpenHelper(FoodDetail.this);

        //Init View
        numberButton = findViewById(R.id.number_button);
        Button btnCart = findViewById(R.id.floatingBtnCart);

        //Tambah Ke keranjang
        if (btnCart != null)
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO : Periksa item sudah di keranjang ?
                if (openHelper.hasObject(currentFood.getName()) == true) {
                    Toast.makeText(FoodDetail.this, "Sudah di keranjang", Toast.LENGTH_SHORT).show();
                } else {

                    openHelper.addOrder(new Order(foodId,
                            currentFood.getName(),
                            numberButton.getNumber(),
                            currentFood.getPrice(),
                            currentFood.getDiscount(),
                            Common.currentUser.getPhone()
                            ));

                    Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        food_description = findViewById(R.id.food_descrption);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_image = findViewById(R.id.img_food);

        //GetIntent
        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        Log.d(TAG, "onCreate: " + foodId);
        if (foodId != null) {
            getDetailFood(foodId);
        }
    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood  = dataSnapshot.getValue(Food.class);

                try {
                    Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                } catch (NullPointerException e) {
                    Log.d(TAG, "onDataChange: " + e.getMessage());
                }

                //CurrentFood.getName()
                food_price.setText("$ " + currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescrption());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}