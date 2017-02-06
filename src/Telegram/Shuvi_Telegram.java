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

public class Shuvi_Telegram extends TelegramLongPollingBot {

    private DataStorage ds = new DataStorage();

    //Данные по всем группам
    private Map<String, String> infoGroup = new HashMap<>();
    //Данные групп
    private Map<Long, String> groupData = new HashMap<>();



    private String faqmsg = "1. Создавать надо группу, а не канал \n" +
            "2. Из веба создать группу нельзя \n" +
            "3. Когда создаешь, надо кого-то пригласить. Можно приглашать @MBFCP_Bot\n" +
            "4. Лучше сразу апдейтить до супергруппы, потому что меняется инвайт линк\n" +
            "5. Админов можно назначать правой кнопкой мыши (без создания идентификатора)";

    private String rngmsg = "Синтаксис: /regnewgroup название группы, ссылка на конфу.\n" +
            "Пример: /regnewgroup Японский язык, https://t.me/joinchat/AAAAAEJlNDlO63fiUtnrvQ\n";

    private String rgmsg = "Синтаксис: /removegroup название группы или ссылка на конфу.\n" +
            "Пример: /removegroup Японский язык \n" +
            "Пример: /removegroup https://t.me/joinchat/AAAAAEJlNDlO63fiUtnrvQ\n";

    private String hlpmsg = "Command List:\n" +
            "/info - информация обо всех доступных группах на данный момент\n" +
            "/faq - информация о создании групп\n" +
            "/regnewgroup - записать группу\n" +
            "/removegroup - удалить группу\n" +
            "/setgd - добавить данные группы\n" +
            "/remgd - cтереть данные группы\n" +
            "/getgd - показать данные группы\n" +
            "Для вывода данных команды, введите её без параметров. Пример: /removegroup";

    private String setgdmsg = "Синтаксис: /setgd информация.\n" +
            "Пример: /setgd Книги для ознакомления:\n" +
            "Книга 1, Книга 2, Книга3..." +
            "Закрепляет записанную информацию";

    private String getgdmsg = "Синтаксис: /getgd .\n" +
            "Выводит закрепленную в группе информацию.";

    private String remgdmsg = "Синтаксис: /remgd .\n" +
            "Удаляет закрепленную в группе информацию.";


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
        String token = null;

        try {
            Scanner scan = new Scanner(new File("data/Token.txt"));
            while (scan.hasNext())
                token = scan.nextLine();
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token.substring(1);
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
                sendMsg(message, hlpmsg);
                break;
            case "/help@MBFCP_Bot":
                sendMsg(message, hlpmsg);
                break;
//-------------------------------------------
            case "/regnewgroup":
                sendMsg(message, rngmsg);
                break;
            case "/regnewgroup@MBFCP_Bot":
                sendMsg(message, rngmsg);
                break;
//-------------------------------------------
            case "/removegroup":
                sendMsg(message, rgmsg);
                break;
            case "/removegroup@MBFCP_Bot":
                sendMsg(message, rgmsg);
                break;
//-------------------------------------------
            case "/faq":
                sendMsg(message, faqmsg);
                break;
            case "/faq@MBFCP_Bot":
                sendMsg(message, faqmsg);
                break;
//-------------------------------------------
            case "/setgd":
                sendMsg(message, setgdmsg);
                break;
            case "/setgd@MBFCP_Bot":
                sendMsg(message, setgdmsg);
                break;
//-------------------------------------------
            default: {
                String[] comType = text.split(" ");

                if (comType[0].equals("/regnewgroup")) {
                    //удаление вызова команды
                    String syntaxis = text.replaceAll(comType[0] + " ", "");

                    //разделение параметров запятой
                    String[] parameters = syntaxis.split(", ");



                    //Загрузка БД
                    infoGroup = ds.getGroupInfoFromData();

                    //Процесс записи
                    ds.setGroupInfo(infoGroup, parameters[0], parameters[1]);

                    //Запись в БД
                    ds.setGroupInfoToData(infoGroup);
                    log.info("New group was register: " + parameters[0]);

                    sendMsg(message, "Была создана группа " + parameters[0] + '.');
                }

                if (comType[0].equals("/removegroup")) {
                    //удаление вызова команды
                    String parameter = text.replaceAll(comType[0] + " ", "");
                    //удаление группы
                    String groupname = removeDataFromMap(infoGroup, parameter);
                    log.info("Group " + groupname + " was remove");
                    sendMsg(message, "Группа " + groupname + " была удалена из списка.");
                }

                if (comType[0].equals("/setgd@MBFCP_Bot")) {
                    //удаление вызова команды
                    String data = text.replaceAll(comType[0] + " ", "");
                    setGroupData(message.getChatId(), data);
                    log.info("Group date was set in chat: " + message.getChatId().toString());
                    sendMsg(message, "Successful");
                }

                if (comType[0].equals("/getgd@MBFCP_Bot")) {
                    //удаление вызова команды
                    String data = text.replaceAll(comType[0] + " ", "");
                    log.info("Group date was get in chat: " + message.getChatId().toString());
                    sendMsg(message, getGroupData(message.getChatId()));
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

    //данные удаляются, если найден ключ или значение.
    private String removeDataFromMap(Map<String, String> hashMap, String parameter) {
        String s;
        //Удаление по ключу
        if (hashMap.containsKey(parameter)) {
            s = parameter;
            hashMap.remove(parameter);
            return s;
        }
        //Удалене по значению
        //проходимся по хешу
        for (Map.Entry entry : hashMap.entrySet()) {
            //если найдено значение равное параметру
            if (entry.getValue().equals(parameter)) {
                //находим ключ
                s = entry.getKey().toString();
                // и удаляем
                hashMap.remove(entry.getKey());
                return s;
            }
        }
        return "Nothing";
    }

    private String getGroupData(long groupId) {

        for (Map.Entry entry : groupData.entrySet()) {
            if (entry.getKey().equals(groupId)) {
                return entry.getValue().toString();
            }
        }
        return "Empty";
    }

    private void setGroupData(long groupId, String data) {
        groupData.put(groupId, data);
    }
}

