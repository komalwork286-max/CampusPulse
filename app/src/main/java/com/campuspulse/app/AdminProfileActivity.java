package com.campuspulse.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminProfileActivity extends AppCompatActivity {

    TextView tvAdminName, tvAdminEmail, tvAdminRole;
    Button btnLogout;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        tvAdminName = findViewById(R.id.tvAdminName);
        tvAdminEmail = findViewById(R.id.tvAdminEmail);
        tvAdminRole = findViewById(R.id.tvAdminRole);
        btnLogout = findViewById(R.id.btnLogout);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadAdminProfile();

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }

    private void loadAdminProfile() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUid = auth.getCurrentUser().getUid();

        // ✅ CORRECT QUERY BASED ON YOUR FIREBASE STRUCTURE
        db.collection("users")
                .whereEqualTo("uid", currentUid)
                .get()
                .addOnSuccessListener(query -> {

                    if (query.isEmpty()) {
                        Toast.makeText(this, "Admin profile not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (QueryDocumentSnapshot doc : query) {

                        String name = doc.getString("name");
                        String email = doc.getString("email");
                        String role = doc.getString("role");

                        tvAdminName.setText(name != null ? name : "Admin");
                        tvAdminEmail.setText(email != null ? email : "");
                        tvAdminRole.setText(role != null ? role.toUpperCase() : "ADMIN");

                        break; // only one profile needed
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
