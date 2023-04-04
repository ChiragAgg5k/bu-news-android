package com.chiragagg5k.bu_news_android.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.R;
import com.chiragagg5k.bu_news_android.UtilityClass;
import com.chiragagg5k.bu_news_android.objects.EventsObject;

import java.util.List;

public class EventsRvAdaptor extends RecyclerView.Adapter<EventsRvAdaptor.ViewHolder> {

    private final List<EventsObject> eventObjects;
    Context context;
    int previousExpandedPosition = -1;
    int mExpandedPosition = -1;

    public EventsRvAdaptor(List<EventsObject> eventsObjects, Context context) {
        this.eventObjects = eventsObjects;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsRvAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRvAdaptor.ViewHolder holder, int position) {
        holder.event_heading.setText(eventObjects.get(position).getEventHeading());
        holder.event_description.setText(eventObjects.get(position).getEventDescription());

        final boolean isExpanded = holder.getAdapterPosition() == mExpandedPosition;
        if (isExpanded) {
            holder.event_heading.setMaxLines(2);
            holder.event_description.setMaxLines(3);
        } else {
            holder.event_heading.setMaxLines(1);
            holder.event_description.setMaxLines(1);
        }
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(v -> {
            mExpandedPosition = isExpanded ? -1 : holder.getAdapterPosition();
            notifyItemChanged(previousExpandedPosition);
            notifyItemChanged(position);
        });

        String date = UtilityClass.getDate(eventObjects.get(holder.getAdapterPosition()).getEventDate());
        String day = date.split(",")[0];
        String month = date.split(",")[1].trim();

        holder.event_date.setText(String.format("%s\n%s", day, month));
    }

    @Override
    public int getItemCount() {
        return eventObjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView event_heading, event_description, event_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event_heading = itemView.findViewById(R.id.event_heading);
            event_description = itemView.findViewById(R.id.event_description);
            event_date = itemView.findViewById(R.id.event_date);
        }
    }
}
