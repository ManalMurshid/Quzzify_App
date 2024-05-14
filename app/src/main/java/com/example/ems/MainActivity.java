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

//            @Override
//            public void onClick(View v) {
//                String username = usernameInput.getText().toString().trim();
//                String password = passwordInput.getText().toString().trim();
//
//                // Check if username and password are correct
//                boolean isValidUser = dbHelper.authenticateUser(username, password);
//
//                if (isValidUser) {
//                    // Redirect to respective activity based on user type
//                    String userType = dbHelper.getUserType(username);
//                    if (userType.equals("Examiner")) {
//                        // Redirect to ExaminerHomeActivity
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                    } else if (userType.equals("Candidate")) {
//                        // Redirect to CandidateActivity
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//                }
//            }
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


//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class MainActivity extends AppCompatActivity {
//    EditText username, password;
//    Button signup, signin;
//    DBHelper DB;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        username = (EditText) findViewById(R.id.username_input);
//        password = (EditText) findViewById(R.id.password_input);
//        signup = (Button) findViewById(R.id.new_user_button);
//        signin = (Button) findViewById(R.id.login_button);
//        DB = new DBHelper(this);
//
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String user = username.getText().toString();
//                String pass = password.getText().toString();
//
//                if (user.equals("") || pass.equals("")) {
//                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
//                    if (checkuserpass != null) {
//                        if (checkuserpass) {
//                            // Candidate, direct to CandidateActivity
//                            Toast.makeText(MainActivity.this, "Sign in successful (Candidate)", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), CandidateActivity.class);
//                            startActivity(intent);
//                        } else {
//                            // Examiner, direct to HomeActivity
//                            Toast.makeText(MainActivity.this, "Sign in successful (Examiner)", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                            startActivity(intent);
//                        }
//                    } else {
//                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),  LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}
