package com.campuspulse.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentDashboardActivity extends AppCompatActivity {

    // Header
    TextView tvWelcome;

    // Cards
    LinearLayout cardRaiseIssue, cardMyIssues, cardPublicIssues, cardNotifications;

    // Button
    Button btnProfile;

    // Firebase
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Firebase init
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // UI init
        tvWelcome = findViewById(R.id.main).findViewById(1);

        cardRaiseIssue = findViewById(R.id.cardRaiseIssue);
        cardMyIssues = findViewById(R.id.cardMyIssues);
        cardPublicIssues = findViewById(R.id.cardPublicIssues);
        cardNotifications = findViewById(R.id.cardNotifications);
        btnProfile = findViewById(R.id.btnProfile);

        // Load student data
        loadStudentData();

        // Click listeners
        cardRaiseIssue.setOnClickListener(v ->
                startActivity(new Intent(this, RaiseIssueActivity.class))
        );

        cardMyIssues.setOnClickListener(v ->
                startActivity(new Intent(this, MyIssuesActivity.class))
        );

        cardPublicIssues.setOnClickListener(v ->
                startActivity(new Intent(this, PublicIssuesActivity.class))
        );

        cardNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class))
        );

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, StudentProfileActivity.class))
        );
    }

    private void loadStudentData() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        if (name != null) {
                            Toast.makeText(this,
                                    "Welcome " + name,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to load user data",
                                Toast.LENGTH_SHORT).show()
                );
    }
}
