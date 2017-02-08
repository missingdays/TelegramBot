import java.io.*;
import java.util.Map;

/**
 * Created by Makentoshe on 08.02.2017.
 */
public class DataStorage {

    /**
     * Декодирует из бинарного кода.
     * @return Загруженные данные из файла.
     */
    Map<String, String> getGroupInfoFromFile() {
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
    void setGroupInfoToFile(Map<String, String> groupInfo) {
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

        for (Map.Entry entry : getGroupInfoFromFile().entrySet()) {
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

    Map<Long, String> getGroupDataFromFile(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/GroupData.shuvi"));
            GroupData gd = (GroupData) ois.readObject();
            return gd.groupData;
        } catch (Exception e){
            e.printStackTrace();
        }
        //wtf?
        return null;
    }

    void setGroupDataToFile(Map<Long, String> groupData) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/GroupData.shuvi"));
            GroupData gd = new GroupData(groupData);
            oos.writeObject(gd);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String faqmsg = "1. Создавать надо группу, а не канал \n" +
            "2. Из веба создать группу нельзя(можно) \n" +
            "3. Когда создаешь, надо кого-то пригласить. Можно приглашать @MBFCP_Bot\n" +
            "4. Лучше сразу апдейтить до супергруппы, потому что меняется инвайт линк\n" +
            "5. Админов можно назначать правой кнопкой мыши (без создания идентификатора)";

    String rngmsg = "Синтаксис: /rng название группы, ссылка на конфу.\n" +
            "Пример: /rng Японский язык, https://t.me/joinchat/AAAAAEJlNDlO63fiUtnrvQ\n";

    String rgmsg = "Синтаксис: /rg название группы или ссылка на конфу.\n" +
            "Пример: /rg Японский язык \n" +
            "Пример: /rg https://t.me/joinchat/AAAAAEJlNDlO63fiUtnrvQ\n";

    String hlpmsg = "Command List:\n" +
            "/info - информация обо всех доступных группах на данный момент\n" +
            "/faq - информация о создании групп\n" +
            "/rng - записать группу\n" +
            "/rg - удалить группу\n" +
            "/sd - добавить данные группы\n" +
            "/gd - показать данные группы\n" +
            "Для вывода данных команды, введите её без параметров. Пример: /rg";

    String setgdmsg = "Синтаксис: /sd информация.\n" +
            "Пример: /sd Книги для ознакомления:\n" +
            "Книга 1, Книга 2, Книга3..." +
            "Закрепляет записанную информацию";
}
