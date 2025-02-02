package com.example.given;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Organization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        Button Rev = findViewById(R.id.addReview);

        // Retrieve the strings using the same keys
        String CoNa = getIntent().getStringExtra("CoNa");
        String CN = getIntent().getStringExtra("CN");
        String con = getIntent().getStringExtra("con");
        String sd = getIntent().getStringExtra("sd");

        TextView CollegeNa = findViewById(R.id.collegeName);
        TextView CityNA = findViewById(R.id.collegeCity);
        TextView ConNa = findViewById(R.id.latentNumbers);
        TextView SDNa = findViewById(R.id.shortDescription);

        CollegeNa.setText(CoNa);
        CityNA.setText(CN);
        ConNa.setText(con);
        SDNa.setText(sd);

        // Initialize intent to send data to Write_Rev
        Intent intentation = new Intent(Organization.this, Write_Rev.class);

        Rev.setOnClickListener(view -> {
            // Pass the data using keys and values
            intentation.putExtra("CoNa", CoNa);
            intentation.putExtra("CN", CN);
            intentation.putExtra("con", con);
            intentation.putExtra("sd", sd);
            startActivity(intentation);
        });
    }
}
