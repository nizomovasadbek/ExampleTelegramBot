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

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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
        telegramBotsApi.registerBot(telegramBot);

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

class Ussdbot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        if(update.hasMessage()&&update.getMessage().hasText()){
            long chat_id = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            User user = update.getMessage().getFrom();
            SendMessage msg = new SendMessage();
            msg.setParseMode(ParseMode.HTML);

            if(text.equals("/start")){
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> board = new ArrayList<List<InlineKeyboardButton>>();
                List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
                row.add(new InlineKeyboardButton("Uzmobile").setCallbackData("uzmobile"));
                row.add(new InlineKeyboardButton("Beeline").setCallbackData("beeline"));
                row1.add(new InlineKeyboardButton("MobiUz").setCallbackData("mobiuz"));
                row1.add(new InlineKeyboardButton("UCell").setCallbackData("ucell"));

                board.add(row);
                board.add(row1);

                markup.setKeyboard(board);

                msg.setChatId(chat_id);
                msg.setText("Assalomu alaykum\uD83D\uDC4B <b>" + user.getFirstName() + "</b>");
                msg.setReplyMarkup(markup);
            }

            try{
                if(!msg.getText().equals(null)&&!msg.getChatId().equals(null)){
                    execute(msg);
                }
            }catch(TelegramApiException e){
                e.printStackTrace();
            }
        }

        if(update.hasCallbackQuery()){
            String callback_data = update.getCallbackQuery().getData();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();

            if(callback_data.equals("uzmobile")){
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Siz Uzmobile ni tanladingiz");

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(callback_data.equals("mobiuz")){
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Siz MobiUz ni tanladingiz");

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(callback_data.equals("beeline")){
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Siz Beeline ni tanladingiz");

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(callback_data.equals("ucell")){
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Siz Ucell ni tanladingiz");

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public String getBotUsername() {
        return "ussd_robot";
    }

    public String getBotToken() {
        return "1343013669:AAEpfzmRYjuP8RAzvZ8IfNtC96W4pxSDOew";
    }
}
