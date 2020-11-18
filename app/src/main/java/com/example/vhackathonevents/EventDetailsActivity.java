package com.example.vhackathonevents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EventDetailsActivity extends AppCompatActivity {

    TextView eventNameText,eventOrganizer, eventTime , eventDescription , eventLink;
    ImageView eventPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        componentRef();

        getAndSetValues();

        eventLink.setMovementMethod(LinkMovementMethod.getInstance()); //clickable link

    }

    private void componentRef() {
        eventPoster = findViewById(R.id.EventCardImage);
        eventNameText = findViewById(R.id.EventNameText);
        eventOrganizer = findViewById(R.id.organizerText);
        eventTime = findViewById(R.id.DateAndTimeText);
        eventLink = findViewById(R.id.LinkText);
        eventDescription = findViewById(R.id.DescriptionText);
    }

    private void getAndSetValues() {
        String posterURL ,eventName, organizer , dateNTime , link , description;

        posterURL = getIntent().getStringExtra("posterURL");
        eventName = getIntent().getStringExtra("eventName");
        organizer = getIntent().getStringExtra("organizer");
        dateNTime = getIntent().getStringExtra("dateNTime");
        link = getIntent().getStringExtra("link");
        description = getIntent().getStringExtra("description");

        Picasso.get()
                .load(posterURL)
                .into(eventPoster);

        eventNameText.setText(eventName);
        eventOrganizer.setText(organizer);
        eventTime.setText(dateNTime);
        eventLink.setText(link);
        eventDescription.setText(description);

        getSupportActionBar().setTitle(eventName);
    }
}