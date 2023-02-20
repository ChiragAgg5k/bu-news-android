package com.chiragagg5k.bu_news_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriesRvAdaptor extends RecyclerView.Adapter<CategoriesRvAdaptor.ViewHolder>{

    ArrayList<String> categories;

    public CategoriesRvAdaptor(ArrayList<String> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoriesRvAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRvAdaptor.ViewHolder holder, int position) {
        holder.category.setText(categories.get(position));

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Clicked on " + categories.get(position), Toast.LENGTH_SHORT).show();
        });
    }
}
