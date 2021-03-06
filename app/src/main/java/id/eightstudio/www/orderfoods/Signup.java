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

import id.eightstudio.www.orderfoods.Model.User;

public class Signup extends AppCompatActivity {

    EditText edtPhone, edtName, edtPassword;
    Button btnSignUp;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set screen agar tidak memiliki toobar dan title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Set aplikasi ke dalam keadaan fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup);

        edtPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);

        btnSignUp = findViewById(R.id.btnSignUp);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(Signup.this);
                progressDialog.setMessage("Tunggu Sebentar");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(edtPassword.getText().toString()).exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Phone sudah terdaftar", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();

                            User user = new User(edtName.getText().toString(),
                                    "TanggalLahir",
                                    "BulanLahir",
                                    "TahunLahir",
                                    edtPassword.getText().toString(),
                                    "false"
                            );

                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(Signup.this, "Berhasil Registrasi", Toast.LENGTH_SHORT).show();

                            resetInput();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    //Reset input
    private void resetInput() {
        edtPhone.setText("");
        edtName.setText("");
        edtPassword.setText("");
    }

    //Menuju ke signin
    public void sudahPunyaAkun(View view) {
        Intent intent = new Intent(Signup.this, Signin.class);
        startActivity(intent);
    }
}
