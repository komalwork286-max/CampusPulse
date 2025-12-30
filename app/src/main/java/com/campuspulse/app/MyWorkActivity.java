package com.campuspulse.app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyWorkActivity extends AppCompatActivity {

    ListView listMyWork;
    ArrayList<String> displayList;
    ArrayList<String> issueIds;
    ArrayList<String> issueStatus;

    ArrayAdapter<String> adapter;

    FirebaseFirestore db;
    FirebaseAuth auth;
    String employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work);

        listMyWork = findViewById(R.id.listMyWork);

        displayList = new ArrayList<>();
        issueIds = new ArrayList<>();
        issueStatus = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        listMyWork.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        employeeId = auth.getCurrentUser().getUid();

        loadIssues();

        listMyWork.setOnItemClickListener((parent, view, position, id) -> {

            if ("Pending".equals(issueStatus.get(position))) {
                showAcceptDialog(issueIds.get(position));
            } else {
                Toast.makeText(this,
                        "Already accepted or in progress",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadIssues() {

        db.collection("issues")
                .get()
                .addOnSuccessListener(query -> {

                    displayList.clear();
                    issueIds.clear();
                    issueStatus.clear();

                    for (QueryDocumentSnapshot doc : query) {

                        String status = doc.getString("status");
                        String assignedId = doc.getString("assignedEmployeeId");

                        // Show all pending + my accepted issues
                        if ("Pending".equals(status) ||
                                employeeId.equals(assignedId)) {

                            issueIds.add(doc.getId());
                            issueStatus.add(status);

                            String text =
                                    doc.getString("title") + "\n" +
                                            doc.getString("description") + "\n" +
                                            "Status: " + status;

                            displayList.add(text);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to load work",
                                Toast.LENGTH_SHORT).show()
                );
    }

    private void showAcceptDialog(String issueId) {

        new AlertDialog.Builder(this)
                .setTitle("Accept Issue")
                .setMessage("Do you want to accept this issue?")
                .setPositiveButton("Accept", (d, w) -> acceptIssue(issueId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void acceptIssue(String issueId) {

        db.collection("users").document(employeeId)
                .get()
                .addOnSuccessListener(userDoc -> {

                    String empName = userDoc.getString("name");

                    Map<String, Object> update = new HashMap<>();
                    update.put("status", "Accepted");
                    update.put("assignedEmployeeId", employeeId);
                    update.put("assignedEmployeeName", empName);

                    db.collection("issues").document(issueId)
                            .update(update)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this,
                                        "Issue Accepted",
                                        Toast.LENGTH_SHORT).show();
                                loadIssues();
                            });
                });
    }
}
