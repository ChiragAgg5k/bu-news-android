package com.chiragagg5k.bu_news_android.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.R;
import com.chiragagg5k.bu_news_android.objects.LostFoundObject;

import java.util.ArrayList;

public class LostFoundAdaptor extends RecyclerView.Adapter<LostFoundAdaptor.ViewHolder>{

    ArrayList<LostFoundObject> lostFoundObjects;
    boolean isLost;

    public LostFoundAdaptor(ArrayList<LostFoundObject> lostFoundObjects, boolean isLost) {
        this.lostFoundObjects = lostFoundObjects;
        this.isLost = isLost;
    }

    @NonNull
    @Override
    public LostFoundAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_found_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LostFoundAdaptor.ViewHolder holder, int position) {
        holder.itemName.setText(lostFoundObjects.get(position).getItemName());
        holder.itemDescription.setText(lostFoundObjects.get(position).getItemDescription());
    }

    @Override
    public int getItemCount() {
        return lostFoundObjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemLostFoundName);
            itemDescription = itemView.findViewById(R.id.itemLostFoundDescription);
        }
    }
}