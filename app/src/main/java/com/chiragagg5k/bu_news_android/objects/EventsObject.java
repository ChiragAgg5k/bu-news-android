package com.chiragagg5k.bu_news_android.objects;

import com.chiragagg5k.bu_news_android.UtilityClass;

import java.time.LocalDate;

public class EventsObject {

    private final String eventHeading;
    private final String eventDescription;
    private final String eventDate;
    private final LocalDate eventLocalDate;

    public EventsObject(String eventHeading, String eventDescription, LocalDate eventDate) {
        this.eventHeading = eventHeading;
        this.eventDescription = eventDescription;
        this.eventLocalDate = eventDate;
        this.eventDate = UtilityClass.getDate(eventDate);
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

    public LocalDate getEventLocalDate() {
        return eventLocalDate;
    }
}
