package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReadMessageActivity extends AppCompatActivity {

    //Object for this activity is stored here
    ReadMessageActivity activity;
    TextView titleOut;
    TextView mailOut;
    TextView dateOut;
    TextView bodyOut;

    String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        activity = this;

        //Get parameters from previous activity
        receiver = getIntent().getStringExtra("receiver");
        String mail = getIntent().getStringExtra("sender");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String body = getIntent().getStringExtra("body");

        //Show message's details on layout objects
        titleOut = findViewById(R.id.readMessageTitleBox);
        mailOut = findViewById(R.id.readMessageMailBox);
        dateOut = findViewById(R.id.readMessageDateBox);
        bodyOut = findViewById(R.id.readMessageBodyBox);
        titleOut.setText(title);
        mailOut.setText(mail);
        dateOut.setText(date);
        bodyOut.setText(body);
    }

    //This function is called each time user clicks "forward" button
    public void forward(View view) {
        //Change to send message activity. Before, send to it the mail
        Intent intentSend = new Intent(activity, SendMessageActivity.class);
        intentSend.putExtra("sender", receiver);
        intentSend.putExtra("title", "Fwd: " + titleOut.getText());
        intentSend.putExtra("body", "\n\n" + mailOut.getText() + " Previously wrote: \n" + bodyOut.getText());
        activity.startActivity(intentSend);
    }

    //This function is called each time user clicks "reply" button
    public void reply(View view) {
        //Change to send message activity. Before, send to it the mail
        Intent intentSend = new Intent(activity, SendMessageActivity.class);
        intentSend.putExtra("sender", receiver);
        intentSend.putExtra("receiver", mailOut.getText());
        intentSend.putExtra("title", "Re: " + titleOut.getText());
        intentSend.putExtra("body", "\n\n" + mailOut.getText() + " Previously wrote: \n" + bodyOut.getText());
        activity.startActivity(intentSend);
    }
}