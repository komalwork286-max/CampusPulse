package com.campuspulse.app;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PublicIssuesActivity extends AppCompatActivity {

    ListView listPublicIssues;
    ArrayList<SpannableString> issueList;
    ArrayAdapter<SpannableString> adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_issues);

        listPublicIssues = findViewById(R.id.listPublicIssues);
        issueList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                issueList
        );
        listPublicIssues.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadPublicIssues();
    }

    private void loadPublicIssues() {

        db.collection("issues")
                .get()
                .addOnSuccessListener(query -> {

                    issueList.clear();

                    if (query.isEmpty()) {
                        Toast.makeText(this, "No issues found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (QueryDocumentSnapshot doc : query) {

                        // 🔥 ISSUE ID
                        String issueId = doc.getId();

                        String title = doc.getString("title");
                        String category = doc.getString("category");
                        String status = doc.getString("status");

                        SpannableStringBuilder builder = new SpannableStringBuilder();

                        // Issue ID (Gray)
                        String idText = "Issue ID: " + issueId + "\n";
                        SpannableString idSpan = new SpannableString(idText);
                        idSpan.setSpan(
                                new ForegroundColorSpan(Color.DKGRAY),
                                0,
                                idText.length(),
                                0
                        );

                        builder.append(idSpan);

                        // Normal text
                        String mainText =
                                "Title: " + title + "\n" +
                                        "Category: " + category + "\n" +
                                        "Status: " + status;

                        SpannableString mainSpan = new SpannableString(mainText);

                        // 🎨 Status Color
                        int statusStart = mainText.indexOf("Status:");
                        int statusEnd = mainText.length();

                        if (status != null) {
                            int color = Color.GRAY;

                            switch (status) {
                                case "Pending":
                                    color = Color.parseColor("#F9A825"); // Yellow
                                    break;
                                case "In Progress":
                                    color = Color.parseColor("#1A73E8"); // Blue
                                    break;
                                case "Resolved":
                                    color = Color.parseColor("#34A853"); // Green
                                    break;
                            }

                            mainSpan.setSpan(
                                    new ForegroundColorSpan(color),
                                    statusStart,
                                    statusEnd,
                                    0
                            );
                        }

                        builder.append(mainSpan);

                        issueList.add(new SpannableString(builder));
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load issues", Toast.LENGTH_SHORT).show()
                );
    }
}
