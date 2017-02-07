package Telegram;

import java.io.*;
import java.util.*;

/**
 * Created by Makentoshe on 06.02.2017.
 */

class DataStorage {

    /**
     * @return Загруженные данные из файла.
     */
    Map<String, String> getGroupInfoFromData() {

        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/GroupInfo.shuvi"));
            InfoGroupMap igm = (InfoGroupMap)ois.readObject();
            return igm.infoGroup;
        } catch (Exception e){
            e.printStackTrace();
        }
        //wtf?
        return null;
    }

    /**
     * Кодирует в бинарный код
     * Сохраняет @param groupInfo в файл data/GroupInfo.shuvi.
     */
    void setGroupInfoToData(Map<String, String> groupInfo) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/GroupInfo.shuvi"));
            InfoGroupMap igm = new InfoGroupMap(groupInfo);
            oos.writeObject(igm);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет данные @param groupname и @param linq к @param groupInfo
     */
    void setGroupInfo(Map<String, String> groupInfo, String groupname, String linq) {
        groupInfo.put(groupname, linq);
    }

    /**
     * @return загруженные из файла данные в строке.
     */
    String getGroupInfo() {
        String data = "";

        for (Map.Entry entry : getGroupInfoFromData().entrySet()) {
            data += entry.getKey().toString() + " - " + entry.getValue().toString() + "\n\n";
        }

        if (data.equals("")) return "List is empty.";
        return data;
    }

    /**
     * Ищет группу в списке и удаляет её.
     * @param param - параметр, по которому ищется группа в списке @param groupInfo
     * @return название группы, или null, если таковое не найдено
     */
    String removeGroupInfo(Map<String, String> groupInfo, String param) {
        String out;
        //Удаление по ключу
        if (groupInfo.containsKey(param)) {

            out = param;
            groupInfo.remove(param);

            return out;
        }
        //Удаление по значению
        for (Map.Entry entry : groupInfo.entrySet()) {

            if (entry.getValue().equals(param)) {

                out = entry.getKey().toString();
                groupInfo.remove(entry.getKey());

                return out;
            }
        }
        return null;
    }
}

