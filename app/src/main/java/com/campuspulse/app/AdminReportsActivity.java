package com.campuspulse.app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminReportsActivity extends AppCompatActivity {

    TextView tvTotalIssues, tvPendingIssues, tvInProgressIssues, tvResolvedIssues, tvPerformance;
    FirebaseFirestore db;

    int total = 0, pending = 0, inProgress = 0, resolved = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);

        tvTotalIssues = findViewById(R.id.tvTotalIssues);
        tvPendingIssues = findViewById(R.id.tvPendingIssues);
        tvInProgressIssues = findViewById(R.id.tvInProgressIssues);
        tvResolvedIssues = findViewById(R.id.tvResolvedIssues);
        tvPerformance = findViewById(R.id.tvPerformance);

        db = FirebaseFirestore.getInstance();

        loadReports();
    }

    private void loadReports() {

        db.collection("issues")
                .get()
                .addOnSuccessListener(query -> {

                    total = pending = inProgress = resolved = 0;

                    for (QueryDocumentSnapshot doc : query) {

                        total++;

                        String status = doc.getString("status");

                        if ("Pending".equals(status)) pending++;
                        else if ("In Progress".equals(status)) inProgress++;
                        else if ("Resolved".equals(status)) resolved++;
                    }

                    updateUI();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load reports", Toast.LENGTH_SHORT).show()
                );
    }

    private void updateUI() {

        tvTotalIssues.setText("Total Issues: " + total);
        tvPendingIssues.setText("Pending Issues: " + pending);
        tvInProgressIssues.setText("In Progress Issues: " + inProgress);
        tvResolvedIssues.setText("Resolved Issues: " + resolved);

        int rate = total == 0 ? 0 : (resolved * 100 / total);
        tvPerformance.setText("Resolution Rate: " + rate + "%");
    }
}
