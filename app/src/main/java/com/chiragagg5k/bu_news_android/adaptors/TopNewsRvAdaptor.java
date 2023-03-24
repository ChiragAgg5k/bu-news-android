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

public class TopNewsRvAdaptor extends RecyclerView.Adapter<TopNewsRvAdaptor.ViewHolder> {

    private final List<NewsObject> uploadObjects;
    Context context;

    public TopNewsRvAdaptor(List<NewsObject> uploadObjects, Context context) {
        this.uploadObjects = uploadObjects;
        this.context = context;
    }


    @NonNull
    @Override
    public TopNewsRvAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_news_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return uploadObjects.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TopNewsRvAdaptor.ViewHolder holder, int position) {
        NewsObject uploadCurrent = uploadObjects.get(position);
        holder.title.setText(uploadCurrent.getNewsHeading());

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

        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.top_news_heading);
            image = itemView.findViewById(R.id.top_news_image);
        }
    }
}