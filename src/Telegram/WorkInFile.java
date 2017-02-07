package Telegram;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Makentoshe on 05.02.2017.
 */
public class WorkInFile {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Данные по всем группам
        Map<String, String> group = new HashMap<>();
        group.put("Groupname","Linq");
        group.put("Another Groupname","Another Linq");

        FileOutputStream fos = new FileOutputStream("data/GroupInfo.shuvi");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        InfoGroupMap igm = new InfoGroupMap(group);

        oos.writeObject(igm);
        oos.flush();
        oos.close();
        System.out.println("Serealization done");

        FileInputStream fis = new FileInputStream("data/GroupInfo.shuvi");
        ObjectInputStream oin = new ObjectInputStream(fis);
        InfoGroupMap igm2  = (InfoGroupMap)oin.readObject();

        for (Map.Entry entry : igm2.infoGroup.entrySet()) {
            System.out.println(entry.getKey()+" ???? " + entry.getValue());
        }

    }
}


