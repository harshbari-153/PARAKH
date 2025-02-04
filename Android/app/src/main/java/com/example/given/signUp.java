package com.example.given;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        firestore = FirebaseFirestore.getInstance();

        Button register = findViewById(R.id.signUpButton);
        EditText email , password , institute , course , username ;
        TextView display_field ;

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        institute = findViewById(R.id.instituteEditText);
        username = findViewById(R.id.usernameEditText);


        TextView login;
        login = findViewById(R.id.alreadyHaveAccountTextView);

        login.setOnClickListener(view -> {
            Intent it  = new Intent(signUp.this, Login.class);
            startActivity(it);
        });



        // Register Button
        register.setOnClickListener(view -> {
            // Get text from EditText fields
            String passwordText = password.getText().toString().trim();
            String instituteText = institute.getText().toString().trim();
            String usernameText = username.getText().toString().trim();
            String email_field = email.getText().toString().trim();

            // Check if fields are not empty
            if (email_field.isEmpty() || passwordText.isEmpty() || instituteText.isEmpty() || usernameText.isEmpty()) {
                Toast.makeText(signUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a Map to store the data
            Map<String, String> user = new HashMap<>();
            user.put("email", email_field);
            user.put("name", usernameText);
            user.put("password", passwordText);
            user.put("institute", instituteText);

            // Store data under a specific key
            firestore.collection("nikhil").document(email.getText().toString().trim()) // Using userId as the document ID
                    .set(user)  // Set the data
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            // Clear input fields by setting them to an empty string (or null if required)
                            email.setText("");  // Clears the email input field
                            password.setText("");  // Clears the password input field
                            institute.setText("");  // Clears the institute input field
                            username.setText("");  // Clears the username input field
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failure: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        });


    }
}