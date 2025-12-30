package com.campuspulse.app;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;

public class NotificationsActivity extends AppCompatActivity {

    ListView listNotifications;
    ArrayList<SpannableString> notificationList;
    ArrayAdapter<SpannableString> adapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        listNotifications = findViewById(R.id.listNotifications);
        notificationList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                notificationList
        );
        listNotifications.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadNotifications();
    }

    private void loadNotifications() {

        db.collection("adminNotifications")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(query -> {

                    notificationList.clear();

                    for (QueryDocumentSnapshot doc : query) {

                        String target = doc.getString("target");

                        // STUDENT sees only All + Students
                        if (!"All".equals(target) && !"Students".equals(target)) {
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
                        styled.setSpan(new StyleSpan(BOLD), 0, title.length(), 0);

                        notificationList.add(styled);
                    }

                    if (notificationList.isEmpty()) {
                        Toast.makeText(this, "No notifications", Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load notifications", Toast.LENGTH_SHORT).show()
                );
    }
}
