package com.example.given;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class Write_Review extends AppCompatActivity {
    FirebaseFirestore firestore;
    TextView tv;
    Button post, generate;

    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        // Initialize views
        generate = findViewById(R.id.generateScoreButton);
        post = findViewById(R.id.postButton);  // Make sure to initialize post if needed
        tv = findViewById(R.id.extraTextView);  // TextView to display API result
        ed = findViewById(R.id.writeReviewEdit);

        // Retrieve "colleg" value from the Intent
        String CoNa = getIntent().getStringExtra("colleg");
//        EditText revi = findViewById(R.id.writeReviewEdit);  // EditText for the review

        // Get text from the review EditText
        String str = ed.getText().toString().trim();

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Example prompt for Gemini API
        String P1 = "Given a paragraph, only return number of patents (return 0 if no patent information found) \n\nparagraph:\n" ;
        String P2 = "Given a paragraph, return average package in integer format, it may be in LPA or lakh or thousand or any other format, return proper number (return zero if found none)\n" + "\n" + "paragraph:\n ";
        String P3 = "Given a paragraph, return total number of courses or subject it provides (return zero if found none)\n";
        String P4 = "Given a paragraph, return total number of addmissions (return zero if found none)\n";

        // Generate score button click listener
        generate.setOnClickListener(view -> {

            Log.d("NikhilGajbhiyeSvnit", "onCreate: "+ str);
            model_one_call(P1);
            model_one_call(P2);
            model_one_call(P3);
            model_one_call(P4);





            // Fetch data from Firestore based on the CoNa value (College Name or ID)
            Log.d("CoNa", "Document ID: " + CoNa);  // Log the value of CoNa for debugging
            firestore.collection("nirf_details_2024").document(CoNa)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Fetch data from Firestore
                            Integer addmissions = documentSnapshot.getLong("addmissions").intValue();  // Assuming "addmissions" is a Long
                            Integer average_package = documentSnapshot.getLong("average_package").intValue();  // Assuming "average_package" is a Long
                            Integer patents_published = documentSnapshot.getLong("patents_published").intValue();  // Assuming "patents_published" is a Long
                            Integer total_courses = documentSnapshot.getLong("total_courses").intValue();  // Assuming "total_courses" is a Long

                            // Log the values for debugging
                            Log.d("FirestoreData", "Addmissions: " + addmissions);
                            Log.d("FirestoreData", "Average Package: " + average_package);
                            Log.d("FirestoreData", "Patents Published: " + patents_published);
                            Log.d("FirestoreData", "Total Courses: " + total_courses);
                        } else {
                            // Document does not exist
                            Toast.makeText(Write_Review.this, "No such document!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(Write_Review.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    public void model_one_call(String P1) {
        Log.d("fhgftyfkgfhgdfkhdkghd", "model_one_call: " + P1);

        // For text-only input, use the Gemini model
        GenerativeModel gm = new GenerativeModel("gemini-2.0-flash-exp", "AIzaSyDJAvS5grnneKNrpSkIG3qPDN_sJtDUi0g");

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(P1)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                // Extract the result text
                String resultText = result.getText();
                // Display the result in the TextView
                tv.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                // Log the error and print stack trace
                Log.e("GeminiAPIError", "Failed to generate content", t);
                t.printStackTrace();
            }
        }, this.getMainExecutor());
    }
}
