package id.eightstudio.www.orderfoods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.eightstudio.www.orderfoods.Common.Common;
import id.eightstudio.www.orderfoods.Database.OpenHelper;
import id.eightstudio.www.orderfoods.Model.User;

public class Signin extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference table_user;

    OpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set screen agar tidak memiliki toobar dan title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Set aplikasi ke dalam keadaan fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signin);

        openHelper = new OpenHelper(Signin.this);

        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignIn = findViewById(R.id.btnSignIn);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressBar = new ProgressDialog(Signin.this);
                progressBar.setMessage("Tunggu Sebentar");
                progressBar.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            progressBar.dismiss();

                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone(edtPhone.getText().toString()); //Set phone
                            if (user.getPassword().equals(edtPassword.getText().toString())) {

                                Intent intent = new Intent(Signin.this, Home.class);
                                Common.currentUser = user;
                                startActivity(intent);

                                //Menyimpan data current user
                                //Bertujuan untuk tidak login
                                //Data akan di hapus saat logout
                                openHelper.addUser(new User(user.getPhone(), user.getPassword(), user.getIsStaff()));
                                finish();

                            } else {
                                Toast.makeText(Signin.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.dismiss();
                            Toast.makeText(Signin.this, "User tidak Terdafatar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Signin.this, "Server Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    //Menuju ke activity signup
    public void belumPunyaAkun(View view) {
        Intent intent = new Intent(Signin.this, Signup.class);
        startActivity(intent);
    }
}
