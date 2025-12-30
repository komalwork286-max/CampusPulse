package com.campuspulse.app;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EmpoyeeUpdateStatusActivity extends AppCompatActivity {

    TextView txtIssueTitle;
    Spinner spinnerStatus;
    EditText edtRemark;
    Button btnUpdateStatus;

    FirebaseFirestore db;
    FirebaseAuth auth;

    String issueId, issueTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empoyee_update_status);

        txtIssueTitle = findViewById(R.id.txtIssueTitle);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        edtRemark = findViewById(R.id.edtRemark);
        btnUpdateStatus = findViewById(R.id.btnUpdateStatus);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get issue data from previous screen
        issueId = getIntent().getStringExtra("issueId");
        issueTitle = getIntent().getStringExtra("title");

        txtIssueTitle.setText(issueTitle);

        // Spinner values
        String[] statusList = {"In Progress", "Resolved"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statusList
        );
        spinnerStatus.setAdapter(adapter);

        btnUpdateStatus.setOnClickListener(v -> updateIssueStatus());
    }

    private void updateIssueStatus() {

        if (issueId == null) {
            Toast.makeText(this, "Invalid issue", Toast.LENGTH_SHORT).show();
            return;
        }

        String newStatus = spinnerStatus.getSelectedItem().toString();
        String remark = edtRemark.getText().toString().trim();
        String employeeId = auth.getCurrentUser().getUid();

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("status", newStatus);
        updateMap.put("remark", remark);
        updateMap.put("updatedBy", employeeId);
        updateMap.put("updatedAt", System.currentTimeMillis());

        db.collection("issues")
                .document(issueId)
                .update(updateMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
                );
    }
}
