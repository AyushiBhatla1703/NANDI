package com.example.nandi;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CattleActivity extends AppCompatActivity {
    private TextView cattleNameTextView;
    private TextView textViewTemperature;
    private TextView textViewHeartRate;
    private TextView textViewCattleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle);

        cattleNameTextView = findViewById(R.id.textViewCattleName);
        textViewCattleId = findViewById(R.id.textViewCattleId);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewHeartRate = findViewById(R.id.textViewHeartRate);

        String selectedCattleId = getIntent().getStringExtra("cattleId");
        DatabaseReference cattleRef = FirebaseDatabase.getInstance().getReference().child("cattle").child(selectedCattleId);

        cattleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    String id = dataSnapshot.child("ID").getValue(String.class);
                    String temperature = dataSnapshot.child("temperature").getValue(String.class);
                    String heartRate = dataSnapshot.child("heartRate").getValue(String.class);

                    cattleNameTextView.setText("Cattle Name: " + name);
                    textViewCattleId.setText("Cattle ID: " + id);
                    textViewTemperature.setText("Temperature: " + temperature);
                    textViewHeartRate.setText("Heart Rate: " + heartRate);
                } else {
                    cattleNameTextView.setText("Cattle not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cattleNameTextView.setText("Error retrieving cattle data");
            }
        });
    }
}
