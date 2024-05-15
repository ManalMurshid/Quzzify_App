package com.example.ems;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button loginButton, newUserButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        newUserButton = findViewById(R.id.new_user_button);

        loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String user = usernameInput.getText().toString();
                    String pass = passwordInput.getText().toString();

                    if (user.equals("") || pass.equals("")) {
                        Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    } else {
                        String user_type = dbHelper.checkusernamepassword(user, pass);
                        if (user_type != null) {
                            if (user_type.equals(DBHelper.USER_TYPE_CANDIDATE)) {
                                // Candidate, direct to CandidateActivity
                                Toast.makeText(MainActivity.this, "Login successful (Candidate)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), CandidateActivity.class);
                                intent.putExtra("USER_NAME", user);
                                startActivity(intent);
                            } else {
                                // Examiner, direct to HomeActivity
                                Toast.makeText(MainActivity.this, "Login successful (Examiner)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra("USER_NAME", user); // Replace userId with the actual user ID
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to RegistrationActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}


