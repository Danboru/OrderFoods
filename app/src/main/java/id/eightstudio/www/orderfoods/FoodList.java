package id.eightstudio.www.orderfoods;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import id.eightstudio.www.orderfoods.Interface.OnClickListener;
import id.eightstudio.www.orderfoods.Model.Food;
import id.eightstudio.www.orderfoods.Utils.GridSpacingItemDecoration;
import id.eightstudio.www.orderfoods.ViewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity {
    private static final String TAG = "FoodList";

    private String categoryId = "";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference foodList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    //Search Function
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set aplikasi ke dalam keadaan fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_food);

        //GetIntent
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        Log.d(TAG, "onCreate: " + categoryId);
        if (categoryId != null) {
            loadListFood(categoryId);
        }

        //Search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Your Food");
        loadSuggest(); //Get data from firebase
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //While user type their text, we will change suggest list

                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) { //Loop in suggest list
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When searchbar is close
                //Restore original adapter
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When seach finish
                //Show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
            }
        });

    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString()) //Compare name
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.food_name.setText(model.getName());
                Picasso.with(FoodList.this).load(model.getImage()).into(viewHolder.food_image);

                final Food local = model;
                viewHolder.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLonggerPress) {

                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", searchAdapter.getRef(position).getKey());//Send food id
                        startActivity(foodDetail);

                    }
                });
            }
        };

        recyclerView.setAdapter(searchAdapter); //Set adapter for recycler view is earch result
    }

    private void loadSuggest() {
        foodList.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren() ) {
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                viewHolder.food_price.setText("Rp " + model.getPrice());
                Picasso.with(FoodList.this).load(model.getImage()).into(viewHolder.food_image);

                final Food local = model;
                viewHolder.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLonggerPress) {

                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());//Send food id
                        startActivity(foodDetail);

                    }
                });
            }
        };


        //Costum grid recycler
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
