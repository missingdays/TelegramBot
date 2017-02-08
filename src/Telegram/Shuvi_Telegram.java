package Telegram;

/**
 * Created by Makentoshe on 04.02.2017.
 */

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


/**
 * Serializable class for saving data in binary code.
 */
class InfoGroupMap implements Serializable {
    Map<String, String> infoGroup = new HashMap<>();

    InfoGroupMap(Map<String, String> map) {
        infoGroup = map;
    }
}

/**
 * Serializable class for saving data in binary code.
 */
class GroupData implements Serializable {
    Map<Long, String> groupData = new HashMap<>();

    GroupData(Map<Long, String> map){
        groupData = map;
    }
}

/**
 * Serializable class for saving data in binary code.
 */
class Token implements Serializable {
    String token = new String();

    Token(String token) {
        this.token = token;
    }
}

public class Shuvi_Telegram extends TelegramLongPollingBot {
    private Map<String, String> infoGroup = new HashMap<>();
    private Map<Long, String> groupData = new HashMap<>();

    private DataStorage ds = new DataStorage();

    private static Logger log = Logger.getLogger(Shuvi_Telegram.class.getName());

    public static void main(String[] args) {
        log.info("System start");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            log.info("Trying to register bot...");
            telegramBotsApi.registerBot(new Shuvi_Telegram());
            log.info("Success");
        } catch (TelegramApiException e) {
            log.info("Fail");
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "MBFCP_Bot";   //Makentoshe's Bot For Co-oP
    }

    @Override
    public String getBotToken() {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/Token.shuvi"));
            Token token = (Token)ois.readObject();
            return token.token;
        } catch (Exception e){
            e.printStackTrace();
        }
        //wtf?
        return null;

       /* String token = null;
        try {
            Scanner scan = new Scanner(new File("data/Token.txt"));
            while (scan.hasNext())
                token = scan.nextLine();
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token.substring(1);
    */
    }

    @Override
    public void onUpdateReceived(Update update) {
        String text = "";
        Message message = update.getMessage();

        if (message != null && message.hasText()) text = message.getText();

        switch (text) {
            case "/info":
                sendMsg(message, ds.getGroupInfo());
                break;
            case "/info@MBFCP_Bot":
                sendMsg(message, ds.getGroupInfo());
                break;
//-------------------------------------------
            case "/help":
                sendMsg(message, ds.hlpmsg);
                break;
            case "/help@MBFCP_Bot":
                sendMsg(message, ds.hlpmsg);
                break;
//-------------------------------------------
            case "/rng":
                sendMsg(message, ds.rngmsg);
                break;
            case "/rng@MBFCP_Bot":
                sendMsg(message, ds.rngmsg);
                break;
//-------------------------------------------
            case "/rg":
                sendMsg(message, ds.rgmsg);
                break;
            case "/rg@MBFCP_Bot":
                sendMsg(message, ds.rgmsg);
                break;
//-------------------------------------------
            case "/faq":
                sendMsg(message, ds.faqmsg);
                break;
            case "/faq@MBFCP_Bot":
                sendMsg(message, ds.faqmsg);
                break;
//-------------------------------------------
            case "/sd":
                sendMsg(message, ds.setgdmsg);
                break;
            case "/sd@MBFCP_Bot":
                sendMsg(message, ds.setgdmsg);
                break;
//-------------------------------------------
            case "/gd":
                sendMsg(message, ds.getgdmsg);
                break;
            case "/gd@MBFCP_Bot":
                sendMsg(message, ds.getgdmsg);
                break;
//-------------------------------------------
            default: {
                String[] comType = text.split(" ");

                if (comType[0].equals("/rng") || comType[0].equals("/rng@MBFCP_Bot")) {
                    //удаление вызова команды
                    String syntaxis = text.replaceAll(comType[0] + " ", "");
                    //разделение параметров запятой
                    String[] parameters = syntaxis.split(", ");
                    //Загрузка данных
                    infoGroup = ds.getGroupInfoFromFile();
                    //Процесс записи
                    ds.setGroupInfo(infoGroup, parameters[0], parameters[1]);
                    //Запись
                    ds.setGroupInfoToFile(infoGroup);
                    log.info("New group was register: " + parameters[0]);
                    sendMsg(message, "Была создана группа " + parameters[0] + '.');
                }

                if (comType[0].equals("/rg") || comType[0].equals("/rg@MBFCP_Bot")) {
                    //удаление вызова команды
                    String param = text.replaceAll(comType[0] + " ", "");
                    //Загрузка данных
                    infoGroup = ds.getGroupInfoFromFile();
                    //удаление группы
                    String groupname = ds.removeGroupInfo(infoGroup, param);
                    //Запись
                    ds.setGroupInfoToFile(infoGroup);

                    if (groupname != null) {
                        log.info("Group " + groupname + " was remove");
                        sendMsg(message, "Группа " + groupname + " была удалена из списка.");
                    } else {
                        log.info("Group not found.");
                        sendMsg(message, "Group not found.");
                    }
                }

                if (comType[0].equals("/sd") || comType[0].equals("/sd@MBFCP_Bot") ) {
                    //Удаление вызова команды
                    String data = text.replaceAll(comType[0] + " ", "");
                    //Запись
                    groupData.put(message.getChatId(), data);
                    //Сохранение
                    ds.setGroupDataToFile(groupData);
                    sendMsg(message, "Done.");
                }

                if (comType[0].equals("/gd") || comType[0].equals("/gd@MBFCP_Bot")) {
                    groupData = ds.getGroupDataFromFile();
                    if (groupData.containsKey(message.getChatId())) sendMsg(message, groupData.get(message.getChatId()));
                    else sendMsg(message, "Empty");
                }

                break;
            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}


