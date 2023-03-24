package com.chiragagg5k.bu_news_android.objects;

public class EventsObject {

    private final String eventHeading;
    private final String eventDescription;
    private final String eventDate;

    public EventsObject(String eventHeading, String eventDescription, String eventDate) {
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

    public String getEventDate() {
        return eventDate;
    }

}
