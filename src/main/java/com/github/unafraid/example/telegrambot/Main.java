package com.github.unafraid.example.telegrambot;

import com.github.unafraid.example.telegrambot.handlers.ExampleInlineMenuHandler;
import com.github.unafraid.example.telegrambot.handlers.HelpHandler;
import com.github.unafraid.example.telegrambot.handlers.StartCommandHandler;
import com.github.unafraid.example.telegrambot.handlers.WhoAmIHandler;
import com.github.unafraid.example.telegrambot.validators.AdminIdValidator;
import com.github.unafraid.telegrambot.bots.DefaultTelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.util.ArrayList;
import java.util.List;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author UnAfraid
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        final String token = System.getenv("EXAMPLE_TG_BOT_TOKEN");
        final String username = System.getenv("EXAMPLE_TG_BOT_USERNAME");
        final String adminIds = System.getenv("EXAMPLE_TG_BOT_ADMIN_IDS");
        if (token == null || token.isBlank()) {
            LOGGER.warn("EXAMPLE_TG_BOT_TOKEN is not defined!");
            return;
        } else if (username == null || username.isBlank()) {
            LOGGER.warn("EXAMPLE_TG_BOT_USERNAME is not defined!");
            return;
        } else if (adminIds == null || adminIds.isBlank()) {
            LOGGER.warn("EXAMPLE_TG_BOT_ADMIN_IDS is not defined!");
            return;
        }

        LOGGER.info("Initializing {} ...", username);

        final List<Integer> adminIdsList = parseAdminIds(adminIds);
        if (adminIdsList.isEmpty()) {
            LOGGER.warn("Couldn't find admin ids");
            return;
        }
        LOGGER.info("Authorized admin ids: {}", adminIdsList);

        // Initialize API Context
        ApiContextInitializer.init();

        // Create new instance of TelegramBotsAPI
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        // Register the default bot with token and username
        final DefaultTelegramBot telegramBot = new DefaultTelegramBot(token, username);
        telegramBotsApi.registerBot(new CloneMainCampbot());

        // Register access level validator
        telegramBot.setAccessLevelValidator(new AdminIdValidator(adminIdsList));

        // Register handlers
        telegramBot.addHandler(new ExampleInlineMenuHandler());
        telegramBot.addHandler(new HelpHandler());
        telegramBot.addHandler(new StartCommandHandler());
        telegramBot.addHandler(new WhoAmIHandler());
        LOGGER.info("Initialization done");
    }

    private static List<Integer> parseAdminIds(String adminIds) {
        final List<Integer> whitelistUserIds = new ArrayList<>();
        for (String adminIdValue : adminIds.split(",")) {
            try {
                final int adminId = Integer.parseInt(adminIdValue);
                if (adminId < 0) {
                    LOGGER.warn("User ID expected, negative ids are reserved for groups!");
                    continue;
                }
                whitelistUserIds.add(adminId);
            } catch (Exception e) {
                LOGGER.warn("Failed to parse admin id {}", adminIdValue, e);
            }
        }
        return whitelistUserIds;
    }
}

//C:\Users\User\clonemaincamp\pom.xml

class CloneMainCampbot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()&&update.getMessage().isUserMessage()){
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            SendMessage msg = new SendMessage();
            msg.setParseMode(ParseMode.HTML);

            if(message_text.equals("/start")){
                msg.setChatId(chat_id);
                msg.setText("Salom <b>"+update.getMessage().getFrom().getFirstName()+"</b>");
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> board = new ArrayList<List<InlineKeyboardButton>>();
                List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
                row1.add(new InlineKeyboardButton("Natija").setCallbackData("clicked_natija"));
                row1.add(new InlineKeyboardButton("Mooncat").setCallbackData("mooncat"));
                row.add(new InlineKeyboardButton("Admin").setUrl("http://t.me/EngineerOfJava"));
                board.add(row);
                board.add(row1);
                markup.setKeyboard(board);
                msg.setReplyMarkup(markup);
            }

            try{
                if(!msg.getChatId().equals(null))
                execute(msg);
            }catch(TelegramApiException e){
                e.printStackTrace();
            }
        }
        if(update.hasCallbackQuery()){
            String mat1 = "\uD83C\uDF16\uD83C\uDF17\uD83C\uDF18\uD83C\uDF11\uD83C\uDF12\uD83C\uDF13\uD83C\uDF14\uD83C\uDF15";
            String mat2 = "\uD83C\uDF15\uD83C\uDF16\uD83C\uDF17\uD83C\uDF18\uD83C\uDF11\uD83C\uDF12\uD83C\uDF13\uD83C\uDF14";
            String mat3 = "\uD83C\uDF14\uD83C\uDF15\uD83C\uDF16\uD83C\uDF17\uD83C\uDF18\uD83C\uDF11\uD83C\uDF12\uD83C\uDF13";
            String mat4 = "\uD83C\uDF13\uD83C\uDF14\uD83C\uDF15\uD83C\uDF16\uD83C\uDF17\uD83C\uDF18\uD83C\uDF11\uD83C\uDF12";
            String mat5 = "\uD83C\uDF12\uD83C\uDF13\uD83C\uDF14\uD83C\uDF15\uD83C\uDF16\uD83C\uDF17\uD83C\uDF18\uD83C\uDF11";
            String mat6 = "\uD83C\uDF11\uD83C\uDF12\uD83C\uDF13\uD83C\uDF14\uD83C\uDF15\uD83C\uDF16\uD83C\uDF17\uD83C\uDF18";
            String mat7 = "\uD83C\uDF18\uD83C\uDF11\uD83C\uDF12\uD83C\uDF13\uD83C\uDF14\uD83C\uDF15\uD83C\uDF16\uD83C\uDF17";
            String mat8 = "\uD83C\uDF17\uD83C\uDF18\uD83C\uDF11\uD83C\uDF12\uD83C\uDF13\uD83C\uDF14\uD83C\uDF15\uD83C\uDF16";
            final String matrix[] = { mat1, mat2, mat3, mat4, mat5, mat6, mat7, mat8  };
            String call_data = update.getCallbackQuery().getData();
            final long message_id = update.getCallbackQuery().getMessage().getMessageId();
            final long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if(call_data.equals("mooncat")){

                Thread th = new Thread(new Runnable() {
                    int matrix_counter = 0;
                    public void run() {
                        for(int i = 0; i < 3*matrix.length; i++){
                        EditMessageText edit = new EditMessageText();
                        edit.setChatId(chat_id);
                        edit.setMessageId((int) message_id);
                        edit.setText(matrix[matrix_counter]);
                        matrix_counter++;
                        if(matrix_counter==7){ matrix_counter=0; }
                        try {
                            Thread.sleep(50);
                            execute(edit);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch(TelegramApiException e){
                            e.printStackTrace();
                        }
                        }
                    }
                });
                    th.start();

            }

            if(call_data.equals("clicked_natija")){
                String answer = "Nima gaplar " + update.getCallbackQuery().getFrom().getFirstName()
                         + "";
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText(answer);

                try{
                    execute(edit);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public String getBotUsername() {
        return "clonemaincampbot";
    }

    public String getBotToken() {
        return "901883086:AAFeYGIPybkAp5uupuD3jje2EbdXQs5oZuI";
    }
}
