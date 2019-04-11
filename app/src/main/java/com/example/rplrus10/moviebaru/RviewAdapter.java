package com.example.rplrus10.moviebaru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {

    private ArrayList<movie> movieArrayList;
    Context context;

    public RviewAdapter(Context context, ArrayList<movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public RviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        RviewHolder rcv = new RviewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RviewHolder holder, final int position) {
        final movie movie = movieArrayList.get(position);
        Glide.with(context)
                .load(movie.getPoster_path())
                .into(holder.image_view);
        holder.textview_judul.setText(movieArrayList.get(position).getTitle());
        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = movieArrayList.get(position).getTitle();
                final String overview = movieArrayList.get(position).getOverview();
                final String poster_path = movieArrayList.get(position).getPoster_path();
                final String releasedate = movieArrayList.get(position).getReleasedate();
                final int idMovie = movieArrayList.get(position).getIdMovie();
                Intent i = new Intent(context.getApplicationContext(), DetailScreen.class);
                i.putExtra("title", title);
                i.putExtra("overview", overview);
                i.putExtra("poster_path", poster_path);
                i.putExtra("releasedate", releasedate);
                i.putExtra("idMovie", idMovie);
                context.startActivity(i);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
                builder.setTitle("Delete");
                builder.setCancelable(true);
                builder.setMessage("are you sure to delete this item?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        final movie movie = movieArrayList.get(position);

                        String url = "http://192.168.6.79/file/delete.php?idMovie=" + movieArrayList.get(position).getIdMovie();
                        System.out.println("url ku " + url);
                        new deleteData(url).execute();
                        movieArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, movieArrayList.size());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    class deleteData extends AsyncTask<Void, Void, JSONObject> {

        public String url;

        public deleteData(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            //kasih loading
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
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
                System.out.println("json error : " + json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);
                    if (sukses.equals("true")) {
                        Toast.makeText(context, "Delete sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Delete gagal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    System.out.println("erornya " + ignored);
                }
            } else {
            }
        }
    }

}