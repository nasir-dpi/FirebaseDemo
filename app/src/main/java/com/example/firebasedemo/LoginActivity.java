package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    EditText userEmail, userPass;
    Button logInButton;
    TextView goToRegisterButton;

    private FirebaseAuth mAuth;
    String email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        /*if (getIntent().getExtras() != null) {
            email = getIntent().getExtras().getString("email");
            pass = getIntent().getExtras().getString("password");
        }*/

        userEmail = findViewById(R.id.emailid);
        userPass = findViewById(R.id.passid);
        logInButton = findViewById(R.id.login);
        goToRegisterButton = findViewById(R.id.go_to_rigester);

        logInButton.setOnClickListener(this);
        goToRegisterButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                final String email = userEmail.getText().toString().trim();
                final String password = userPass.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Intent  intent1 = new Intent(LoginActivity.this, ProfileActivity.class);
                                        intent1.putExtra("email", email);
                                        intent1.putExtra("password", password);
                                        startActivity(intent1);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });

                } else {
                    Toast.makeText(this, "Field Empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.go_to_rigester:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
        }
    }


}
