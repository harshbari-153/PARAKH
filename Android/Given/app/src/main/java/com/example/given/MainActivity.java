package com.example.given;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    ListView ls;
    ArrayList<String> arrNames = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    ArrayList<String> contact = new ArrayList<>();
    ArrayList<String> sortDesc = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button login , profile , addCollege ;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();


        login = findViewById(R.id.loginbutton);
        profile = findViewById(R.id.MyProfile);
        addCollege = findViewById(R.id.AddCollege);



        Intent it = new Intent(MainActivity.this , Login.class);

        login.setOnClickListener(view -> {
            startActivity(it);
        });


        Intent iter = new Intent(MainActivity.this , User.class);

        profile.setOnClickListener(view -> {
            startActivity(iter);
        });

        Intent itera = new Intent(MainActivity.this , AddCollege.class);

        addCollege.setOnClickListener(view -> {
            startActivity(itera);
        });


        ls = findViewById(R.id.listView);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1 ,arrNames);

        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(true){
                    Intent at = new Intent(MainActivity.this , Organization.class );
                    startActivity(at);
                }
            }
        });



        // Fetch all documents from the "college" collection
        firestore.collection("college")
                .get()  // Fetch all documents
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        // Check if there are any documents in the collection
                        if (!querySnapshot.isEmpty()) {
                            // Iterate through each document and extract the "name" field
                            for (DocumentSnapshot documentSnapshot : querySnapshot) {
                                // Extract the "name" field (or other fields)
                                String collegeName = documentSnapshot.getString("name");
                                String Cit = documentSnapshot.getString("city");
                                String Cont = documentSnapshot.getString("contact");
                                String Desc = documentSnapshot.getString("desc");





                                // Add the college name to the list
                                if (collegeName != null) {
                                    arrNames.add(collegeName);
                                    cityName.add(Cit);
                                    contact.add(Cont);
                                    sortDesc.add(Desc);
                                }
                            }

                            // Notify the adapter that data has been added to the list
                            adapter.notifyDataSetChanged();  // Refresh the ListView
                        } else {
                            // No documents found in the collection
                            Toast.makeText(MainActivity.this, "No documents found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle error
                        Toast.makeText(MainActivity.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Set an item click listener to handle clicks on items in the ListView
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String selectedCollege = arrNames.get(position);  // Get the selected item
                String CoNa = arrNames.get(position);
                String CN = cityName.get(position);
                String con = contact.get(position);
                String sd = sortDesc.get(position);

                Log.d("Tryuniqy", "onItemClick: " + CoNa + CN + con + sd);

//                Toast.makeText(MainActivity.this, "Selected: " + selectedCollege, Toast.LENGTH_SHORT).show();
                // You can navigate to another activity or handle the item click here

                Intent ut = new Intent(MainActivity.this , Organization.class);
                // Put the strings into the Intent using putExtra()
                ut.putExtra("CoNa", CoNa);
                ut.putExtra("CN", CN);
                ut.putExtra("con", con);
                ut.putExtra("sd", sd);
                startActivity(ut);
            }
        });
    }
}