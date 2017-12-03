package id.eightstudio.www.orderfoods;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

import id.eightstudio.www.orderfoods.Database.OpenHelper;
import id.eightstudio.www.orderfoods.Model.Food;
import id.eightstudio.www.orderfoods.Model.Order;
import in.goodiebag.carouselpicker.CarouselPicker;

public class FoodDetail extends AppCompatActivity {
    private static final String TAG = "FoodDetail";

    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";

    FirebaseDatabase database;
    DatabaseReference foods;
    OpenHelper openHelper;

    private Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set aplikasi ke dalam keadaan fullscreen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_food_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");
        openHelper = new OpenHelper(FoodDetail.this);

        //Init View
        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);

        //Tambah Ke keranjang
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));*/

                //TODO : Periksa item sudah di keranjang ?
                if (openHelper.hasObject(currentFood.getName()) == true) {
                    Toast.makeText(FoodDetail.this, "Sudah di keranjang", Toast.LENGTH_SHORT).show();
                } else {

                    openHelper.addOrder(new Order(foodId,
                            currentFood.getName(),
                            numberButton.getNumber(),
                            currentFood.getPrice(),
                            currentFood.getDiscount()));

                    Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        food_description = findViewById(R.id.food_descrption);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_image = findViewById(R.id.img_food);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseBar);

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

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(currentFood.getName());
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