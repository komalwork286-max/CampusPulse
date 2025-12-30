package com.campuspulse.app;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;

public class EmpoyeeNotificationsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<SpannableString> notificationList;
    ArrayAdapter<SpannableString> adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empoyee_notifications);

        listView = findViewById(R.id.listEmployeeNotifications);
        notificationList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                notificationList
        );
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadEmployeeNotifications();
    }

    private void loadEmployeeNotifications() {

        db.collection("adminNotifications")
                .get()
                .addOnSuccessListener(query -> {

                    notificationList.clear();

                    if (query.isEmpty()) {
                        Toast.makeText(this, "No notifications", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (QueryDocumentSnapshot doc : query) {

                        String target = doc.getString("target");

                        // EMPLOYEE sees only All + Employees
                        if (!"All".equals(target) && !"Employees".equals(target)) {
                            continue;
                        }

                        String title = doc.getString("title");
                        String message = doc.getString("message");
                        String time = doc.getString("time");

                        String fullText =
                                title + "\n" +
                                        message + "\n" +
                                        time;

                        SpannableString styled = new SpannableString(fullText);
                        styled.setSpan(
                                new StyleSpan(BOLD),
                                0,
                                title.length(),
                                0
                        );

                        notificationList.add(styled);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Failed to load notifications",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }
}
