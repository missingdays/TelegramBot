/**
 * Created by Makentoshe on 08.02.2017.
 */

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

class Command {
    String text;
    private String botName;
    String syntax;

    Command(String text, String botName) {
        this.text = text;
        this.botName = botName;
    }

    /**
     * @return -1, if (@param text) is not a command
     *          0, if (@param text) is unknown command
     *          else return command number, begin from 1
     *          twice digit like 44 for command with syntax.
     */
    int textHandler(){
        String[] avaliableCommands={"help", "info", "faq", "rng", "rg", "sd", "gd"};
        if (text.charAt(0) != '/') return -1;
        text = text.replace("/", "");
        String[] cmd = text.split(" ");
        for (int i = 0; i<avaliableCommands.length; i++){
            if (cmd[0].equals(avaliableCommands[i]) || cmd[0].equals(avaliableCommands[i]+botName)){
                if (cmd.length > 1) {
                    syntax = text.replace(cmd[0] + " ","");
                    System.out.println(syntax);
                    return (i+1)*10 + (i+1);
                }
                return i+1;
            }
        }
        return 0;
    }
}

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
class BotData implements Serializable {
    String token;
    String username;

    BotData(String token, String username) {
        this.token = token;
        this.username = username;
    }
}

public class TelegramBot extends TelegramLongPollingBot {

    private static Logger log = Logger.getLogger(TelegramBot.class.getName());
    private DataStorage ds = new DataStorage();

    public static void main(String[] args) {
        log.info("System start");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            log.info("Trying to register bot...");
            telegramBotsApi.registerBot(new TelegramBot());
            log.info("Success");
        } catch (TelegramApiException e) {
            log.info("Fail");
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("H:/Projects/Java/Shuvi/data/BotData.shuvi"));
            BotData token = (BotData)ois.readObject();
            return token.username;
        } catch (Exception e){
            e.printStackTrace();
        }
        //wtf?
        return null;
    }

    @Override
    public String getBotToken() {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("H:/Projects/Java/Shuvi/data/BotData.shuvi"));
            BotData bd = (BotData)ois.readObject();
            return bd.token;
        } catch (Exception e){
            e.printStackTrace();
        }
        //wtf?
        return null;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Command command;
        if (message != null && message.hasText()) command = new Command(message.getText(), getBotUsername()); //text = message.getText();
        else return;

        switch (command.textHandler()) {
            case 1: sendMsg(message, ds.hlpmsg); break;
            case 2: sendMsg(message, ds.getGroupInfo()); break;
            case 3: sendMsg(message, ds.faqmsg); break;
            case 4: sendMsg(message, ds.rngmsg); break;
            case 5: sendMsg(message, ds.rgmsg); break;
            case 6: sendMsg(message, ds.setgdmsg); break;

            case 44: {
                String syntaxis = command.syntax;
                String[] syntax = syntaxis.split(", ");
                //Далее загрузка из файла
                //Запись в файл
                //Сохранение
                //Сообщение
            }
            case 55:
            case 66:
            case 77:

            default: {
                /*
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
                */

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
