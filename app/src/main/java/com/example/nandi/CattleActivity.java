package com.example.nandi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CattleActivity extends AppCompatActivity {

    private TextView cattleNameTextView;
    private Button backbtn; // Button for going back
    private TextView cattleIdTextView;
    private TextView temperatureTextView;
    private TextView heartRateTextView;
    private TextView accelerationTextView;
    private TextView healthStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle);

        cattleNameTextView = findViewById(R.id.textViewCattleName);
        cattleIdTextView = findViewById(R.id.textViewCattleId);
        temperatureTextView = findViewById(R.id.textViewTemperature);
        heartRateTextView = findViewById(R.id.textViewHeartRate);
        accelerationTextView = findViewById(R.id.textViewAccelerationX);
        healthStatusTextView = findViewById(R.id.textViewHealthStatus);
        backbtn = findViewById(R.id.backButton); // Initialize the back button

        // Get the cattle ID passed from the previous activity
        String cattleId = getIntent().getStringExtra("cattleId");

        // Create a reference to the specific cattle using the ID
        DatabaseReference cattleRef = FirebaseDatabase.getInstance().getReference().child("cattle").child(cattleId);

        // Attach a ValueEventListener to fetch and display the cattle's data
        cattleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve data from the DataSnapshot
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    String id = dataSnapshot.getKey(); // Corrected to get cattle ID
                    Double temperature = dataSnapshot.child("temperature").getValue(Double.class);
                    Long heartRate = dataSnapshot.child("heartRate").getValue(Long.class);
                    Double accelerationX = dataSnapshot.child("acceleration").child("x").getValue(Double.class);
                    Double accelerationY = dataSnapshot.child("acceleration").child("y").getValue(Double.class);
                    Double accelerationZ = dataSnapshot.child("acceleration").child("z").getValue(Double.class);

                    // Set the TextViews with the retrieved data
                    cattleNameTextView.setText("Cattle Name: " + name);
                    cattleIdTextView.setText("Cattle ID: " + id);
                    temperatureTextView.setText("Temperature: " + temperature);
                    heartRateTextView.setText("Heart Rate: " + heartRate);
                    accelerationTextView.setText("Acceleration (X, Y, Z): " + accelerationX + ", " + accelerationY + ", " + accelerationZ);

                    // Classify health status based on temperature, heart rate, and acceleration
                    String healthStatus = classifyHealthStatus(temperature, heartRate, accelerationX, accelerationY, accelerationZ);
                    healthStatusTextView.setText("Health Status: " + healthStatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur during data retrieval
            }
        });

        // Set an OnClickListener for the back button
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click (e.g., navigate back to the previous activity)
                onBackPressed();
            }
        });
    }

    private String classifyHealthStatus(Double temperature, Long heartRate, Double accelerationX, Double accelerationY, Double accelerationZ) {
        // Define your health status classification logic here
        // Modify this logic based on your criteria for classifying health status.
        // You can consider temperature, heart rate, and acceleration values.
        if (temperature >= 39.0 || heartRate >= 100 || accelerationX >= 2.0 || accelerationY >= 2.0 || accelerationZ >= 2.0) {
            return "High Risk";
        } else if (temperature >= 37.5 || heartRate >= 80 || accelerationX >= 1.0 || accelerationY >= 1.0 || accelerationZ >= 1.0) {
            return "Moderate Risk";
        } else {
            return "Normal";
        }
    }
}
