package com.example.basefireproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText inputUsername, inputPassword, inputFullname, inputEmail;
    Button registerButton;
    TextView tvLoginLink;
    FirebaseAuth fAuth;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Binding
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputFullname = findViewById(R.id.inputFullname);
        inputEmail = findViewById(R.id.inputEmail);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        registerButton = findViewById(R.id.registerButton);
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

//        Tombol Login Link Listener
        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
//        Tombol register Listener
        registerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                String fullname = inputFullname.getText().toString();
                String email = inputEmail.getText().toString();

                if (TextUtils.isEmpty(username)){
                    inputUsername.setError("Username is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    inputPassword.setError("Password is required");
                    return;
                }

                if (TextUtils.isEmpty(fullname)){
                    inputFullname.setError("Fullname is required");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    inputEmail.setError("Email is required");
                    return;
                }

//                Create new instance
                Log.d("DebugRegiterActivity", "Harusnya update e");



                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                            db = FirebaseDatabase.getInstance();
                            ref = db.getReference("Users");
                            User newUser = new User(username, email, fullname);
                            ref.child(username).setValue(newUser);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}