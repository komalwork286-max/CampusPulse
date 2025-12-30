package com.campuspulse.app;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdminSendNotificationActivity extends AppCompatActivity {

    EditText edtTitle, edtMessage;
    Spinner spinnerTarget;
    Button btnSend;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_send_notification);

        edtTitle = findViewById(R.id.edtTitle);
        edtMessage = findViewById(R.id.edtMessage);
        spinnerTarget = findViewById(R.id.spinnerTarget);
        btnSend = findViewById(R.id.btnSend);

        db = FirebaseFirestore.getInstance();

        String[] targets = {"All", "Students", "Employees"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                targets
        );
        spinnerTarget.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendNotification());
    }

    private void sendNotification() {

        String title = edtTitle.getText().toString().trim();
        String message = edtMessage.getText().toString().trim();
        String target = spinnerTarget.getSelectedItem().toString();

        if (title.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String time = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
        ).format(new Date());

        HashMap<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("message", message);
        notification.put("target", target);
        notification.put("time", time);

        db.collection("adminNotifications")
                .add(notification)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();
                    edtTitle.setText("");
                    edtMessage.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to send", Toast.LENGTH_SHORT).show()
                );
    }
}
