package com.example.given;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseFirestore firestore;

        TextView signup;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup = findViewById(R.id.signUpTextView);

        Intent intent = new Intent(Login.this, signUp.class );

        signup.setOnClickListener(view -> {
            startActivity(intent);
        });

        firestore = FirebaseFirestore.getInstance();

        Button login = findViewById(R.id.signInButton);
        EditText email , password;

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);


        // Find the TextView
        TextView signUpTextView = findViewById(R.id.signUpTextView);

        // Set click listener


        // function to fetch/read
        login.setOnClickListener(view ->{
            if(email.getText().toString().trim().isEmpty()){
                Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Fetch the document with the specified id
            // Reference to the specific document under 'nikhil' collection
            firestore.collection("nikhil").document(email.getText().toString().trim())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                //display_field.setText("Ready to display");

                                // Retrieve the values of fields A, B, C1, C2
                                String A = documentSnapshot.getString("email");
                                String B = documentSnapshot.getString("name");
                                String C1 = documentSnapshot.getString("institute");
                                String C2 = documentSnapshot.getString("password");

                                String temp = password.getText().toString().trim();

                                Intent gotoHomepage =new Intent(Login.this , MainActivity.class);

                                if(C2.equals(temp)){
                                    Toast.makeText(Login.this, "Successfully Logged in ", Toast.LENGTH_LONG).show();

                                    startActivity(gotoHomepage);
                                }
                                else{
                                    Toast.makeText(Login.this, "Enter Correct Password", Toast.LENGTH_LONG).show();
                                }

                                // Store the values in variables or use them as needed
                                // For example, display in Toast or assign to UI elements
//                            Toast.makeText(MainActivity.this,
//                                    "A: " + A + "\nB: " + B + "\nC1: " + C1 + "\nC2: " + C2,
//                                    Toast.LENGTH_LONG).show();

                                String ans = "A: " + A + ", B: " + B + ", C1: " + C1 + ", C2: " + C2;
//                                display_field.setText(ans);
                                // Alternatively, store in string variables for further use
//                                String strA = A != null ? A : "Not available";
//                                String strB = B != null ? B : "Not available";
//                                String strC1 = C1 != null ? C1 : "Not available";
//                                String strC2 = C2 != null ? C2 : "Not available";

                                // Use these string variables as needed
                            } else {
                                // If the document doesn't exist
                                Toast.makeText(Login.this, "No such document!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure (e.g., network error)
                            Toast.makeText(Login.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

    }
}