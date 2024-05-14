package com.example.ems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdatePaperActivity extends AppCompatActivity {

    EditText subjectEditText, timeEditText, formEditText;
    Spinner degreeSpinner, yearSpinner;
    Button updateButton;

    DBHelper dbHelper;
    String loggedInUserName;
    int paperId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_paper);

        dbHelper = new DBHelper(this);

        // Initialize views
        subjectEditText = findViewById(R.id.subjectEditText);
        timeEditText = findViewById(R.id.timeEditText);
        formEditText = findViewById(R.id.formEditText);
        degreeSpinner = findViewById(R.id.degreeSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        updateButton = findViewById(R.id.updateButton);

        // Set up spinner adapter
        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(
                this, R.array.degree_options, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreeSpinner.setAdapter(degreeAdapter);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(
                this, R.array.batch_options, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Get data passed from intent
        Intent intent = getIntent();
        if (intent != null) {
            paperId = intent.getIntExtra("PAPER_ID", -1);
            subjectEditText.setText(intent.getStringExtra("SUBJECT"));
            timeEditText.setText(intent.getStringExtra("TIME_DURATION"));
            formEditText.setText(intent.getStringExtra("FORM_LINK"));
            // Set the degree spinner selection
            String degree = intent.getStringExtra("DEGREE");
            if (degree != null) {
                int spinnerPosition = degreeAdapter.getPosition(degree);
                degreeSpinner.setSelection(spinnerPosition);
            }
            String year = intent.getStringExtra("YEAR");
            if (year != null) {
                int spinnerPosition = yearAdapter.getPosition(year);
                degreeSpinner.setSelection(spinnerPosition);
            }
            loggedInUserName = intent.getStringExtra("USER_NAME");
        }

        // Set up button click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePaper();
            }
        });
    }

    private void updatePaper() {
        String subject = subjectEditText.getText().toString().trim();
        String timeDuration = timeEditText.getText().toString().trim();
        String formLink = formEditText.getText().toString().trim();
        String degree = degreeSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();

        // Update paper in database
        long result = dbHelper.updatePaper(paperId, subject, year, timeDuration, formLink, degree, loggedInUserName);
        if (result != -1) {
            Toast.makeText(this, "Paper updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("USER_NAME", loggedInUserName); // Replace userId with the actual user ID
            finish(); // Finish the activity
        } else {
            Toast.makeText(this, "Failed to update paper", Toast.LENGTH_SHORT).show();
        }
    }
}
