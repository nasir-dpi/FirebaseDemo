package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegistrationActivity";
    private FirebaseAuth mAuth;

    EditText registerName, registerEmail, registerbirthOfDate, registerPass, registerRePass;
    Button registerButton;
    TextView goToLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        registerEmail = findViewById(R.id.register_email);
        registerPass = findViewById(R.id.register_password);
        registerRePass = findViewById(R.id.register_re_password);
        registerButton = findViewById(R.id.register_btn);
        goToLoginButton = findViewById(R.id.go_to_login);

        registerButton.setOnClickListener(this);
        goToLoginButton.setOnClickListener(this);



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_to_login:
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                break;
            case R.id.register_btn:

                final String email = registerEmail.getText().toString().trim();
                final String password = registerPass.getText().toString().trim();


                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user.getUid() != null) {
                                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

                                            /*Intent  intent1 = new Intent(RegistrationActivity.this,LoginActivity.class);
                                            intent1.putExtra("email",email);
                                            intent1.putExtra("password",password);
                                            startActivity(intent1);*/
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e(TAG, task.getException().getMessage());
                                        Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                } else
                    Toast.makeText(this, "Field Empty", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
