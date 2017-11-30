package id.eightstudio.www.orderfoods;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import id.eightstudio.www.orderfoods.Interface.ItemClickListener;
import id.eightstudio.www.orderfoods.Model.Food;
import id.eightstudio.www.orderfoods.ViewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity {
    private static final String TAG = "FoodList";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private String categoryId;

    FirebaseDatabase database;
    DatabaseReference foodList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_food);

        //GetIntent
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryFood");
        Log.d(TAG, "onCreate: " + categoryId);
        if (categoryId != null) {
            loadListFood(categoryId);
        }
    }

    public void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId) //Select * where categoruId=""
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.food_name.setText(model.getName());
                Picasso.with(FoodList.this).load(model.getImage()).into(viewHolder.food_image);

                final Food food = model;
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLonggerPress) {

                        Intent intent = new Intent(FoodList.this, FoodDetail.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey());//Send food id
                        startActivity(intent);

                    }
                });
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
