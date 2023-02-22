package com.chiragagg5k.bu_news_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRvAdaptor extends RecyclerView.Adapter<CategoriesRvAdaptor.ViewHolder> {

    ArrayList<String> categories;
    Fragment fragment;

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

    @Override
    public void onBindViewHolder(@NonNull CategoriesRvAdaptor.ViewHolder holder, int position) {

        holder.category.setText(categories.get(position));
        AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
        this.fragment = new GeneralNewsFragment();

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.newsLayout, fragment).addToBackStack(null).commit();

        holder.itemView.setOnClickListener(v -> {
            YoYo.with(Techniques.FadeIn).duration(400).playOn(holder.itemView);

            switch (position) {
                case 0:
                    if (this.fragment instanceof GeneralNewsFragment)
                        return;

                    this.fragment = new GeneralNewsFragment();
                    break;

                case 1:
                    if (this.fragment instanceof ClubsRelatedFragment)
                        return;

                    this.fragment = new ClubsRelatedFragment();
                    break;

                case 2:
                    if (this.fragment instanceof EventsNewsFragment)
                        return;

                    this.fragment = new EventsNewsFragment();
                    break;

                case 3:
                    if (this.fragment instanceof SportsNewsFragment)
                        return;

                    this.fragment = new SportsNewsFragment();
                    break;
            }

            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.newsLayout, this.fragment).addToBackStack(null).commit();
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category_name);
        }
    }
}
