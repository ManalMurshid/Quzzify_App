package com.example.ems;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class CandidateActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private LinearLayout cardContainer;
    private String loggedInUserName; // Store the logged-in user's ID
//    private Button addPaperButton, updatePaperButton, deletePaperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        dbHelper = new DBHelper(this);
        cardContainer = findViewById(R.id.candidateCardContainer);

        // Assuming you pass the logged-in user's ID from LoginActivity
        loggedInUserName = getIntent().getStringExtra("USER_NAME");

        // Fetch papers for the logged-in user
        String[] batchDegree = dbHelper.getUserBatchDegree(loggedInUserName);
        String batch = batchDegree[0];
        String degree = batchDegree[1];

        List<Paper> papers = dbHelper.getPapersForCandidate(batch, degree);

        // Dynamically populate cards with paper details
        for (Paper paper : papers) {
            addPaperCard(paper);
        }

        Button logoutButton = findViewById(R.id.logout_button);

        // Set a click listener for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to MainActivity (login page)
                Intent intent = new Intent(CandidateActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the current activity (CandidateActivity)
            }
        });

    }


    private void addPaperCard(Paper paper) {
        // Inflate the card layout from XML
        View cardView = getLayoutInflater().inflate(R.layout.candidate_paper_card, null);

        // Set paper details to TextViews in the card layout
        TextView subjectTextView = cardView.findViewById(R.id.text_subject);
        subjectTextView.setText("Subject: " + paper.getSubject());

        TextView yearTextView = cardView.findViewById(R.id.text_year);
        yearTextView.setText("Year: " + paper.getYear());

        TextView degreeTextView = cardView.findViewById(R.id.text_degree);
        degreeTextView.setText("Degree: " + paper.getDegree());

        TextView durationTextView = cardView.findViewById(R.id.text_duration);
        durationTextView.setText("Time Duration: " + paper.getTimeDuration());

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