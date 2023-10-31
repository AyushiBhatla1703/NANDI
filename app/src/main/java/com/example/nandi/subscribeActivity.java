package com.example.nandi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class subscribeActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Find the SUBMIT button
        Button submitButton = findViewById(R.id.subs_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values entered in the EditText fields
                String userName = ((EditText) findViewById(R.id.uname)).getText().toString();
                String address = ((EditText) findViewById(R.id.cid)).getText().toString();
                String mobileNumber = ((EditText) findViewById(R.id.mno)).getText().toString();

                // Check if any of the fields are empty
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(subscribeActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a reference to the "subscribe" node and set user data
                    DatabaseReference userRef = databaseReference.child("subscribe").push(); // Use push to generate a unique key
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("Name", userName);
                    userData.put("Address", address);
                    userData.put("Mobile", mobileNumber);

                    // Set the user data to Firebase
                    userRef.setValue(userData, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(subscribeActivity.this, "Subscribed!!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(subscribeActivity.this, StartActivity.class));
                            } else {
                                Toast.makeText(subscribeActivity.this, "Failed to subscribe!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
