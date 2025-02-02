package com.example.given;

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
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.FirebaseFirestore;

public class Write_Rev extends AppCompatActivity {

    FirebaseFirestore firestore;

    Integer addmissions = 0, average_package = 0 ,patents_published  = 0,total_courses = 0;

    EditText editText;
    Button button;
    TextView textView, textView2, textView3, textView4 ,textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_rev);

        // Initialize UI components
        editText = findViewById(R.id.inputEditText);
        button = findViewById(R.id.submitButton);
        textView = findViewById(R.id.outputTextView);
        textView2 = findViewById(R.id.outputTextView2);
        textView3 = findViewById(R.id.outputTextView3);
        textView4 = findViewById(R.id.outputTextView4);
        textView5 = findViewById(R.id.outputTextView5);


        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Retrieve the passed data from the Intent using the correct keys
        String CoNa = getIntent().getStringExtra("CoNa");
        String CN = getIntent().getStringExtra("CN");
        String con = getIntent().getStringExtra("con");
        String sd = getIntent().getStringExtra("sd");

        // Log the values for debugging purposes
        Log.d("ReceivedData", "CoNa: " + CoNa);
        Log.d("ReceivedData", "CN: " + CN);
        Log.d("ReceivedData", "con: " + con);
        Log.d("ReceivedData", "sd: " + sd);

        // Fetch data from Firestore based on the passed data (College Name)
        firestore.collection("nirf_details_2024").document(CoNa)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        addmissions = documentSnapshot.getLong("addmissions").intValue();
                        average_package = documentSnapshot.getLong("average_package").intValue();
                        patents_published = documentSnapshot.getLong("patents_published").intValue();
                        total_courses = documentSnapshot.getLong("total_courses").intValue();

                        // Log Firestore data for debugging
                        Log.d("FirestoreData", "Addmissions: " + addmissions);
                        Log.d("FirestoreData", "Average Package: " + average_package);
                        Log.d("FirestoreData", "Patents Published: " + patents_published);
                        Log.d("FirestoreData", "Total Courses: " + total_courses);
                    } else {
                        // If no document is found in Firestore
                        Toast.makeText(Write_Rev.this, "No such document!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(Write_Rev.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

        // Set button click listener to start processing the input text
        button.setOnClickListener(view -> {
            String core = editText.getText().toString().trim();  // Get text input from EditText
            if (!core.isEmpty()) {
                model_one_call_patents(core);
                model_two_call_package(core);
                model_three_call_course(core);
                model_four_call_admissions(core);

                generating_claim(core);
            } else {
                Toast.makeText(Write_Rev.this, "Please enter some text", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to calculate the absolute difference
    public int calculateAbsoluteDifference(int value1, int value2) {
        return Math.abs(value1 - value2);
    }

    // Method to handle Patent-related processing
    public void model_one_call_patents(String c) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBjT_lfZk9v3OrgQPvQiOJwhfcmq2ymu_Y");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Given a paragraph, only return number of patents (return 0 if no patent information found).\n\nparagraph:\n" + c)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                textView.setText(result.getText());

                // Convert API response to integer and calculate absolute difference
                int gemPatent = Integer.parseInt(result.getText().trim());
                int patentDifference = calculateAbsoluteDifference(patents_published, gemPatent);

                if(patentDifference >2 ){
                    Toast.makeText(Write_Rev.this, "Your deflection is more ", Toast.LENGTH_SHORT).show();
                }
                Log.d("Difference", "Patent Difference: " + patentDifference);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, this.getMainExecutor());
    }

    // Method to handle average package-related processing
    public void model_two_call_package(String c) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBjT_lfZk9v3OrgQPvQiOJwhfcmq2ymu_Y");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Given a paragraph, return average package in integer format (return zero if found none).\n\nparagraph:\n" + c)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                textView2.setText(result.getText());

                // Convert API response to integer and calculate absolute difference
                int gemPackage = Integer.parseInt(result.getText().trim());
                int packageDifference = calculateAbsoluteDifference(average_package, gemPackage);
                if(packageDifference >150000 ){
                    Toast.makeText(Write_Rev.this, "Your deflection is more ", Toast.LENGTH_SHORT).show();
                }
                Log.d("Difference", "Package Difference: " + packageDifference);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, this.getMainExecutor());
    }

    // Method to handle course-related processing
    public void model_three_call_course(String c) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBjT_lfZk9v3OrgQPvQiOJwhfcmq2ymu_Y");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Given a paragraph, return total number of courses or subjects it provides (return zero if found none).\n\nparagraph:\n" + c)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                textView3.setText(result.getText());

                // Convert API response to integer and calculate absolute difference
                int gemCourse = Integer.parseInt(result.getText().trim());
                int courseDifference = calculateAbsoluteDifference(total_courses, gemCourse);
                if(courseDifference >10 ){
                    Toast.makeText(Write_Rev.this, "Your deflection is more ", Toast.LENGTH_SHORT).show();
                }
                Log.d("Difference", "Course Difference: " + courseDifference);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, this.getMainExecutor());
    }

    // Method to handle admission-related processing
    public void model_four_call_admissions(String c) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBjT_lfZk9v3OrgQPvQiOJwhfcmq2ymu_Y");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Given a paragraph, return total number of admissions (return zero if found none).\n\nparagraph:\n" + c)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                textView4.setText(result.getText());

                // Convert API response to integer and calculate absolute difference
                int gemAdmissions = Integer.parseInt(result.getText().trim());
                int admissionsDifference = calculateAbsoluteDifference(addmissions, gemAdmissions);
                if(admissionsDifference >20 ){
                    Toast.makeText(Write_Rev.this, "Your deflection is more ", Toast.LENGTH_SHORT).show();
                }

                Log.d("Difference", "Admissions Difference: " + admissionsDifference);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, this.getMainExecutor());
    }



    // Method to handle admission-related processing
    public void generating_claim(String c) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBjT_lfZk9v3OrgQPvQiOJwhfcmq2ymu_Y");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Extracting two claims from reviews.\n" +
                        "Finding justifications for specific claims.\n" +
                        "Measuring contextual similarity between a claim and its justification on a scale from 0 to 100.\n" +
                        "Assigning an average similarity score (justification score/trust score) to each review, reflecting its credibility. give me trust score" +
                        "Give me trust score in integer format which is the the average of the both claims Similarity score" + c)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String responseText = result.getText(); // Assuming result.getText() returns the content

                // Regular expression to find the first integer (handles both positive and negative integers)
                String regex = "-?\\d+";
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                java.util.regex.Matcher matcher = pattern.matcher(responseText);

                int que = 0;  // Default value if no integer is found
                if (matcher.find()) {
                    // Parse the first integer found
                    try {
                        que = Integer.parseInt(matcher.group());
                    } catch (NumberFormatException e) {
                        // Handle error if parsing fails
                        e.printStackTrace();
                    }
                }

                // Set the value in the textView
                textView5.setText(String.valueOf(que));
            }



            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }

        }, this.getMainExecutor());


    }

}
