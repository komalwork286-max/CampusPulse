package com.campuspulse.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDashboardActivity extends AppCompatActivity {

    LinearLayout btnCampusIssues, btnMyWork, btnUpdateStatus;
    LinearLayout btnNotifications;
    Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        btnCampusIssues = findViewById(R.id.cardCampusIssues);
        btnMyWork = findViewById(R.id.cardMyWork);
        btnUpdateStatus = findViewById(R.id.cardUpdateStatus);
        btnNotifications = findViewById(R.id.cardNotifications);
        btnProfile = findViewById(R.id.btnProfile);

        btnCampusIssues.setOnClickListener(v ->
                startActivity(new Intent(this, PublicIssuesActivity.class)));

        btnMyWork.setOnClickListener(v ->
                startActivity(new Intent(this, MyWorkActivity.class)));

        btnUpdateStatus.setOnClickListener(v ->
                startActivity(new Intent(this, EmpoyeeUpdateStatusActivity.class)));

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, EmpoyeeNotificationsActivity.class)));

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, EmployeeProfileActivity.class)));
    }
}
