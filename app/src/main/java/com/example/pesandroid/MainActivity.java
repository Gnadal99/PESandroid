package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
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

public class MainActivity extends AppCompatActivity {

    //Object for this activity is stored here
    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This is executed when loading the activity, that is, when launching the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;

        //Make progress bar invisible
        ProgressBar pgBar = findViewById(R.id.loginProgressBar);
        pgBar.setVisibility(View.INVISIBLE);

        //Look for shared preferences. In case there are stored values, try to log in
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String mail = sh.getString("mail", null);
        String password = sh.getString("password", null);
        if (mail != null && password != null){
            loginWithPreferences(mail, password);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //This is executed when user clicks any button of the phone system. If the button was "back", finish the app
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finishAffinity();
        }
        return super.onKeyDown(keyCode, event);
    }

    //Query for logging in with credentials introduced by user
    public void login(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            final Handler handler = new Handler();
            public void run() {
                try {
                    //Obtain data
                    EditText mailIn = findViewById(R.id.logineditTextEmailAddress);
                    EditText passwordIn = findViewById(R.id.logineditTextPassword);
                    ImageView image = findViewById(R.id.loginImageView);
                    Button button = findViewById(R.id.loginBtn);
                    TextView text = findViewById(R.id.loginsignUpText);
                    TextView text2 = findViewById(R.id.loginsignUpLink);
                    ProgressBar pgBar = findViewById(R.id.loginProgressBar);
                    String mail = mailIn.getText().toString();
                    String password = passwordIn.getText().toString();

                    handler.post(() -> {
                        //Change visibilities. Only progress bar must be seen
                        TextView title = findViewById(R.id.loginTitleText);
                        title.setVisibility(View.INVISIBLE);
                        image.setVisibility(View.INVISIBLE);
                        mailIn.setVisibility(View.INVISIBLE);
                        passwordIn.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        text.setVisibility(View.INVISIBLE);
                        text2.setVisibility(View.INVISIBLE);

                        pgBar.setVisibility(View.VISIBLE);
                    });

                    //Query
                    String query = "http://10.0.2.2:9000/Android/login?mail=" + mail + "&password=" + password;
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
                        //If answer is 0, credentials are right
                        case "0":
                            handler.post(() -> {
                                //Save shared preferences
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("MySharedPref", 0);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("mail", mail);
                                myEdit.putString("password", password);
                                myEdit.apply();

                                //Change to inbox activity. Before, send to it the mail
                                Intent intent = new Intent(activity, InboxActivity.class);
                                intent.putExtra("mail", mail);
                                activity.startActivity(intent);

                                //Change visibilities. Progress bar must not be seen
                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                                mailIn.setVisibility(View.VISIBLE);
                                passwordIn.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                text.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);

                                pgBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        //If answer is -1, the mail introduced does not exist
                        case "-1":
                            handler.post(() -> {
                                //Render toast with error message
                                Toast.makeText(activity.getApplicationContext(), "Mail address does not exist. Try again.", Toast.LENGTH_LONG).show();

                                //Change visibilities. Progress bar must not be seen
                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                                mailIn.setVisibility(View.VISIBLE);
                                passwordIn.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                text.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        //If answer is -2, the password introduced is wrong
                        case "-2":
                            handler.post(() -> {
                                //Render toast with error message
                                Toast.makeText(activity.getApplicationContext(), "Wrong password. Try again.", Toast.LENGTH_LONG).show();

                                //Change visibilities. Progress bar must not be seen
                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                                mailIn.setVisibility(View.VISIBLE);
                                passwordIn.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                text.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        //This option should never happen
                        default:
                            handler.post(() -> {
                                //Render toast with error message
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                                //Change visibilities. Progress bar must not be seen
                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                                mailIn.setVisibility(View.VISIBLE);
                                passwordIn.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                text.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //Query for logging in with credentials saved on shared preferences
    public void loginWithPreferences(String mail, String password) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            final Handler handler = new Handler();
            public void run() {
                try {
                    TextView title = findViewById(R.id.loginTitleText);
                    ImageView image = findViewById(R.id.loginImageView);
                    EditText mailBox = findViewById(R.id.logineditTextEmailAddress);
                    EditText passBox = findViewById(R.id.logineditTextPassword);
                    Button button = findViewById(R.id.loginBtn);
                    TextView text = findViewById(R.id.loginsignUpText);
                    TextView text2 = findViewById(R.id.loginsignUpLink);
                    ProgressBar pgBar = findViewById(R.id.loginProgressBar);

                    handler.post(() -> {
                        //Change visibilities. Only progress bar must be seen
                        title.setVisibility(View.INVISIBLE);
                        image.setVisibility(View.INVISIBLE);
                        mailBox.setVisibility(View.INVISIBLE);
                        passBox.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        text.setVisibility(View.INVISIBLE);
                        text2.setVisibility(View.INVISIBLE);
                        pgBar.setVisibility(View.VISIBLE);
                    });

                    //Query
                    String query = "http://10.0.2.2:9000/Android/login?mail=" + mail + "&password=" + password;
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
                        //If answer is 0, credentials are right
                        case "0":
                            handler.post(() -> {
                                //Save credentials
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("MySharedPref", 0);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("mail", mail);
                                myEdit.putString("password", password);
                                myEdit.apply();

                                //Show inbox activity. Before that, send to it the mail
                                Intent intent = new Intent(activity, InboxActivity.class);
                                intent.putExtra("mail", mail);
                                activity.startActivity(intent);

                                //Change visibilities. Progress bar must not be seen
                                title.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                                mailBox.setVisibility(View.VISIBLE);
                                passBox.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                text.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        //If answer is -1 or -2, credentials are wrong
                        case "-1":
                        case "-2":
                            handler.post(() -> {

                                //Change visibilities. Progress bar must not be seen
                                title.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                                mailBox.setVisibility(View.VISIBLE);
                                passBox.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                text.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        //This option should never happen
                        default:
                            handler.post(() -> {
                                //Render toast with error message
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                                //Change visibilities. Progress bar must not be seen
                                title.setVisibility(View.VISIBLE);
                                image.setVisibility(View.VISIBLE);
                                mailBox.setVisibility(View.VISIBLE);
                                passBox.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                text.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //Event that executes when user clicks signUp button
    public void signUp(View view) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }
}