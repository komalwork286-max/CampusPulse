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

public class EmployeeWorkStatusActivity extends AppCompatActivity {

    ListView listEmployeeWork;
    ArrayList<SpannableString> workList;
    ArrayAdapter<SpannableString> adapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_work_status);

        listEmployeeWork = findViewById(R.id.listEmployeeWork);
        workList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                workList
        );
        listEmployeeWork.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadEmployeeWorkStatus();
    }

    private void loadEmployeeWorkStatus() {

        db.collection("issues")
                .whereNotEqualTo("assignedEmployeeId", null)
                .get()
                .addOnSuccessListener(query -> {

                    workList.clear();

                    if (query.isEmpty()) {
                        Toast.makeText(this, "No employee work assigned yet", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (QueryDocumentSnapshot doc : query) {

                        String issueTitle = doc.getString("title");
                        String employeeName = doc.getString("assignedEmployeeName");
                        String status = doc.getString("status");
                        String time = doc.getString("updatedTime");

                        String text =
                                "Issue: " + issueTitle + "\n" +
                                        "Employee: " + employeeName + "\n" +
                                        "Status: " + status + "\n" +
                                        "Updated: " + time;

                        SpannableString styled = new SpannableString(text);
                        styled.setSpan(
                                new StyleSpan(BOLD),
                                0,
                                ("Issue: " + issueTitle).length(),
                                0
                        );

                        workList.add(styled);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                );
    }
}
