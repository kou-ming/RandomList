package com.example.randomlist;

public class List_Info {
    private String List_name;

    public List_Info(String List_name){
        this.List_name = List_name;
    }
    public String getName(){
        return List_name;
    }

    public void setName(String List_name){
        this.List_name = List_name;
    }
}
