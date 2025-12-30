package com.campuspulse.app;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RaiseIssueActivity extends AppCompatActivity {

    EditText etTitle, etDescription;
    Spinner spCategory;
    Button btnSubmit;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_issue);

        // UI bindings
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        spCategory = findViewById(R.id.spinnerCategory);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Spinner values
        String[] categories = {
                "Infrastructure",
                "Cleanliness",
                "Electricity",
                "Water",
                "Security",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spCategory.setAdapter(adapter);

        // Submit button
        btnSubmit.setOnClickListener(v -> submitIssue());
    }

    private void submitIssue() {

        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSubmit.setEnabled(false); // 🔒 prevent double click

        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> issue = new HashMap<>();
        issue.put("title", title);
        issue.put("description", description);
        issue.put("category", category);
        issue.put("userId", userId);

        // Default admin-controlled fields
        issue.put("status", "Pending");
        issue.put("assignedDepartment", "");
        issue.put("timestamp", System.currentTimeMillis());
        issue.put("lastUpdated", System.currentTimeMillis());

        firestore.collection("issues")
                .add(issue)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(
                            this,
                            "Issue submitted successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnSubmit.setEnabled(true);
                    Toast.makeText(
                            this,
                            "Failed: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}
