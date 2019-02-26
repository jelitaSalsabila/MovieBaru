package com.example.rplrus10.moviebaru;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    movie movie;
    public ArrayList<movie> movieArrayList;
    RecyclerView recyclerView;
    JSONArray Hasiljson;
    RviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        movie = new movie();
        recyclerView = findViewById(R.id.Rview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeScreen.this));

        new ambildata().execute();

//        adapter = new RviewAdapter(getApplicationContext(),movieArrayList);
//        RviewAdapter adapter = new RviewAdapter(HomeScreen.this,movieArrayList);

    }
    @SuppressLint("StaticFieldLeak")
    public class ambildata extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "http://192.168.6.230/file/get.php";
                System.out.println("url ku " +url);
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
                System.out.println("json nya " + json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                System.out.println("json error : " + e.toString());
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //System.out.println("Hasilnya adalah " +jsonObject.toString());

            try {
                movieArrayList = new ArrayList<>();
                Hasiljson  = jsonObject.getJSONArray("hasil");
                for (int i = 0; i<Hasiljson.length();i++){
                    movie s = new movie();
                    s.setTitle(Hasiljson.getJSONObject(i).getString("title"));
                    s.setPoster_path(Hasiljson.getJSONObject(i).getString("poster_path"));
                    s.setOverview(Hasiljson.getJSONObject(i).getString("overview"));
                    s.setReleasedate(Hasiljson.getJSONObject(i).getString("releasedate"));
                    s.setIdMovie(Hasiljson.getJSONObject(i).getInt("idMovie"));

                    movieArrayList.add(s);
                }
                //pasang data arraylist ke listview
                adapter= new RviewAdapter(HomeScreen.this,movieArrayList);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
