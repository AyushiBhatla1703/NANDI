package com.example.nandi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CattledetailsActivity extends AppCompatActivity {
    private ListView cattleListView;
    private ArrayList<String> cattleList;
    private DatabaseReference cattleRef;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattledetails);

        cattleListView = findViewById(R.id.listview);
        back=findViewById(R.id.out_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click (e.g., navigate back to the previous activity)
                onBackPressed();
            }
        });
        cattleList = new ArrayList<>();
        cattleRef = FirebaseDatabase.getInstance().getReference().child("cattle");

        // Set up an ArrayAdapter to populate the ListView
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cattleList);
        cattleListView.setAdapter(arrayAdapter);

        // Read cattle data from Firebase
        cattleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cattleList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String cattleId = childSnapshot.getKey();
                    String cattleName = childSnapshot.child("Name").getValue(String.class);
                    cattleList.add(cattleId + ": " + cattleName);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CattledetailsActivity.this, "Failed to retrieve cattle data", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle item clicks in the ListView
        cattleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected cattle's ID (the part before ':')
                String selectedCattleInfo = cattleList.get(position);
                String selectedCattleId = selectedCattleInfo.split(":")[0].trim();

                // Start a new activity to display details of the selected cattle
                Intent intent = new Intent(CattledetailsActivity.this, CattleActivity.class);
                intent.putExtra("cattleId", selectedCattleId);
                startActivity(intent);
            }
        });
    }
}
