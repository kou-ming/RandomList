package com.example.randomlist;

import java.io.*;
import java.util.Scanner;

public class CsvReader {
    public static void main(String[] args) throws FileNotFoundException {
        String file_path = "src\\main\\java\\csvfile\\input.csv";
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(file_path));
            while((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                for(String index: row) {
                    System.out.printf("%-20s", index);
                }
                System.out.println();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
