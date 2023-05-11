package com.example.randomlist;

public class Song {
    private String name;
    private String channel;
    private String duration;
    private String link;

    public Song(String name, String channel, String duration, String link){
        this.name = name;
        this.channel = channel;
        this.duration = duration;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getChannel() {
        return channel;
    }

    public String getDuration() {
        return duration;
    }

    public String getLink() {
        return link;
    }
}
