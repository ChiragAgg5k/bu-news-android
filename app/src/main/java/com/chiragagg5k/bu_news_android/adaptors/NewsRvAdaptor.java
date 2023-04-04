package com.chiragagg5k.bu_news_android.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.NewsDetailActivity;
import com.chiragagg5k.bu_news_android.R;
import com.chiragagg5k.bu_news_android.objects.NewsObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRvAdaptor extends RecyclerView.Adapter<NewsRvAdaptor.ViewHolder> {

    private final List<NewsObject> uploadObjects;
    Context context;

    public NewsRvAdaptor(List<NewsObject> uploadObjects, Context context) {
        this.uploadObjects = uploadObjects;
        this.context = context;
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
        NewsObject uploadCurrent = uploadObjects.get(position);
        holder.title.setText(uploadCurrent.getNewsHeading());
        holder.description.setText(uploadCurrent.getNewsDescription());
        holder.uploader.setText(String.format("- %s", uploadCurrent.getUsername()));

        Picasso.get().load(uploadCurrent.getmImageUrl()).fit().centerCrop().into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(this.context, NewsDetailActivity.class);
            intent.putExtra("news_heading", uploadCurrent.getNewsHeading());
            intent.putExtra("news_description", uploadCurrent.getNewsDescription());
            intent.putExtra("news_image_url", uploadCurrent.getmImageUrl());
            intent.putExtra("news_uploader", uploadCurrent.getUsername());
            intent.putExtra("news_category", uploadCurrent.getCategory());
            intent.putExtra("news_uploadDate", uploadCurrent.getDateInMilliseconds());

            this.context.startActivity(intent);
        });
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