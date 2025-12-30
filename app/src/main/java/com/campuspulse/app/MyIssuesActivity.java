package com.campuspulse.app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyIssuesActivity extends AppCompatActivity {

    ListView listMyIssues;
    ArrayList<String> issueList;
    ArrayAdapter<String> adapter;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_issues);

        listMyIssues = findViewById(R.id.listMyIssues);

        issueList = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                issueList
        );
        listMyIssues.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadMyIssues();
    }

    private void loadMyIssues() {

        String userId = auth.getCurrentUser().getUid();

        db.collection("issues")
                .whereEqualTo("userId", userId) // ✅ FIXED
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    issueList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No issues found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        String title = doc.getString("title");
                        String status = doc.getString("status");
                        String category = doc.getString("category");

                        String dateText = "N/A";
                        Long timestamp = doc.getLong("timestamp");
                        if (timestamp != null) {
                            dateText = new SimpleDateFormat(
                                    "dd MMM yyyy",
                                    Locale.getDefault()
                            ).format(new Date(timestamp));
                        }

                        String issueText =
                                "Title: " + title + "\n" +
                                        "Category: " + category + "\n" +
                                        "Status: " + status + "\n" +
                                        "Date: " + dateText;

                        issueList.add(issueText);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load issues", Toast.LENGTH_SHORT).show()
                );
    }
}
