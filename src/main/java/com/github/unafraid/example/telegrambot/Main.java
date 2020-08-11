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
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinChatMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatTitle;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.User;
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

//C:\Users\User\clonemaincamp\pom.xml
//http://cbu.uz/uzc/arkhiv-kursov-valyut/xml/ kurs

public class CloneMainCampbot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        List<List<InlineKeyboardButton>> main_board = new ArrayList<List<InlineKeyboardButton>>();
        List<List<InlineKeyboardButton>> min_board = new ArrayList<List<InlineKeyboardButton>>();

        if(update.hasMessage() && update.getMessage().hasText()){
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            SendMessage msg = new SendMessage();
            msg.setParseMode(ParseMode.HTML);
            Chat chat = update.getMessage().getChat();

            if(update.getMessage().isSuperGroupMessage()||update.getMessage().isGroupMessage()){
                if(message_text.equals("/gid")){
                    msg.setChatId(chat_id);
                    msg.setText("Guruh id: <code>"+chat.getId() + "</code>\n" +
                            "Guruh niki: <code>"+chat.getTitle()+"</code>\n" +
                            "Guruh linki: @"+chat.getUserName());

                }

                if(message_text.equals("/permissions")){
                    SetChatPermissions per = new SetChatPermissions();
                    per.setChatId(chat_id);
                    ChatPermissions c = new ChatPermissions();
                    per.setPermissions(c);

                    msg.setChatId(chat_id);
                    msg.setText("Permissionlar o'rnatildi.");
                    msg.setReplyToMessageId(update.getMessage().getMessageId());

                    try {
                        execute(per);
                    }catch (TelegramApiException e){
                        e.printStackTrace();
                    }
                }

                if(message_text.equals("/leave_chat")&&update.getMessage().
                        getFrom().getId().equals(649244901)){
                    LeaveChat leave = new LeaveChat();
                    leave.setChatId(chat_id);

                    try {
                        execute(leave);
                    }catch (TelegramApiException e){
                        e.printStackTrace();
                    }
                }

                if(message_text.startsWith("/brt=")){
                    String mv = message_text.substring(5);
                    SetChatTitle title = new SetChatTitle(chat_id, mv);

                    try {
                        execute(title);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                if(message_text.equals("/pin")&&update.getMessage().isReply()){
                    PinChatMessage pin = new PinChatMessage();
                    pin.setChatId(chat_id);
                    pin.setMessageId(update.getMessage().getReplyToMessage().getMessageId());
                    try {
                        execute(pin);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(message_text.equals("/unpin")){
                    UnpinChatMessage unpin = new UnpinChatMessage(chat_id);

                    try {
                        execute(unpin);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(message_text.trim().equals("/me")){
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> list = new ArrayList<List<InlineKeyboardButton>>();
                List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
                row.add(new InlineKeyboardButton("Mana").setUrl("http://t.me/"+update.getMessage()
                .getFrom().getUserName()));
                list.add(row);
                markup.setKeyboard(list);

                msg.setChatId(chat_id);
                msg.setReplyMarkup(markup);
                msg.setText("Link tayyor");

            }

            if(message_text.trim().equals("/start")||message_text.trim().equals("/start@clonemaincampbot")){
                InlineKeyboardMarkup main_markup = new InlineKeyboardMarkup();

                msg.setChatId(chat_id);
                msg.setText("Salom <b>"+update.getMessage().getFrom().getFirstName()+"</b>");
                List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> row2 = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> row3 = new ArrayList<InlineKeyboardButton>();
                row3.add(new InlineKeyboardButton("Ob-havo ⛰").setCallbackData("obhavo"));
                row2.add(new InlineKeyboardButton("Get Info \uD83D\uDCBD").setCallbackData("info"));
                row1.add(new InlineKeyboardButton("Natija \uD83D\uDCCC").setCallbackData("clicked_natija"));
                row1.add(new InlineKeyboardButton("Mooncat \uD83C\uDF15").setCallbackData("mooncat"));
                row.add(new InlineKeyboardButton("Admin \uD83D\uDC68\u200D\uD83D\uDCBB").setUrl("http://t.me/EngineerOfJava"));
                row.add(new InlineKeyboardButton("Modernator").setUrl("http://t.me/belaya_romawka_17o7"));
                main_board.add(row);
                main_board.add(row1);
                main_board.add(row2);
                main_board.add(row3);
                main_markup.setKeyboard(main_board);
                msg.setReplyMarkup(main_markup);
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
            User callback_user = update.getCallbackQuery().getFrom();

            if(call_data.equals("obhavo")){
                InlineKeyboardMarkup region_markup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
                row1.add(new InlineKeyboardButton("Toshkent").setCallbackData("tashkent"));
                row1.add(new InlineKeyboardButton("Andijon").setCallbackData("andijan"));
                List<InlineKeyboardButton> row2 = new ArrayList<InlineKeyboardButton>();
                row2.add(new InlineKeyboardButton("Buxoro").setCallbackData("bukhara"));
                row2.add(new InlineKeyboardButton("Jizzax").setCallbackData("jizzakh"));
                List<InlineKeyboardButton> row3 = new ArrayList<InlineKeyboardButton>();
                row3.add(new InlineKeyboardButton("Guliston").setCallbackData("gulistan"));
                row3.add(new InlineKeyboardButton("Zarafshon").setCallbackData("zarafshan"));
                List<InlineKeyboardButton> row4 = new ArrayList<InlineKeyboardButton>();
                row4.add(new InlineKeyboardButton("Qarshi").setCallbackData("karshi"));
                row4.add(new InlineKeyboardButton("Navoiy").setCallbackData("navoi"));
                List<InlineKeyboardButton> row5 = new ArrayList<InlineKeyboardButton>();
                row5.add(new InlineKeyboardButton("Namangan").setCallbackData("namangan"));
                row5.add(new InlineKeyboardButton("Nukus").setCallbackData("nukus"));
                List<InlineKeyboardButton> row6 = new ArrayList<InlineKeyboardButton>();
                row6.add(new InlineKeyboardButton("Samarqand").setCallbackData("samarkand"));
                row6.add(new InlineKeyboardButton("Urganch").setCallbackData("urgench"));
                List<InlineKeyboardButton> row7 = new ArrayList<InlineKeyboardButton>();
                row7.add(new InlineKeyboardButton("Farg'ona").setCallbackData("ferghana"));
                row7.add(new InlineKeyboardButton("Xiva").setCallbackData("khiva"));
                List<InlineKeyboardButton> row8 = new ArrayList<InlineKeyboardButton>();
                row8.add(new InlineKeyboardButton("Rasmiy sayt").setUrl("https://obhavo.uz"));

                min_board.add(row1);
                min_board.add(row2);
                min_board.add(row3);
                min_board.add(row4);
                min_board.add(row5);
                min_board.add(row6);
                min_board.add(row7);
                min_board.add(row8);

                region_markup.setKeyboard(min_board);

                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Regionlar");
                edit.setReplyMarkup(region_markup);

                try{
                    execute(edit);
                }catch(TelegramApiException e){
                    e.printStackTrace();
                }
            }

            // Ob-havo call_data:start

            if(call_data.equals("jizzakh")){

                obhavoback o = new obhavoback("Jizzax");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Jizzaxdagi ob-havo: " + o.getHarorat());

                try{
                    execute(edit);
                }catch(TelegramApiException e){
                    e.printStackTrace();
                }
            }

            if(call_data.equals("tashkent")){

                obhavoback o = new obhavoback("Toshkent");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Toshkentdagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("andijan")){

                obhavoback o = new obhavoback("Andijon");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Andijondagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("bukhara")){

                obhavoback o = new obhavoback("Buxoro");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Buxorodagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("gulistan")){

                obhavoback o = new obhavoback("Guliston");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Gulistondagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("zarafshan")){


                obhavoback o = new obhavoback("Zarafshon");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Zarafshondagi ob-havo: " + o.getHarorat());

                try{
                    execute(edit);
                }catch(TelegramApiException e){
                    e.printStackTrace();
                }
            }

            if(call_data.equals("karshi")){


                obhavoback o = new obhavoback("Qarshi");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Qarshidagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("navoi")){

                obhavoback o = new obhavoback("Navoiy");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Navoiydagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("namangan")){


                obhavoback o = new obhavoback("Namangan");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Namangandagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("nukus")){


                obhavoback o = new obhavoback("Nukus");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Nukusdagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("samarkand")){


                obhavoback o = new obhavoback("Samarqand");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Samarqanddagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("urgench")){


                obhavoback o = new obhavoback("Urganch");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Urganchdagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("ferghana")){


                obhavoback o = new obhavoback("Farg'ona");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Farg'onadagi ob-havo: " + o.getHarorat());
                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(call_data.equals("khiva")){


                obhavoback o = new obhavoback("Xiva");
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                edit.setText("Xivadagi ob-havo: " + o.getHarorat());

                try {
                    execute(edit);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            //Ob-havo call_data:end

            if(call_data.equals("info")){
                EditMessageText edit = new EditMessageText();
                edit.setChatId(chat_id);
                edit.setMessageId((int) message_id);
                AnswerCallbackQuery answer = new AnswerCallbackQuery();
                answer.setCallbackQueryId(update.getCallbackQuery().getId());
                answer.setShowAlert(true);
                answer.setText("ID: " + callback_user.getId() + "\nFirst Name: " + callback_user
                .getFirstName() + "\nLast Name: " + callback_user.getLastName() + "\n" +
                         "Username: " +callback_user.getUserName() + "\n"
                + "is Bot: " +callback_user.getBot());
                edit.setText(answer.getText());
                try{
                    execute(answer);
                    execute(edit);
                }catch(TelegramApiException e){
                    e.printStackTrace();
                }
            }

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

                AnswerCallbackQuery ans = new AnswerCallbackQuery();
                ans.setCallbackQueryId(update.getCallbackQuery().getId());
                ans.setShowAlert(true);
                ans.setText("Nima gaplar " + update.getCallbackQuery().getFrom().getFirstName());



                try{
                    execute(edit);
                    execute(ans);
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
