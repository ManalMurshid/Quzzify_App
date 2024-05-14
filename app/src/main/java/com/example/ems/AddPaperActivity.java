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

public class AddPaperActivity extends AppCompatActivity {

    EditText subjectEditText, timeEditText, formEditText;
    Spinner degreeSpinner, yearSpinner;
    Button addPaperButton;

    DBHelper dbHelper;
    String loggedInUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paper);

        loggedInUserName = getIntent().getStringExtra("USER_NAME");

        dbHelper = new DBHelper(this);

        // Initialize views
        subjectEditText = findViewById(R.id.subjectEditText);
        timeEditText = findViewById(R.id.timeEditText);
        formEditText = findViewById(R.id.formEditText);
        degreeSpinner = findViewById(R.id.degreeSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        addPaperButton = findViewById(R.id.addPaperButton);

        // Set up spinner adapters
        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(
                this, R.array.degree_options, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreeSpinner.setAdapter(degreeAdapter);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(
                this, R.array.batch_options, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Set up button click listener
        addPaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPaper();
            }
        });
    }

    private void addPaper() {
        String subject = subjectEditText.getText().toString().trim();
        String timeDuration = timeEditText.getText().toString().trim();
        String formLink = formEditText.getText().toString().trim();
        String degree = degreeSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();

        // Add paper to database with the username of the logged-in user
        long result = dbHelper.addPaper(subject, year,
                timeDuration, formLink, getApplicationContext(), degree, loggedInUserName);
        if (result != -1) {
            Toast.makeText(this, "Paper added successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields
            clearFields();
            // Redirect to HomeActivity
            Intent intent = new Intent(AddPaperActivity.this, HomeActivity.class);
            intent.putExtra("USER_NAME", loggedInUserName);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to add paper", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to clear input fields
    private void clearFields() {
        subjectEditText.setText("");
        timeEditText.setText("");
        formEditText.setText("");
        degreeSpinner.setSelection(0);
        yearSpinner.setSelection(0);
    }

}
