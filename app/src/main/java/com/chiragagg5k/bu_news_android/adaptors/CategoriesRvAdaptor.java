package com.chiragagg5k.bu_news_android.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.R;

import java.util.ArrayList;

/**
 * Adaptor for categories RecyclerView
 */
public class CategoriesRvAdaptor extends RecyclerView.Adapter<CategoriesRvAdaptor.ViewHolder> {

    ArrayList<String> categories;
    CategoryClickListener categoryClickListener;
    Context context;
    int selectedPosition = 0;

    public CategoriesRvAdaptor(ArrayList<String> categories, Context context, CategoryClickListener categoryClickListener) {
        this.categories = categories;
        this.context = context;
        this.categoryClickListener = categoryClickListener;
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

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CategoriesRvAdaptor.ViewHolder holder, int position) {

        if (selectedPosition == position) {
            holder.category.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.category.setTextColor(Color.WHITE);
        }

        holder.category.setText(categories.get(position));

        holder.itemView.setOnClickListener(v -> {
            categoryClickListener.onCategoryClick(position);

            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);

            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    public interface CategoryClickListener {
        void onCategoryClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category_name);
        }
    }
}
