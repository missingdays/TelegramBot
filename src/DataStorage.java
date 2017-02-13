/**
 * Created by Makentoshe on 08.02.2017.
 */

import java.io.*;
import java.util.Map;


class DataStorage {

    /**
     * Decoding from binary code.
     * @return downloaded from a data file.
     */
    private Map<String, String> getGroupInfoFromFile(String path) {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            InfoGroupMap igm = (InfoGroupMap)ois.readObject();
            return igm.infoGroup;
        } catch (Exception e){
            e.printStackTrace();
        }
        //wtf?
        return null;
    }

    /**
     * @return downloaded from a data file in string.
     */
    String getGroupInfo(String pathToFile) {
        String data = "";

        for (Map.Entry<String, String> entry : getGroupInfoFromFile(pathToFile).entrySet()) {
            data += entry.getKey().toString() + " - " + entry.getValue().toString() + "\n\n";
        }

        if (data.equals("")) return "List is empty.";
        return data;
    }

    /**
     * Finding group in list and remove it.
     * @param param - parameter for which group is searching in list @param groupInfo
     * @return group name or null if not founded.
     */
    String removeGroupInfo(Map<String, String> groupInfo, String param) {
        String out;

        if (groupInfo.containsKey(param)) {
            out = param;
            groupInfo.remove(param);
            return out;
        }

        for (Map.Entry<String, String> entry : groupInfo.entrySet()) {
            if (entry.getValue().equals(param)) {
                out = entry.getKey().toString();
                groupInfo.remove(entry.getKey());
                return out;
            }
        }
        return null;
    }

    /**
     * Read and decode some data from the file
     * @param path - path to file
     * @param go - special object, where the data will be written
     */
    Object downloadFile(String path, Object go) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return go;
    }

    /** Upload object into file.
     * @param path - path to file
     * @param go - special object, which data will be save
     */
    void uploadFile(String path, Object go){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(go);
            oos.flush();
            oos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
