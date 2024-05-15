package com.example.ems;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private LinearLayout cardContainer;
    private String loggedInUserName; // Store the logged-in user's ID

    private Button addPaperButton, updatePaperButton, deletePaperButton,logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DBHelper(this);
        cardContainer = findViewById(R.id.cardContainer);

//         Assuming you pass the logged-in user's ID from LoginActivity
        loggedInUserName = getIntent().getStringExtra("USER_NAME");
//         Fetch papers for the logged-in user
        List<Paper> papers = dbHelper.getPapersForUser(loggedInUserName);


//         Dynamically populate cards with paper details
        for (Paper paper : papers) {
            addPaperCard(paper);
        }
        addPaperButton = findViewById(R.id.add_paper_button);

        addPaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to AddPaperActivity
                Intent intent = new Intent(HomeActivity.this, AddPaperActivity.class);
                intent.putExtra("USER_NAME", loggedInUserName); // Replace userId with the actual user ID
                startActivity(intent);
            }
        });

        // Find the logout button by its ID
        Button logoutButton = findViewById(R.id.logout_button);

        // Set a click listener for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to MainActivity (login page)
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the current activity (HomeActivity)
            }
        });

    }

    private void addPaperCard(Paper paper) {
        // Inflate the card layout from XML
        View cardView = getLayoutInflater().inflate(R.layout.card_paper, null);

        // Set paper details to TextViews in the card layout
        TextView subjectTextView = cardView.findViewById(R.id.text_subject);
        subjectTextView.setText("Subject: " + paper.getSubject());

        TextView yearTextView = cardView.findViewById(R.id.text_year);
        yearTextView.setText("Year: " + String.valueOf(paper.getYear()));

        TextView timeTextView = cardView.findViewById(R.id.text_time);
        timeTextView.setText("Time: " + paper.getTimeDuration());

        Button deleteButton = cardView.findViewById(R.id.delete_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Are you sure you want to delete this paper?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the paper if confirmed
                        dbHelper.deletePaper(paper.getId());
                        // Remove the card from the card container after deletion
                        cardContainer.removeView(cardView);
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        Button updateButton = cardView.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UpdatePaperActivity with preloaded details of the specific paper
                Intent intent = new Intent(HomeActivity.this, UpdatePaperActivity.class);
                // Pass paper details to UpdatePaperActivity
                intent.putExtra("PAPER_ID", paper.getId());
                intent.putExtra("SUBJECT", paper.getSubject());
                intent.putExtra("YEAR", paper.getYear());
                intent.putExtra("DEGREE", paper.getDegree());
                intent.putExtra("TIME_DURATION", paper.getTimeDuration());
                intent.putExtra("FORM_LINK", paper.getFormLink());
                intent.putExtra("USER_NAME", loggedInUserName);
                startActivity(intent);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the form link using an 
                String url = paper.getFormLink();
                if( !url.startsWith("https://") && !url.startsWith("http://") ){
                    url = "https://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        // Add the card layout to the cardContainer LinearLayout
        cardContainer.addView(cardView);
    }
}
