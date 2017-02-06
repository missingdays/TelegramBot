package Telegram;

import com.fasterxml.jackson.databind.deser.Deserializers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Makentoshe on 06.02.2017.
 */
public class DataStorage {

    private static Logger log = Logger.getLogger(DataStorage.class.getName());

    /**
     * @return Загруженные данные из файла.
     */
    public Map<String, String> getGroupInfoFromData() {
        Map<String, String> groupInfo = new HashMap<>();
        String data;
        try {

            log.info("Scanning...");
            Scanner scan = new Scanner(new File("data/GroupInfo.txt"));

            log.info("Processing...");
            while (scan.hasNext()) {
                //Сканируем построчно
                data = scan.nextLine();
                //превращаем строку в 2
                String[] dataArr = data.split(" ");
                //Создаем массив байтов для перевода в строку.
                byte[] bytes = new byte[512];

                String[] param = new String[1];

                for (int j = 0;j < 2; j++){
                    for (int i = 0; i < dataArr[j].length(); i++){
                        //берем из строки символ и приводим в байт
                        byte b = (byte)dataArr[j].charAt(i);
                        //записываем байт в массив
                        bytes[i] = b;
                    }

                }

                //groupInfo.put(, );
            }

            log.info("Closing...");
            scan.close();
            log.info("Success");
        } catch (Exception e) {
            log.warning("Fail");
            e.printStackTrace();
        }

        return groupInfo;
    }

    /**
     * Кодирует в бинарный код
     * Сохраняет @param groupInfo в файл.
     */
    public void setGroupInfoToData(Map<String, String> groupInfo) {
        FileWriter fr = null;
        String data = "";

        try {
            log.info("Building data...");
            for (Map.Entry entry : groupInfo.entrySet()) {
                //Динамический массив интов для сохранения посимвольного сохранения строки в интах
                ArrayList ints = new ArrayList();
                //По всей длинне строки
                for (int i = 0; i < entry.getKey().toString().length(); i++){
                    //присваивать массиву символ, приведенный в инт
                    ints.add(i, (int)entry.getKey().toString().charAt(i));
                }
                String key = "";
                for (int i = 0; i < ints.toArray().length; i++){
                    key += ints.toArray()[i] + ", ";
                }
                ints.clear();

                for (int i = 0; i < entry.getValue().toString().length(); i++){
                    ints.add(i, (int)entry.getValue().toString().charAt(i));
                }
                String value = "";
                for (int i = 0; i < ints.toArray().length; i++){
                    value += ints.toArray()[i] + ", ";
                }

                //START FROM HERE
                System.out.println(key + "   " + value);



                /*
                byte[] bytes = entry.getKey().toString().getBytes();
                data += bytes + " ";
                bytes = entry.getValue().toString().getBytes();
                data += bytes + "\r\n";
                System.out.println(bytes);

                data += Base64.getDecoder().decode(entry.getKey().toString());
                data += " ";
                data += Base64.getDecoder().decode(entry.getValue().toString());
                data += "\r\n";
                */
            }
            log.info("Done");

            fr = new FileWriter("data/GroupInfo.txt");

            log.info("Saving");
            fr.write(data);

        } catch (IOException e) {
            log.warning("Fail");
            e.printStackTrace();

        } finally {
            try {
                fr.close();

            } catch (IOException e) {
                log.warning("Fail");
                e.printStackTrace();
            }
        }
        log.info("Success");
    }

    /**
     * Добавляет данные @param groupname и @param linq к @param groupInfo
     */
    public void setGroupInfo(Map<String, String> groupInfo, String groupname, String linq) {
        groupInfo.put(groupname, linq);
    }

    /**
     * @return загруженные из файла данные строкой
     */
    public String getGroupInfo() {
        String data = "";

        for (Map.Entry entry : getGroupInfoFromData().entrySet()) {
            data += entry.getKey().toString() + " - " + entry.getValue().toString() + "\n\n";
        }

        return data;
    }


}
