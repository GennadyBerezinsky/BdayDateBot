package bot;
// Created by User on 16.06.2018
// Project: BDayDateBotTg
// Target: bot


import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot implements ConstStrings {
    //Interface ConstStrings contains some contants such as bot token, passwords from database, etg..



    public void onUpdateReceived(Update update) {

        Logger.getGlobal().info("LOG --> " + update.getMessage().getFrom().getFirstName() + " "
                + update.getMessage().getFrom().getId() + " " + +update.getMessage().getChatId() + " : " + update.getMessage().getText());

        String chat = update.getMessage().getChatId().toString();
        boolean flag = true;
        DateChecker dateChecker = new DateChecker();
        String full_message = update.getMessage().getText().toString();
        String message ="none";
        String dateText = "none";
        int index = -1;
        index = full_message.indexOf(' ');
        if(index>0  && full_message.length() == "/add_date 18-18-2018".length()){
            message = full_message.substring(0, index);
            dateText = full_message.substring(index+1, full_message.length());
        }
        else {
            message = full_message;
            flag = false;
        }

        switch (message) {
            case "/start":
                sendMsg(chat, "Hello!");
                break;
            case "/add_date":
                if(flag){
                    sendMsg(chat, "okey, your date is " + dateText);
                    try {
                        dateChecker.newDate(update.getMessage().getFrom().getId().toString(),
                                update.getMessage().getFrom().getFirstName(),
                                dateText, chat);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    sendMsg(chat, "Exception: date input error! Please input date in format dd-MM-yyyy! ");
                }
                break;
            case "/me":
                sendMsg(update.getMessage().getChatId().toString(), update.getMessage().getChatId().toString() +
                        " " + update.getMessage().getFrom().toString());
                break;
            default:
                sendMsg(update.getMessage().getChatId().toString(), "WAT?");
                break;
        }
    }

    public String getBotUsername() {
        return bot_name;
    }


    public String getBotToken() {
        return token;
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println(e.toString());
        }

    }
}
