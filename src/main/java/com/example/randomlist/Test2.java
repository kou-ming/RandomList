package com.example.randomlist;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import java.io.IOException;
//
//public class Test2 {
//
//    public static void main(String[] args) {
//        String playlistUrl = "https://www.youtube.com/playlist?list=PLSkq1u7ewbUO5oC1jAdQWx0c20aoTmy7E";
//
//        try {
//            Document doc = Jsoup.connect(playlistUrl).get();
//            Elements videoElements = doc.select("a.playlist-video");
//
//            for (Element videoElement : videoElements) {
//                String title = videoElement.attr("title");
//                String videoUrl = "https://www.youtube.com" + videoElement.attr("href");
//
//                System.out.println("Title: " + title);
//                System.out.println("URL: " + videoUrl);
//                System.out.println("-----------------------");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("wow");
//        }
//    }
//}
