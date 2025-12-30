package com.campuspulse.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    RadioGroup rgRole;
    RadioButton rbStudent, rbEmployee, rbAdmin;
    TextView tvRegister;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        rgRole = findViewById(R.id.rgRole);

        rbStudent = findViewById(R.id.rbStudent);
        rbEmployee = findViewById(R.id.rbEmployee);
        rbAdmin = findViewById(R.id.rbAdmin);

        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void loginUser() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Role selection
        String selectedRole;
        if (rbStudent.isChecked()) selectedRole = "student";
        else if (rbEmployee.isChecked()) selectedRole = "employee";
        else if (rbAdmin.isChecked()) selectedRole = "admin";
        else {
            Toast.makeText(this, "Select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    String uid = auth.getCurrentUser().getUid();

                    db.collection("users")
                            .document(uid)
                            .get()
                            .addOnSuccessListener(doc -> {

                                if (!doc.exists()) {
                                    Toast.makeText(this, "User record not found", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String roleFromDb = doc.getString("role");

                                if (roleFromDb == null || !roleFromDb.equals(selectedRole)) {
                                    Toast.makeText(this, "Wrong role selected", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // 🔀 Redirect by role
                                Intent intent;

                                switch (roleFromDb) {
                                    case "student":
                                        intent = new Intent(this, com.campuspulse.app.StudentDashboardActivity.class);
                                        break;

                                    case "employee":
                                        intent = new Intent(this, com.campuspulse.app.EmployeeDashboardActivity.class);
                                        break;

                                    case "admin":
                                        intent = new Intent(this, com.campuspulse.app.AdminDashboardActivity.class);
                                        break;

                                    default:
                                        Toast.makeText(this, "Invalid role", Toast.LENGTH_SHORT).show();
                                        return;
                                }

                                startActivity(intent);
                                finish();
                            });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
