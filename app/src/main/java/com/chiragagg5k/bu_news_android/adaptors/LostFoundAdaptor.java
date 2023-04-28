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

import com.chiragagg5k.bu_news_android.LostFoundDetailActivity;
import com.chiragagg5k.bu_news_android.R;
import com.chiragagg5k.bu_news_android.objects.LostFoundObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LostFoundAdaptor extends RecyclerView.Adapter<LostFoundAdaptor.ViewHolder> {

    private final Context context;
    ArrayList<LostFoundObject> lostFoundObjects;
    boolean isLost;

    public LostFoundAdaptor(ArrayList<LostFoundObject> lostFoundObjects, boolean isLost, Context context) {
        this.lostFoundObjects = lostFoundObjects;
        this.isLost = isLost;
        this.context = context;
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
        holder.itemLocation.setText(lostFoundObjects.get(position).getItemLocation());

        Picasso.get().load(lostFoundObjects.get(position).getItemImageURL()).fit().centerCrop().into(holder.itemImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LostFoundDetailActivity.class);
            intent.putExtra("item_name", lostFoundObjects.get(position).getItemName());
            intent.putExtra("item_description", lostFoundObjects.get(position).getItemDescription());
            intent.putExtra("item_location", lostFoundObjects.get(position).getItemLocation());
            intent.putExtra("item_image_url", lostFoundObjects.get(position).getItemImageURL());
            intent.putExtra("item_date", lostFoundObjects.get(position).getItemDate());
            intent.putExtra("uploader_uid", lostFoundObjects.get(position).getToContactUID());
            intent.putExtra("uploader_contact", lostFoundObjects.get(position).getContactNo());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lostFoundObjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemDescription, itemLocation;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemLostFoundName);
            itemDescription = itemView.findViewById(R.id.itemLostFoundDescription);
            itemLocation = itemView.findViewById(R.id.itemLostFoundLocation);
            itemImage = itemView.findViewById(R.id.itemLostFoundImage);
        }
    }
}
