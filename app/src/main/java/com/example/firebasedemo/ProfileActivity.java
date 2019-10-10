package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private static final int GALLERY_REQUEST = 1;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;

    ImageView registerImage;
    EditText registerName, registerbirthOfDate;

    String email, pass;
    Uri imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getIntent().getExtras() != null) {
            email = getIntent().getExtras().getString("email");
            pass = getIntent().getExtras().getString("password");
        }

        registerImage = findViewById(R.id.imageRegId);
        registerName = findViewById(R.id.register_name);
        registerbirthOfDate = findViewById(R.id.register_dateOfBirth);
        Button saveButton = findViewById(R.id.saveBtn);
        registerImage.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("User");
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            registerImage.setImageURI(uri);
            imageUrl = uri;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageRegId){
            final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST);
        }

        if (view.getId() == R.id.saveBtn){
            uploadImage(mAuth.getUid(), imageUrl);
        }
    }

    private void uploadImage(String uid, final Uri imageUrl) {
        final StorageReference riversRef = mStorageRef.child("images/"+uid+".jpg");

        riversRef.putFile(imageUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getImageUrl(riversRef, imageUrl);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, exception.getMessage());
                    }
                });
    }

    private void getImageUrl(final StorageReference riversRef, Uri imageUrl){
        riversRef.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String name = registerName.getText().toString().trim();
                        String birth = registerbirthOfDate.getText().toString().trim();
                        User mUser = new User(name, email, pass, birth, uri.toString());
                        saveData(mAuth.getUid(), mUser);
                    }
                });
            }
        });
    }

    private void saveData(final String uid, User mUser) {
        myRef.child(uid).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "isSuccessful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, DisplayActivity.class));
                } else {
                    Log.e(TAG, task.getException().getMessage());
                    Toast.makeText(ProfileActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}






