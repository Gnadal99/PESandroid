package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendMessageActivity extends AppCompatActivity {

    //Object for this activity is stored here
    SendMessageActivity activity;
    String senderMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        activity = this;

        //Make progress bar invisible
        ProgressBar pBar = findViewById(R.id.sendMessageProgressBar);
        pBar.setVisibility(View.INVISIBLE);

        //Get parameters from previous activity
        senderMail = getIntent().getStringExtra("sender");
        String receiverMail = getIntent().getStringExtra("receiver");
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");

        if(receiverMail != null && title != null && body != null) {
            EditText titleIn = findViewById(R.id.sendMessageTitleIn);
            EditText mailIn = findViewById(R.id.sendMessageMailIn);
            EditText bodyIn = findViewById(R.id.sendMessageBodyIn);
            titleIn.setText(title);
            mailIn.setText(receiverMail);
            bodyIn.setText(body);
        }
        else if(title != null && body != null) {
            EditText titleIn = findViewById(R.id.sendMessageTitleIn);
            EditText bodyIn = findViewById(R.id.sendMessageBodyIn);
            titleIn.setText(title);
            bodyIn.setText(body);
        }
    }

    //This function is called each time user clicks "send" button
    public void send(View view) {

        TextView titleText = findViewById(R.id.sendMessageTitleBox);
        EditText titleIn = findViewById(R.id.sendMessageTitleIn);
        EditText mailIn = findViewById(R.id.sendMessageMailIn);
        EditText bodyIn = findViewById(R.id.sendMessageBodyIn);
        Button sendBtn = findViewById(R.id.sendMessageSendBtn);
        Button discardBtn = findViewById(R.id.sendMessageDiscardBtn);
        ProgressBar pBar = findViewById(R.id.sendMessageProgressBar);

        String mail = mailIn.getText().toString();

        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            final Handler handler = new Handler();
            public void run() {
                try {
                    //Obtain data
                    String title = titleIn.getText().toString();
                    String body = bodyIn.getText().toString();
                    System.out.println(body);

                    handler.post(() -> {
                        //Change visibilities. Only progress bar must be seen
                        titleText.setVisibility(View.INVISIBLE);
                        titleIn.setVisibility(View.INVISIBLE);
                        mailIn.setVisibility(View.INVISIBLE);
                        bodyIn.setVisibility(View.INVISIBLE);
                        sendBtn.setVisibility(View.INVISIBLE);
                        discardBtn.setVisibility(View.INVISIBLE);

                        pBar.setVisibility(View.VISIBLE);
                    });

                    //Query
                    String query = "http://10.0.2.2:9000/Android/send?subject=" + title + "&message=" + body + "&receiverMail=" + mail + "&senderMail=" + senderMail;
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    //Response
                    stream = conn.getInputStream();
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Result processing
                    switch (result) {
                        //If answer is 0, message has been sent successfully
                        case "0":
                            handler.post(() -> {
                                Toast.makeText(activity.getApplicationContext(), "Sent successfully!", Toast.LENGTH_LONG).show();
                                finish();
                            });
                            break;
                        //If answer is -1, receiver mail introduced does not exist
                        case "-1":
                            handler.post(() -> {
                                Toast.makeText(activity.getApplicationContext(), "Receiver mail introduced does not exist", Toast.LENGTH_LONG).show();

                                titleText.setVisibility(View.VISIBLE);
                                titleIn.setVisibility(View.VISIBLE);
                                mailIn.setVisibility(View.VISIBLE);
                                bodyIn.setVisibility(View.VISIBLE);
                                sendBtn.setVisibility(View.VISIBLE);
                                discardBtn.setVisibility(View.VISIBLE);

                                pBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        //This option should never happen
                        default:
                            handler.post(() -> {
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                                titleText.setVisibility(View.VISIBLE);
                                titleIn.setVisibility(View.VISIBLE);
                                mailIn.setVisibility(View.VISIBLE);
                                bodyIn.setVisibility(View.VISIBLE);
                                sendBtn.setVisibility(View.VISIBLE);
                                discardBtn.setVisibility(View.VISIBLE);

                                pBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //This function is called when user clicks "discard" button
    public void discard(View view) {
        finish();
    }
}