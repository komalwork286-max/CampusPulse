package com.campuspulse.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout btnAllIssues, btnIssueStatus, btnEmployeeWorkStatus,
            btnReports, btnNotifications;
    Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnAllIssues = findViewById(R.id.btnAllIssues);
        btnIssueStatus = findViewById(R.id.btnIssueStatus);
        btnEmployeeWorkStatus = findViewById(R.id.btnEmployeeWorkStatus);
        btnReports = findViewById(R.id.btnReports);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnProfile = findViewById(R.id.btnProfile);

        // 📊 All Issues (Public View)
        btnAllIssues.setOnClickListener(v -> {
            startActivity(new Intent(
                    this,
                    PublicIssuesActivity.class
            ));
        });

        // 🔁 Issue Status Update
        btnIssueStatus.setOnClickListener(v -> {
            startActivity(new Intent(
                    this,
                    IssueStatusUpdateActivity.class
            ));
        });

        // 👷 Employee Work Status (NEW)
        btnEmployeeWorkStatus.setOnClickListener(v -> {
            startActivity(new Intent(
                    this,
                    EmployeeWorkStatusActivity.class
            ));
        });

        // 📈 Reports
        btnReports.setOnClickListener(v -> {
            startActivity(new Intent(
                    this,
                    AdminReportsActivity.class
            ));
        });

        // 📢 Notifications
        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(
                    this,
                    AdminSendNotificationActivity.class
            ));
        });

        // 👤 Profile
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(
                    this,
                    AdminProfileActivity.class
            ));
        });
    }
}
