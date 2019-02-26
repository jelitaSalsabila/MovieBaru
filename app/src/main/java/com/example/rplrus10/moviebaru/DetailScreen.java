package com.example.rplrus10.moviebaru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailScreen extends AppCompatActivity {

    Menu menu;
    ImageView iv_idol;
    TextView tv_name_idol,tv_deskripsi,tv_tanggal;
    String title;
    String overview;
    int idMovie;
    String poster_path;
    String releasedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        iv_idol = (ImageView) findViewById(R.id.iv_img_idol);
        tv_name_idol = (TextView) findViewById(R.id.tv_nama_idol);
        tv_deskripsi = (TextView) findViewById(R.id.tv_deskripsi_idol);
        tv_tanggal = findViewById(R.id.tv_tanggal);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            title = bundle.getString("title");
            overview = bundle.getString("overview");
            poster_path = bundle.getString("poster_path");
            releasedate = bundle.getString("releasedate");
            idMovie = bundle.getInt("idMovie");
        }
        tv_name_idol.setText(title);
        tv_deskripsi.setText(overview);
        tv_tanggal.setText(releasedate);
        Glide.with(DetailScreen.this)
                .load(poster_path)
                .into(iv_idol);
    }
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                Intent intent = new Intent(this,EditScreen.class);
                intent.putExtra("idMovie",idMovie);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}