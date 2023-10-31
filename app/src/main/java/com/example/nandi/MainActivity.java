package com.example.nandi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button logout;
    private EditText name;
    private EditText id;
    private Button add;
    private Button show;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.out_btn);
        name = findViewById(R.id.cname);
        id = findViewById(R.id.cid);
        add = findViewById(R.id.cadd_btn);
        show = findViewById(R.id.cshow_btn);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CattledetailsActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = name.getText().toString();
                String txt_id = id.getText().toString();
                if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_id)) {
                    Toast.makeText(MainActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a reference to the "cattle" node and set cattle data
                    DatabaseReference cattleRef = FirebaseDatabase.getInstance().getReference().child("cattle").child(txt_id);
                    Map<String, Object> cattleData = new HashMap<>();
                    cattleData.put("Name", txt_name);

                    cattleRef.setValue(cattleData, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(MainActivity.this, "Cattle added to database", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, CattledetailsActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to add cattle to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
