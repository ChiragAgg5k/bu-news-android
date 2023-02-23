package com.chiragagg5k.bu_news_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsRvAdaptor extends RecyclerView.Adapter<NewsRvAdaptor.ViewHolder> {

    private final List<UploadObject> uploadObjects;

    public NewsRvAdaptor(List<UploadObject> uploadObjects, Context context) {
        this.uploadObjects = uploadObjects;
    }

    @NonNull
    @Override
    public NewsRvAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return uploadObjects.size();
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRvAdaptor.ViewHolder holder, int position) {
        UploadObject uploadCurrent = uploadObjects.get(position);
        holder.title.setText(uploadCurrent.getNewsHeading());
        holder.description.setText(uploadCurrent.getNewsDescription());
        holder.uploader.setText(uploadCurrent.getUsername());

        Picasso.get().load(uploadCurrent.getmImageUrl()).fit().centerCrop().into(holder.image);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, uploader;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_heading);
            description = itemView.findViewById(R.id.news_description);
            image = itemView.findViewById(R.id.news_image);
            uploader = itemView.findViewById(R.id.news_uploader);
        }
    }
}