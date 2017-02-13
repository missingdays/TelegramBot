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
import java.util.*;
import java.util.logging.Logger;

class Command {
    private String text;
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
                    //removing command word
                    syntax = text.replace(cmd[0] + " ","");
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
class StaticData implements Serializable {
    private List list = new LinkedList();

    StaticData(List list) {
        this.list = list;
    }

    public List getList() {
        return list;
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
    private String groupInfoPath = "data/GroupInfo.shv";
    private String groupDataPath = "data/GroupData.shv";
    private String botDataPath = "data/BotDatas.shv";
    private String staticDataPath = "data/StaticData.shv";

    private static Logger log = Logger.getLogger(TelegramBot.class.getName());
    private static DataStorage ds = new DataStorage();

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
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(botDataPath));
            BotData token = (BotData)ois.readObject();
            return token.username;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBotToken() {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(botDataPath));
            BotData bd = (BotData)ois.readObject();
            return bd.token;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Command command;
        if (message != null && message.hasText()) command = new Command(message.getText(), getBotUsername());
        else return;

        switch (command.textHandler()) {
            case 1: sendMsg(message, getStaticData(ds, staticDataPath, 0));break; //help
            case 2: sendMsg(message, ds.getGroupInfo(groupInfoPath)); break;
            case 3: sendMsg(message, getStaticData(ds, staticDataPath, 1));break; //faq
            case 4: sendMsg(message, getStaticData(ds, staticDataPath, 2));break; //rng
            case 5: sendMsg(message, getStaticData(ds, staticDataPath, 3));break; //rg
            case 6: sendMsg(message, getStaticData(ds, staticDataPath, 4));break; //sd

            case 44: {
                String[] syntax = command.syntax.split("; ");
                InfoGroupMap igmObj = (InfoGroupMap) ds.downloadFile(groupInfoPath, new InfoGroupMap(new HashMap<>()));
                igmObj.infoGroup.put(syntax[0], syntax[1]);
                ds.uploadFile(groupInfoPath, igmObj);
                log.info("New group was register: " + syntax[0] + "\nLink: " + syntax[1]);
                sendMsg(message, "Group with name \"" + syntax[0] + "\" was created.");
                break;
            }
            case 55: {
                InfoGroupMap igmObj = (InfoGroupMap) ds.downloadFile(groupInfoPath, new InfoGroupMap(new HashMap<>()));
                String groupname = ds.removeGroupInfo(igmObj.infoGroup, command.syntax);
                ds.uploadFile(groupInfoPath, igmObj);
                if (groupname != null) {
                    log.info("Group " + groupname + " was remove");
                    sendMsg(message, "Group " + groupname + " was remove from list.");
                } else {
                    log.info("Group not found");
                    sendMsg(message, "Group not found.");
                }
                break;
            }
            case 66: {
                GroupData gdObj = (GroupData) ds.downloadFile(groupDataPath, new GroupData(new HashMap<>()));
                gdObj.groupData.put(message.getChatId(), command.syntax);
                ds.uploadFile(groupDataPath, gdObj);
                log.info("Data in group " + message.getChatId() + " was update");
                sendMsg(message, "Done.");
                break;
            }
            case 7: {
                GroupData gdObj = (GroupData) ds.downloadFile(groupDataPath, new GroupData(new HashMap<>()));
                if (gdObj.groupData.containsKey(message.getChatId()))
                    sendMsg(message, gdObj.groupData.get(message.getChatId()));
                else sendMsg(message, "Empty.");
            }
            default: break;
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

    private String getStaticData(DataStorage dataStorage, String path, int index){
        StaticData obj = (StaticData) dataStorage.downloadFile(path, new StaticData(new LinkedList()));
        return obj.getList().get(index).toString();
    }
}
