package com.example.rplrus10.moviebaru;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RviewHolder extends RecyclerView.ViewHolder {
    public TextView textview_judul;
    public ImageView image_view;
    public Button btn_details;
    public Button btn_delete;

    public RviewHolder(View layoutView) {
        super(layoutView);

        textview_judul = (TextView)itemView.findViewById(R.id.textview_judul);
        image_view = (ImageView) itemView.findViewById(R.id.image_view);
        btn_details = (Button) itemView.findViewById(R.id.btn_details);
        btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
    }
}
