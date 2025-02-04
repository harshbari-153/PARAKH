package com.example.given;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCollege extends AppCompatActivity {




    private EditText name, city, contact, desc, review_count;
    private Button register;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_college);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize EditText fields
        name = findViewById(R.id.collegeNameEditText); // Corrected ID
        city = findViewById(R.id.collegeCityEditText);
        contact = findViewById(R.id.contactEditText);
        desc = findViewById(R.id.shortDescriptionEditText);
        review_count = findViewById(R.id.latentEditText); // Corrected ID

        // Initialize Button
        register = findViewById(R.id.registerButton);

        // Set onClickListener for the register button
        register.setOnClickListener(view -> {
            // Get text from EditText fields
            String Name = name.getText().toString().trim();
            String City = city.getText().toString().trim();
            String Contact = contact.getText().toString().trim();
            String Desc = desc.getText().toString().trim();
            String Review_count = review_count.getText().toString().trim();

            // Check if fields are not empty
            if (Name.isEmpty() || City.isEmpty() || Contact.isEmpty() || Desc.isEmpty() || Review_count.isEmpty()) {
                Toast.makeText(AddCollege.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a Map to store the data
            Map<String, Object> collegeData = new HashMap<>(); // Use Object for flexibility
            collegeData.put("name", Name);
            collegeData.put("city", City);
            collegeData.put("contact", Contact);
            collegeData.put("desc", Desc);
            collegeData.put("review_count", Integer.parseInt(Review_count)); // Store as Integer

            // Store data in Firestore
            firestore.collection("college").document(Name) // Use college name as document ID
                    .set(collegeData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                        // Clear input fields
                        name.setText("");
                        city.setText("");
                        contact.setText("");
                        desc.setText("");
                        review_count.setText("");

                        // Start the Organization activity (if needed)
                        Intent intent = new Intent(AddCollege.this, Organization.class);
                        startActivity(intent);
                        finish(); //Finish this activity so user can't come back using back button

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failure: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });


    }
}