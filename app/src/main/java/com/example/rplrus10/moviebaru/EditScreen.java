package com.example.rplrus10.moviebaru;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EditScreen extends AppCompatActivity {

    int idMovie;
    TextView txtUserID;
    EditText editNama,edittanggal,edtOverview;
    Button btnsave;
    ProgressBar muter;
    movie ubah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);
        txtUserID = (TextView)findViewById(R.id.txtUserID);
        editNama = (EditText) findViewById(R.id.editNama);
        edittanggal = (EditText) findViewById(R.id.edittanggal);
        edtOverview = (EditText) findViewById(R.id.edtOverview);
        btnsave = (Button) findViewById(R.id.btnsave);
        muter = findViewById(R.id.muter);
        ubah = new movie();

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            idMovie = extras.getInt("idMovie");
        }
        txtUserID.setText(""+idMovie);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubah.setTitle(editNama.getText().toString());
                ubah.setOverview(edtOverview.getText().toString());
                ubah.setReleasedate(edittanggal.getText().toString());
                new EditData().execute();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class EditData extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            //kasih loading
            muter.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                String tmptitle= ubah.getTitle().replaceAll(" ", "%20");
                String tmpoverview= ubah.getOverview().replaceAll(" ", "%20");
                String tmpreleasedate= ubah.getReleasedate().replaceAll(" ", "%20");

                String url = "http://192.168.6.230/file/update.php?title="+ tmptitle +"&overview="+tmpoverview+"&releasedate="+tmpreleasedate+"&idMovie="+idMovie+"";
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
                System.out.println("json error : "+json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("hasil json ", "onPostExecute: "+jsonObject.toString());
            muter.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result=jsonObject.getJSONObject("Result");
                    String sukses=Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: "+sukses);
                    if (sukses.equals("true")){
                        Toast.makeText(getApplicationContext(),"Edit sukses",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditScreen.this,HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Edit gagal",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ignored) {
                    System.out.println("erornya "+ignored);
                }
            }else{
            }
        }
    }
}
