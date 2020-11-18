package com.example.vhackathonevents;

public class EventModel {

    String poster , time , date , committee , link , description , sortingParameter , eventName;

    public EventModel() {
    //for firebase purpose
    }

    public EventModel(String poster, String time, String date, String committee, String link, String description, String sortingParameter, String eventName) {
        this.poster = poster;
        this.time = time;
        this.date = date;
        this.committee = committee;
        this.link = link;
        this.description = description;
        this.sortingParameter = sortingParameter;
        this.eventName = eventName;
    }

    public String getPoster() {
        return poster;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCommittee() {
        return committee;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getSortingParameter() {
        return sortingParameter;
    }
}
