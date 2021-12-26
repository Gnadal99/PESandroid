package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    //Object for this activity is stored here
    RegisterActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.activity = this;

        //Make progress bar invisible
        ProgressBar pgBar = findViewById(R.id.registerProgressBar);
        pgBar.setVisibility(View.INVISIBLE);
    }

    //Query for registering with credentials introduced by user
    public void register(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            final Handler handler = new Handler();
            public void run() {
                try {
                    //Obtain data
                    TextView title = findViewById(R.id.registerTitleText);
                    ImageView image = findViewById(R.id.registerImageView);
                    EditText mailIn = findViewById(R.id.registerEditTextEmailAddress);
                    EditText passwordIn = findViewById(R.id.registerEditTextPassword);
                    EditText nameIn = findViewById(R.id.registerEditTextFullName);
                    Button button = findViewById(R.id.registerBtn);
                    TextView text2 = findViewById(R.id.registerSignUpLink);
                    ProgressBar pgBar = findViewById(R.id.registerProgressBar);
                    String mail = mailIn.getText().toString();
                    String password = passwordIn.getText().toString();
                    String fullName = nameIn.getText().toString();

                    handler.post(() -> {
                        //Change visibilities. Only progress bar must be seen
                        title.setVisibility(View.INVISIBLE);
                        image.setVisibility(View.INVISIBLE);
                        mailIn.setVisibility(View.INVISIBLE);
                        passwordIn.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        text2.setVisibility(View.INVISIBLE);
                        pgBar.setVisibility(View.VISIBLE);
                    });

                    //Query
                    String query = "http://10.0.2.2:9000/Android/register?mail=" + mail + "&password=" + password + "&fullName=" + fullName;
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    stream = conn.getInputStream();

                    //Response
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Result processing
                    //If response is 0, user has been registered
                    if(result.equals("0")) {
                        handler.post(() -> {
                            //AÑADIR PREFERENCIAS COMPARTIDAS Y PREGUNTAR

                            //Show inbox activity. Before, send to it the mail
                            Intent intent = new Intent(activity, InboxActivity.class);
                            intent.putExtra("mail", mail);
                            activity.startActivity(intent);

                            //Change visibilities. Progress bar must not be seen
                            title.setVisibility(View.VISIBLE);
                            image.setVisibility(View.VISIBLE);
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            text2.setVisibility(View.VISIBLE);
                            pgBar.setVisibility(View.INVISIBLE);
                        });
                    }
                    //If answer is -1, the mail introduced does already exist
                    else if (result.equals("-1")) {
                        handler.post(() -> {
                            //Render toast with error message
                            Toast.makeText(activity.getApplicationContext(), "Mail address does already exist. Try again.", Toast.LENGTH_LONG).show();

                            //Change visibilities. Progress bar must not be seen
                            title.setVisibility(View.VISIBLE);
                            image.setVisibility(View.VISIBLE);
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            text2.setVisibility(View.VISIBLE);
                            pgBar.setVisibility(View.INVISIBLE);
                        });
                    }
                    else {
                        handler.post(() -> {
                            //Render toast with error message
                            Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                            //Change visibilities. Progress bar must not be seen
                            title.setVisibility(View.VISIBLE);
                            image.setVisibility(View.VISIBLE);
                            mailIn.setVisibility(View.VISIBLE);
                            passwordIn.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            text2.setVisibility(View.VISIBLE);
                            pgBar.setVisibility(View.INVISIBLE);
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //This is executed when "log out" option is clicked
    public void goBack(View view) {
        finish();
    }
}