package com.chiragagg5k.bu_news_android.objects;

public class EventsObject {

    private final String eventHeading;
    private final String eventDescription;
    private final long eventDate;

    @SuppressWarnings("unused")
    public EventsObject() {
        this.eventHeading = "";
        this.eventDescription = "";
        this.eventDate = 0;
    }

    public EventsObject(String eventHeading, String eventDescription, long eventDate) {
        this.eventHeading = eventHeading;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
    }

    public String getEventHeading() {
        return eventHeading;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public long getEventDate() {
        return eventDate;
    }
}
