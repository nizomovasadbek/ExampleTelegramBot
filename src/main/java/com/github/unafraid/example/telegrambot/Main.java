package com.github.unafraid.example.telegrambot;

import com.github.unafraid.example.telegrambot.handlers.ExampleInlineMenuHandler;
import com.github.unafraid.example.telegrambot.handlers.HelpHandler;
import com.github.unafraid.example.telegrambot.handlers.StartCommandHandler;
import com.github.unafraid.example.telegrambot.handlers.WhoAmIHandler;
import com.github.unafraid.example.telegrambot.validators.AdminIdValidator;
import com.github.unafraid.telegrambot.bots.DefaultTelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;
import org.slf4j.*;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinChatMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.objects.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import java.time.*;
import javax.script.*;

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
//http://cbu.uz/uzc/arkhiv-kursov-valyut/xml/ kurs

    class CloneMainCampbot extends TelegramLongPollingBot {

        public static String getStatus()throws Exception{
            String get_text = "";
            String n[] = {"Kasallanganlar\uD83E\uDD12    ", "Tuzalganlar\uD83E\uDD24     ",
                    "Vafot etganlar⚰️    "};
            int i = 0;
            URL url = new URL("https://www.gazeta.uz/oz/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            String html = "";
            String line = "";
            BufferedReader buffer = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((line = buffer.readLine())!=null){
                html += line + "\n";
            }
            String reg = "<div class=\"block-row-item row-value\"><span>";
            Pattern ptr = Pattern.compile(reg);
            Matcher mt = ptr.matcher(html);
            while(mt.find()){
                String xml = html.substring(mt.start()+44, mt.end()+10);
                String as = xml.substring(0, xml.indexOf('<'));
                get_text += n[i] + as + "\n";
                i++;
            }
            buffer.close();
            con.disconnect();

            return get_text;
        }

        private List<Integer> foydalanuvchilar = new ArrayList<>();

        private boolean in_array(Integer id, List<Integer> f) {
            for (int i = 0; i < f.size(); i++) {
                if (id.equals(f.get(i))) {
                    return true;
                }
            } return false;
        }

        public void onUpdateReceived(Update update) {

            List<List<InlineKeyboardButton>> main_board = new ArrayList<List<InlineKeyboardButton>>();
            List<List<InlineKeyboardButton>> min_board = new ArrayList<List<InlineKeyboardButton>>();
            String[] valyutalar = new String[8];

            if(update.hasMessage() && update.getMessage().hasText()){
                String message_text = update.getMessage().getText();
                long chat_id = update.getMessage().getChatId();
                SendMessage msg = new SendMessage();
                msg.setParseMode(ParseMode.HTML);
                Chat chat = update.getMessage().getChat();
                RestrictChatMember mute = new RestrictChatMember();
                ZonedDateTime zd = ZonedDateTime.now();
                boolean is_creator = update.getMessage().getFrom().getId().equals(649244901);
                boolean is_admin = false;
                Integer user_id = update.getMessage().getFrom().getId();

                if(update.getMessage().isSuperGroupMessage()||update.getMessage().isGroupMessage()){

                    GetChatAdministrators administrators = new GetChatAdministrators();
                    administrators.setChatId(chat_id);
                    List<ChatMember> admins = null;
                    try {
                        admins = execute(administrators);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    for(ChatMember c:admins){
                        if(c.getUser().getId().equals(user_id)){
                            is_admin = true;
                            break;
                        }
                        is_admin = false;
                    }

                    if(message_text.equals("/members_count")&&(is_admin||is_creator)){
                        try {
                            GetChatMembersCount count = new GetChatMembersCount();
                            count.setChatId(chat_id);
                            msg.setChatId(chat_id);
                            msg.setText("A'zolar soni: " + execute(count));
                        }catch(TelegramApiException e){
                            e.printStackTrace();
                        }
                    }

                    if(message_text.equals("/gid")&&(is_admin||is_creator)){
                        msg.setChatId(chat_id);
                        msg.setText("Guruh id: <code>"+chat.getId() + "</code>\n" +
                                "Guruh niki: <code>"+chat.getTitle()+"</code>\n" +
                                "Guruh linki: @"+chat.getUserName());

                    }

                    if(message_text.equals("/kick")&&update.getMessage().isReply()
                    &&(is_admin||is_creator)){
                        KickChatMember ki = new KickChatMember(chat_id, update.getMessage()
                                .getReplyToMessage().getFrom().getId());

                        try {
                            execute(ki);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    if(message_text.equals("dir")){
                        try{
                        java.io.File f = new java.io.File("mkfull.txt");
                        f.createNewFile();
                        SendMessage mmm = new SendMessage();
                            mmm.setText("Lezzy");
                            mmm.setChatId(chat_id);
                            execute(mmm);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                    if(message_text.equals("/leave_chat")&&
                    update.getMessage().getFrom().getId().equals(649244901)){
                        LeaveChat leave = new LeaveChat();
                        leave.setChatId(chat_id);

                        try {
                            execute(leave);
                        }catch (TelegramApiException e){
                            e.printStackTrace();
                        }
                    }

                    if(message_text.startsWith("/brt=")&&(is_admin||is_creator)){
                        String mv = message_text.substring(5);
                        SetChatTitle title = new SetChatTitle(chat_id, mv);

                        try {
                            execute(title);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    if(message_text.equals("/pin")&&update.getMessage().isReply()
                    &&(is_admin||is_creator)){
                        PinChatMessage pin = new PinChatMessage();
                        pin.setChatId(chat_id);
                        pin.setMessageId(update.getMessage().getReplyToMessage().getMessageId());

                        DeleteMessage del = new DeleteMessage(chat_id, update.getMessage().getMessageId());

                        try {
                            execute(pin);
                            execute(del);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    if(message_text.equals("/unpin")&&(is_admin||is_creator)){
                        UnpinChatMessage unpin = new UnpinChatMessage(chat_id);

                        DeleteMessage del = new DeleteMessage(chat_id, update.getMessage().getMessageId());

                        try {
                            execute(unpin);
                            execute(del);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    if(update.getMessage().hasEntities()&&!is_admin){
                        List<MessageEntity> en = new ArrayList<>();
                        en = update.getMessage().getEntities();
                        msg.setChatId(chat_id);
                        String type = en.get(0).getType();
                        DeleteMessage del = new DeleteMessage();
                        if(type.equals("url")||type.equals("text_link")){
                            del.setChatId(chat_id);
                            del.setMessageId(update.getMessage().getMessageId());

                            try {
                                execute(del);
                            }catch(TelegramApiException e){
                                e.printStackTrace();
                            }
                        }
                    }

                    if(message_text.equals("/delete")&&update.getMessage().isReply()&&(is_admin||is_creator)){
                        DeleteMessage del = new DeleteMessage(chat_id, update.getMessage()
                                .getReplyToMessage().getMessageId());
                        DeleteMessage del1 = new DeleteMessage(chat_id, update.getMessage().getMessageId());

                        try {
                            execute(del);
                            execute(del1);
                        }catch(TelegramApiException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(message_text.startsWith("calc")){
                    SendMessage msgt = new SendMessage();
                    msgt.setParseMode(ParseMode.HTML);
                    msgt.setChatId(chat_id);
                    msgt.setText("Hisoblanmoqda...");
                    String would_be_calc = message_text.substring(5);
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("nashorn");
                    try{
                        int aimp = (int) engine.eval(would_be_calc);
                        String kemp = String.format("<code>%d</code>", aimp);
                        msgt.setText(kemp);
                        execute(msgt);
                    }catch(Exception e){
                       e.printStackTrace();
                        msgt.setText("<b>Xatolik!</b>");
                    }
                }
                
                if(message_text.equals("/subscriber")&&update.getMessage().getFrom().getId().equals(
                    649244901)){
                SendMessage h = new SendMessage();
                h.setChatId((long) 649244901);
                h.setParseMode(ParseMode.MARKDOWN);
                String no_list = "Foydalanuvchilar soni: *"+foydalanuvchilar.size()+"* ta\n";
                for(int i = 0; i < foydalanuvchilar.size(); i++){
                    no_list += "["+(i+1)+"-foydalanuvchi](tg://user?id="
                            +foydalanuvchilar.get(i)+") 🆔 "+foydalanuvchilar.get(i)+"\n";
                }
                h.setText(no_list);

                try {
                    execute(h);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(message_text.startsWith("/send_all=")&&update.getMessage().getFrom()
                    .getId().equals(
                            649244901
                    )){
                String sub = message_text.substring(10);
                for(int i = 0; i < foydalanuvchilar.size(); i++){
                    SendMessage h = new SendMessage();
                    h.setText(sub);
                    h.setChatId((long)foydalanuvchilar.get(i));

                    try {
                        execute(h);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }

                if(message_text.trim().equals("/me")&&update.getMessage().isUserMessage()){
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

                if(message_text.startsWith("/id=")&&update.getMessage().isUserMessage()){
                    String text = message_text.substring(4);
                    SendMessage id_finder = new SendMessage().setParseMode(ParseMode.MARKDOWN);
                    id_finder.setChatId(chat_id);
                    Integer id = Integer.parseInt(text);
                    id_finder.setText("Siz qidirgan odam\uD83D\uDD0E: ["+text+"](tg://user?id="+id+")");

                    try {
                        execute(id_finder);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                
                if(update.getMessage().isUserMessage()){
                    if(!in_array(update.getMessage().getFrom().getId(), foydalanuvchilar)){
                        User foydalanuvchining_uzi = update.getMessage().getFrom();
                        Integer foydalanuvchi_id = foydalanuvchining_uzi.getId();

                        LocalDateTime l = LocalDateTime.now();
                        l = l.plusHours(5);
                        String time = String.format("%02d:%02d:%02d",
                                l.getHour(), l.getMinute(), l.getSecond());
                        foydalanuvchilar.add(foydalanuvchi_id);
                        SendMessage adminga_log = new SendMessage();
                        adminga_log.setParseMode(ParseMode.HTML);
                        adminga_log.setChatId((long) 649244901);
                        adminga_log.setText("Yangi a`zo qo'shildi.\n" +
                                foydalanuvchining_uzi.getFirstName() + "\n" +
                                foydalanuvchining_uzi.getId() + "\n" +
                                foydalanuvchining_uzi.getUserName() + "\n" +
                                "<b>Qo'shilgan vaqti: " + time + "</b>");

                        try {
                            execute(adminga_log);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(message_text.trim().equals("/start")&&update.getMessage().isUserMessage()){
                    InlineKeyboardMarkup main_markup = new InlineKeyboardMarkup();

                    msg.setChatId(chat_id);
                    msg.setText("Salom <b>"+update.getMessage().getFrom().getFirstName()+"</b>");
                    List<InlineKeyboardButton> row = new ArrayList<InlineKeyboardButton>();
                    List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
                    List<InlineKeyboardButton> row2 = new ArrayList<InlineKeyboardButton>();
                    List<InlineKeyboardButton> row3 = new ArrayList<InlineKeyboardButton>();
                    row3.add(new InlineKeyboardButton("Ob-havo ⛰").setCallbackData("obhavo"));
                    row3.add(new InlineKeyboardButton("Valyuta\uD83D\uDCB5").setCallbackData("valyuta"));
                    row2.add(new InlineKeyboardButton("Ma`lumotlar \uD83D\uDCBD").setCallbackData("info"));
                    row2.add(new InlineKeyboardButton("Kommandalar \uD83D\uDCF2").setCallbackData("commands"));
                    row1.add(new InlineKeyboardButton("Koronavirus \uD83E\uDDA0").setCallbackData("clicked_natija"));
                    row1.add(new InlineKeyboardButton("Mooncat \uD83C\uDF15").setCallbackData("mooncat"));
                    row.add(new InlineKeyboardButton("Admin \uD83D\uDC68\u200D\uD83D\uDCBB").setUrl("http://t.me/EngineerOfJava"));
                    row.add(new InlineKeyboardButton("Guruhga qo'shish➕").setUrl("https://t.me/clonemaincampbot?startgroup=test"));

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

                    min_board.add(row1);
                    min_board.add(row2);
                    min_board.add(row3);
                    min_board.add(row4);
                    min_board.add(row5);
                    min_board.add(row6);
                    min_board.add(row7);

                    region_markup.setKeyboard(min_board);

                    EditMessageText edit = new EditMessageText();
                    edit.setChatId(chat_id);
                    edit.setMessageId((int) message_id);
                    edit.setText("Regionlarni tanlang:");
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
                    EditMessageText edit = new EditMessageText();
                    edit.setChatId(chat_id);
                    edit.setMessageId((int) message_id);
                    try {
                        edit.setText(getStatus());
                        execute(edit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(call_data.equals("valyuta")){
                    try{
                        URL val_url = new URL("http://cbu.uz/uzc/arkhiv-kursov-valyut/xml/");
                        HttpURLConnection connection = (HttpURLConnection) val_url.openConnection();
                        connection.setRequestMethod("GET");
                        String xml = "";
                        BufferedReader reader = new BufferedReader(new
                                InputStreamReader(connection.getInputStream()));
                        String line = "";
                        while((line = reader.readLine())!=null){
                            xml += line + "\n";
                        }

                        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        org.w3c.dom.Document d = db.parse(new InputSource(new StringReader(xml)));

                        NodeList nodejs = d.getDocumentElement().getElementsByTagName("CcyNm_UZ");
                        NodeList nodeval = d.getDocumentElement().getElementsByTagName("Rate");
                        NodeList data = d.getDocumentElement().getElementsByTagName("date");
                        Node date = data.item(0);
                        int[] kerakli_valyutalar = {0, 14, 20, 31, 36, 55, 65, 67};
                        int i = 0;
                        for(int x:kerakli_valyutalar){
                            Node n = nodejs.item(x);
                            Node n_org = nodeval.item(x);
                            valyutalar[i] = n.getTextContent() + " - " + n_org.getTextContent() + " so'm";
                            i++;
                        }

                        String all_text = "";

                        for(int fi = 0; fi < kerakli_valyutalar.length; fi++){
                            all_text += valyutalar[fi] + "\n";
                        }

                        EditMessageText edit = new EditMessageText();
                        edit.setParseMode(ParseMode.HTML);
                        edit.setMessageId((int) message_id);
                        edit.setChatId(chat_id);
                        edit.setText(all_text + "\n<b>" + date.getTextContent() + " ma`lumotiga ko'ra</b>");

                        execute(edit);

                        connection.disconnect();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                
                if(call_data.equals("commands")){
                    EditMessageText edit = new EditMessageText();
                    edit.setChatId(chat_id);
                    edit.setMessageId((int) message_id);
                    edit.setParseMode(ParseMode.HTML);
                    edit.setText("Guruhda ishlaydigan kommandalar:\n" +
                            "<b>Diqqat❗️ quyidagi funksiyalar faqat bot admin bo'lganda ishlaydi.</b>\n" +
                            "/brt=[satr]   -  Guruh nomini siz bergan satr ga almashtiradi\n" +
                            "/kick   -   Reply qilingan odam ni guruhdan chiqarib tashlaydi.\n" +
                            "/gid   -   Guruh haqida ma`lumot.\n" +
                            "/pin   -   Reply qilingan xabarni pin qiladi.\n" +
                            "/unpin   -   Pin qilingan xabarni olib tashlaydi\n" +
                            "/delete   -   Reply qilingan xabarni o'chirib tashlaydi\n" +
                            "/members_count - Guruhdagi a`zolar sonini hisoblaydi.\n" +
                            "<b>Avtomatik ishlovchi funksiyalar: </b>\n" +
                            "Matnli link va linklarni o'chirib tashlaydi.\nLichkada ishlaydigan kommandalar:\n/id=[id] - telegramdagi shu id egasini topib beradi.");

                    try {
                        execute(edit);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(update.getMessage().getNewChatMembers().size()!=0){
            SendMessage join_ = new SendMessage();
            join_.setChatId(update.getMessage().getChatId());
            join_.setParseMode(ParseMode.MARKDOWN);
            String joiner = "";
                if(update.getMessage().getNewChatMembers().size()>1)
            for(User u:update.getMessage().getNewChatMembers()){
                joiner += u.getFirstName() + ", ";
            } else {
                joiner += update.getMessage().getNewChatMembers().get(0).getFirstName();
            }


            join_.setText("Assalomu alaykum*\uD83D\uDC4B" + joiner + "*");

            try {
                execute(join_);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if(!update.getMessage().getLeftChatMember().getFirstName().equals(null)){
            SendMessage left_ = new SendMessage(update.getMessage().getChatId(),
                    "Xayr *" + update.getMessage().getLeftChatMember().
                            getFirstName() + "*").setParseMode(ParseMode.MARKDOWN);

            try {
                execute(left_);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        }

        public String getBotUsername() {
            return "clonemaincampbot";
        }

        public String getBotToken() {
            return "901883086:AAEu8f9l1efmLVnSy4IUg_wk46J41Zt21vY";
        }
    }


//http://cbu.uz/uzc/arkhiv-kursov-valyut/xml/ kurs



class obhavoback {
    private String html = "";
    private String sana = "";
    private String harorat = "";
    private String shahar = "";
    private HashMap<String, String> map = new HashMap<String, String>();

    public void init(){
        map.put("Jizzax", "jizzakh");
        map.put("Toshkent", "tashkent");
        map.put("Andijon", "andijan");
        map.put("Buxoro", "bukhara");
        map.put("Guliston", "gulistan");
        map.put("Zarafshon", "zarafshan");
        map.put("Qarshi", "karshi");
        map.put("Navoiy", "navoi");
        map.put("Namangan", "namangan");
        map.put("Nukus", "nukus");
        map.put("Samarqand", "samarkand");
        map.put("Urganch", "urgench");
        map.put("Farg'ona", "ferghana");
        map.put("Xiva", "khiva");
    }

    public String getShahar(){
        return shahar;
    }

    public obhavoback(String city){
        shahar = city;
        init();
        try{
            URL url = new URL("https://obhavo.uz/"+map.get(city));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader buffer =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            while((line = buffer.readLine()) != null){
                html += line + "\n";
            }
            String current_day_class = "\"current-day\"";
            //Bugungi kun klasi
            Pattern pat = Pattern.compile("class=" + current_day_class);
            Matcher most = pat.matcher(html);

            while(most.find()){
                String xml = html.substring(most.start()-10, most.end()+30).trim();
                xml = xml.substring(xml.indexOf(">")+1, xml.indexOf("</"));
                sana = xml;
            }

            String current_forecast = "\"current-forecast\"";

            Pattern ptr = Pattern.compile("class="+current_forecast);
            Matcher most1 = ptr.matcher(html);

            while(most1.find()){
                String xml = html.substring(most1.start()-10, most1.end()+225).trim();
                xml = xml.substring(xml.indexOf("<strong>")+8, xml.indexOf("</strong>"));
                harorat = xml;
            }

            buffer.close();
            con.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getSana(){
        return sana;
    }

    public String getHarorat(){
        return harorat;
    }
}
