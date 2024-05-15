package com.example.ems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private RadioGroup userRadioGroup;
    private RadioButton examinerRadio, candidateRadio;
    private LinearLayout candidateFields;
    private Spinner batchSpinner, degreeSpinner;
    private Button registerButton;
    private DBHelper dbHelper; // Assuming you have a DBHelper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Replace with your layout resource ID

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        userRadioGroup = findViewById(R.id.user_type_group);
        examinerRadio = findViewById(R.id.examiner_radio);
        candidateRadio = findViewById(R.id.candidate_radio);
        candidateFields = findViewById(R.id.candidate_fields);
        batchSpinner = findViewById(R.id.batch_spinner);
        degreeSpinner = findViewById(R.id.degree_spinner);
        registerButton = findViewById(R.id.register_button);
        dbHelper = new DBHelper(this); // Initialize DBHelper instance

        // Set up spinner adapters (assuming you have string arrays for batch and degree options)
        ArrayAdapter<CharSequence> batchAdapter = ArrayAdapter.createFromResource(this,
                R.array.batch_options, android.R.layout.simple_spinner_item);
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchSpinner.setAdapter(batchAdapter);

        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(this,
                R.array.degree_options, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreeSpinner.setAdapter(degreeAdapter);

        // Handle radio button selection (show/hide candidate fields)
        userRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.candidate_radio) {
                    candidateFields.setVisibility(View.VISIBLE);
                } else {
                    candidateFields.setVisibility(View.GONE);
                }
            }
        });

        // Handle register button click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String userType = examinerRadio.isChecked() ? "Examiner" : "Candidate";
                String batch = batchSpinner.getSelectedItem().toString();
                String degree = degreeSpinner.getSelectedItem().toString();

                // Validation (optional)
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save user data in DB using DBHelper methods
                long userId = dbHelper.addUser(username, password, userType, batch, degree);
                if (userId > 0) {
                    Toast.makeText(LoginActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                    // Redirect based on user type
                    if (userType.equals("Examiner")) {
                        Intent examinerIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        examinerIntent.putExtra("USER_NAME", username);
                        startActivity(examinerIntent);
                    } else {
                        Intent candidateIntent = new Intent(LoginActivity.this, CandidateActivity.class);
                        candidateIntent.putExtra("USER_NAME", username);
                        startActivity(candidateIntent);
                    }

                    // Clear form (optional)
                    usernameInput.setText("");
                    passwordInput.setText("");
                } else {
                    Toast.makeText(LoginActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
