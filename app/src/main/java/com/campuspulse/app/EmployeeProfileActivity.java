package com.campuspulse.app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployeeProfileActivity extends AppCompatActivity {

    TextView txtName, txtEmail, txtDepartment, txtDesignation, txtPhone;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtDepartment = findViewById(R.id.txtDepartment);
        txtDesignation = findViewById(R.id.txtDesignation);
        txtPhone = findViewById(R.id.txtPhone);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            finish();
            return;
        }

        loadEmployeeProfile();
    }

    private void loadEmployeeProfile() {

        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {

                    if (!doc.exists()) {
                        Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Safety check (optional but good)
                    String role = doc.getString("role");
                    if (role == null || !role.equals("employee")) {
                        Toast.makeText(this, "Not an employee account", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    txtName.setText("Name: " + doc.getString("name"));
                    txtEmail.setText("Email: " + doc.getString("email"));
                    txtDepartment.setText("Department: " + doc.getString("department"));
                    txtDesignation.setText("Designation: " + doc.getString("designation"));
                    txtPhone.setText("Phone: " + doc.getString("phone"));

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
                );
    }
}
