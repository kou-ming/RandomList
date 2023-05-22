package com.example.randomlist;


import java.util.ArrayList;

public class Song {
    private String name;
    private String channel;
    private String duration;
    private String link;
    private String owner;

    ArrayList<String> SongLabels = new ArrayList<>();

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

    public int getLabelsize(){
        return SongLabels.size();
    }

    public void addLabel(String label){
        SongLabels.add(label);
    }

    public String getLabel(int index){
        return SongLabels.get(index);
    }

    public void setOwner(String owner){
        this.owner = owner;
    }
}
