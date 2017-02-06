package Telegram;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Makentoshe on 05.02.2017.
 */
public class WorkInFile {

    public static void main(String[] args) {
        String data = "Это тестовая строка для записи в файл";
        String path = "data/FileWriter.txt";
        String data2 = "Это 2 тестовая строка для записи в файл";
        String path2 = "data/FileWriter2.txt";

        FileWriter(path, data);
        FileWriter(path2, data2);

        System.out.println(getToken().charAt(1));
    }




    private static void FileWriter(String path, String data) {
        File file = new File(path);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getToken(){
        String s = "";

        try {
            Scanner scan = new Scanner(new File("data/Token.txt"));
            while (scan.hasNext())
                s += scan.nextLine();
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


}
