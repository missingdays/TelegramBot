package Telegram;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Makentoshe on 05.02.2017.
 */
public class WorkInFile {

    public static void main(String[] args) {

        String[] strings = {"Sas", "linq"};

        // преобразуем нашу строку в массив байт
        byte[] byteArray1 = strings[0].getBytes();
        byte[] byteArray2 = strings[1].getBytes();
        System.out.println(Arrays.toString(byteArray1) + " " +  Arrays.toString(byteArray2));

        // конвертируем байты в строку
        String param1 = new String(byteArray1);
        String param2 = new String(byteArray2);
        System.out.println(param1 + " - " + param2);
    }
}
