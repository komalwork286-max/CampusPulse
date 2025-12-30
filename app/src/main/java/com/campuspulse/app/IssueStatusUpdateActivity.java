package com.campuspulse.app;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.campuspulse.app.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class IssueStatusUpdateActivity extends AppCompatActivity {

    EditText edtIssueId;
    RadioGroup rgStatus;
    RadioButton rbPending, rbInProgress, rbResolved;
    Spinner spDepartment;
    Button btnUpdateIssue;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_issue_status);

        edtIssueId = findViewById(R.id.edtIssueId);
        rgStatus = findViewById(R.id.rgStatus);
        rbPending = findViewById(R.id.rbPending);
        rbInProgress = findViewById(R.id.rbInProgress);
        rbResolved = findViewById(R.id.rbResolved);
        spDepartment = findViewById(R.id.spDepartment);
        btnUpdateIssue = findViewById(R.id.btnUpdateIssue);

        db = FirebaseFirestore.getInstance();

        // Department Spinner
        String[] departments = {
                "Maintenance",
                "IT",
                "Security",
                "Administration",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                departments
        );
        spDepartment.setAdapter(adapter);

        btnUpdateIssue.setOnClickListener(v -> updateIssue());
    }

    private void updateIssue() {

        String issueId = edtIssueId.getText().toString().trim();

        if (issueId.isEmpty()) {
            Toast.makeText(this, "Enter Issue ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rgStatus.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select status", Toast.LENGTH_SHORT).show();
            return;
        }

        String status =
                rbPending.isChecked() ? "Pending" :
                        rbInProgress.isChecked() ? "In Progress" :
                                "Resolved";

        String department = spDepartment.getSelectedItem().toString();

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("status", status);
        updateMap.put("assignedDepartment", department);
        updateMap.put("lastUpdated", System.currentTimeMillis());

        db.collection("issues")
                .document(issueId)
                .update(updateMap)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Issue updated successfully", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                );
    }
}
