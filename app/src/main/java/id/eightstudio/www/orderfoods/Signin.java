package id.eightstudio.www.orderfoods;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

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

                final String phone, pass;
                phone = edtPhone.getText().toString();
                pass = edtPassword.getText().toString();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(phone).exists()) {
                            progressBar.dismiss();
                            User user = dataSnapshot.child(phone).getValue(User.class);
                            if (user.getPassword().equals(pass)) {
                                Toast.makeText(Signin.this, "Berhasil", Toast.LENGTH_SHORT).show();
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

                    }
                });

            }
        });

    }
}
