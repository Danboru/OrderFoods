package id.eightstudio.www.orderfoods;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import id.eightstudio.www.orderfoods.Common.Common;
import id.eightstudio.www.orderfoods.Database.OpenHelper;
import id.eightstudio.www.orderfoods.Interface.ItemClickListener;
import id.eightstudio.www.orderfoods.Model.Category;
import id.eightstudio.www.orderfoods.ViewHolder.MenuViewHolder;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    FirebaseDatabase database;
    DatabaseReference category;
    OpenHelper openHelper;

    TextView txtFullName;
    CounterFab counterFab;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    //Link reference
    //https://stackoverflow.com/questions/30560663/navigationview-menu-items-with-counter-on-the-right
    //https://stackoverflow.com/questions/31265530/how-can-i-get-menu-item-in-navigationview
    Menu menu;
    MenuItem menuCart, menuOrder;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set aplikasi ke dalam keadaan fullscreen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        openHelper = new OpenHelper(this);

        navigationView = findViewById(R.id.nav_view);

        menu = navigationView.getMenu();
        menuCart = menu.findItem(R.id.nav_cart);
        menuOrder = menu.findItem(R.id.nav_orders);

        //Set counter Defaulth
        setMenuCounter(menuCart.getItemId() , openHelper.getOrderCountFilter(Common.currentUser.getPhone()));
        setMenuCounter(menuOrder.getItemId() , 0);

        //Floating Action Bar
        //Library link https://github.com/andremion/CounterFab
        counterFab = findViewById(R.id.fab);

        //Set default counter
        counterFab.setCount(openHelper.getOrderCountFilter(Common.currentUser.getPhone()));

        counterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);

            }
        });

        //Drawer Layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set FullName
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);

        try {
            txtFullName.setText(Common.currentUser.getName());
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Name");
        }

        //Load Menu Firebase
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        //Load data firebase
        loadMenu();

    }

    //Set counter navivigationDrawer
    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        counterFab.setCount(openHelper.getOrderCountFilter(Common.currentUser.getPhone()));

        //Set counter
        setMenuCounter(menuCart.getItemId() , openHelper.getOrderCountFilter(Common.currentUser.getPhone()));
        setMenuCounter(menuOrder.getItemId() , 0);

    }

    //Menampilkan food list berdasarkan kategori
    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,
                R.layout.menu_item,
                MenuViewHolder.class,
                category
        ) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {

                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(Home.this).load(model.getImage()).into(viewHolder.imageView);

                final Category clickItem = model;
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLonggerPress) {

                        Intent intent = new Intent(Home.this, FoodList.class);
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());//Send food id
                        startActivity(intent);
                    }
                });
            }
        };

        //Set RecyclerView Adapter
        recycler_menu.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            showDialogTentang(Home.this);
        }

        return true;
    }

    private void showDialogTentang(Context context) {

        final Dialog dialog = new Dialog(context);

        //Set layout
        dialog.setContentView(R.layout.popup_about);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(true);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {

            Intent cart = new Intent(Home.this, Cart.class);
            startActivity(cart);

        } else if (id == R.id.nav_orders) {

            Intent order = new Intent(Home.this, OrderStatus.class);
            startActivity(order);

        } else if (id == R.id.nav_log_out) {

            Intent signin = new Intent(Home.this, Signin.class);
            signin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signin);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
