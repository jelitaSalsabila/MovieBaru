package com.example.rplrus10.moviebaru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginScreen extends AppCompatActivity {

    Button btnlogin;
    EditText txtuser, txtpass;
    user user;
    SharedPreferences sharedpreferences;
    ProgressBar loading;
    private int MAIN_ACTIVITY_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        btnlogin = findViewById(R.id.btnlogin);
        txtpass = findViewById(R.id.txtpass);
        loading = findViewById(R.id.loading);
        user = new user();
        txtuser = findViewById(R.id.txtuser);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(txtuser.getText().toString());
                user.setPassword(txtpass.getText().toString());
                new LoginProcess().execute();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class LoginProcess extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            //kasih loading
            loading.setVisibility(View.VISIBLE);
            btnlogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "http://192.168.6.227/file/login.php?username="+user.getUsername()+"&password="+user.getPassword();
                System.out.println("url ku " + url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"
                ), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                String json = stringBuilder.toString();
                System.out.println("lala1" + json);
                jsonObject = new JSONObject(json);
                Log.d("lala2", "doInBackground: ");
            } catch (Exception e) {
                jsonObject = null;
                Log.d("lala3", "doInBackground: ");
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            loading.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                        Toast.makeText(getApplicationContext(), "sukses", Toast.LENGTH_SHORT).show();
                        String username = txtuser.getText().toString();
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username", username);
                        editor.apply();
                        startActivity(intent);
                        finish();
                        Log.d("eror if", "onPostExecute: ");
                    } else {
                        Toast.makeText(getApplicationContext(), "lebih teliti", Toast.LENGTH_SHORT).show();
                        Log.d("eror else", "onPostExecute: ");
                    }
                } catch (Exception ignored) {
                    System.out.println("erornya " + ignored);
                }
            } else {
            }
        }
    }
}
