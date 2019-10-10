package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private static final String TAG = "DisplayActivity";
    RecyclerView recyclerView;
    DisplayAdapter adapter;
    List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        mAuth = FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference("User");

        userList = new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayActivity.this));

        //userList.add(new User("Sumon","sumon@gmail.com","06/06/1980"));
        //userList.add(new User("Sumon","sumon@gmail.com","06/06/1980"));
        //userList.add(new User("Sumon","sumon@gmail.com","06/06/1980"));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        User value = snapshot.getValue(User.class);
                        userList.add(value);

                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Value is: " + value);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //adapter.notifyDataSetChanged();
        adapter = new DisplayAdapter(DisplayActivity.this,userList);
        recyclerView.setAdapter(adapter);

    }
}
