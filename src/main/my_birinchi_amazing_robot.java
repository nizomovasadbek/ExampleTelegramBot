import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class my_birinchi_amazing_robot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        SendMessage msg = new SendMessage();
        LocalDateTime vaqt = LocalDateTime.now();
        long chat_id = 0;

        if(update.getMessage().isChannelMessage() && update.getChannelPost().hasText())
        try {
            execute(new SendMessage("@cpp_darslari_n01", update.getChannelPost().getChatId().toString()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();



            if (update.getMessage().getText().equals("/start")) {



            } else
            if (command.equals("/pic")){
                SendPhoto sp = new SendPhoto()
                        .setCaption("Siz so'ragan rasm")
                        .setChatId(update.getMessage().getChatId())
                        .setPhoto("AgADAgADNawxG1-ViEjnT9AlIn2cIWabOQ8ABF6BxLOewK8uuLQFAAEC");
                try{
                    execute(sp);
                } catch(TelegramApiException e){
                    e.printStackTrace();
                }
            } else
                if(update.getMessage().getText().equals("uid")){
                    if (update.getMessage().getFrom().getId() == 535746741){
                        long u_id = update.getMessage().getReplyToMessage().getFrom().getId();
                        msg.setText(update.getMessage().getReplyToMessage().getFrom().getFirstName() + "\uD83C\uDD94: " + u_id);
                    }
                } else
            if (command.equalsIgnoreCase("haqida")) {
        msg.setText("Bot tuzuvchisi\uD83D\uDCBB Nizomov Asadbek.");
    } else if (command.equalsIgnoreCase("/start")) {
        msg.setText("Bot ga hush kelibsiz");
    } else if (command.equalsIgnoreCase("soat")) {
        msg.setReplyToMessageId(msg.getReplyToMessageId());
        int soat = vaqt.getHour();
        final int MINUT = vaqt.getMinute();
        msg.setText(String.format("Aniq vaqt⏱:\t%02d:%02d", soat, MINUT));
        System.out.println(update.getMessage().getFrom().getUserName() + ":\tSoat so'radi.");
    } else
                if(command.equalsIgnoreCase("sana")){
                    final int SANA = vaqt.getDayOfMonth();
                    String OY = vaqt.getMonth().toString();

                    switch (OY){
                        case "JANUARY" : OY = "Yanvar"; break;
                        case "FEBRUARY": OY = "Fevral"; break;
                        case "MARCH": OY = "Mart"; break;
                        case "APRIL": OY = "Aprel"; break;
                        case "MAY": OY = "May"; break;
                        case "JUNE": OY = "Iyun"; break;
                        case "JULY": OY = "Iyul"; break;
                        case "AUGUST": OY = "Avgust"; break;
                        case "SEPTEMBER": OY = "Sentabr"; break;
                        case "OCTOBER": OY = "Oktabr"; break;
                        case "NOVEMBER": OY = "Noyabr"; break;
                        case "DECEMBER": OY = "Dekabr"; break;
                    }
                    final int YIL = vaqt.getYear();
                    msg.setText("Sana\uD83D\uDCC6:\t" + SANA + "-" + OY + "\t"+YIL);
                    System.out.println(update.getMessage().getFrom().getUserName() + "\tSana so'radi");
                } else
                if (command.equalsIgnoreCase("mening do'stim kim")) {
                msg.setText("Sening do'sting qoravoy");
            } else if (command.equalsIgnoreCase("id")) {
                msg.setText("\uD83C\uDD94" + update.getMessage().getFrom().getId().toString());
                System.out.println(update.getMessage().getFrom().getUserName() + " Id adresini so'radi.");
                System.out.println("Uning ID adresi:\t" + update.getMessage().getFrom().getId());
            } else if (command.equalsIgnoreCase("ism")) {
                System.out.println(update.getMessage().getFrom().getFirstName());
                msg.setText(update.getMessage().getFrom().getFirstName());
            } else if (command.equalsIgnoreCase("salom")) {
                    if(update.getMessage().getChatId() == 535746741)
                        msg.setText("Mana bu mening boshlig'im");
                    else
                    if(update.getMessage().getChatId() == 649244901)
                        msg.setText("Xo'jayin yaxshimisiz");
                    else if(update.getMessage().getChatId() == 701975070)
                        msg.setText("Ha, Toqchiliq nima gaplar");
                    else
                msg.setText("Yaxshimisiz oka");
                System.out.println(update.getMessage().getFrom().getUserName() + " \"Salom\" yo'lladi.\nMen nima deyishni bilmay Yaxshimisiz oka dedim.");
            } else if (command.equalsIgnoreCase("fam")) {
                if (update.getMessage().getFrom().getLastName() != null)
                    msg.setText(update.getMessage().getFrom().getLastName());
                else {
                    msg.setText("Sizning familiyangiz yo'q");
                    System.out.println(update.getMessage().getFrom().getUserName() + " Familiyasini so'radi.");
                }
            }
                else

                    if(command.equals("/markup")){
                        msg.setText("Sizning tugmalaringiz");
                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                        List<KeyboardRow> rows = new ArrayList<>();
                        KeyboardRow row = new KeyboardRow();

                        row.add("1-qator 1-tugma");
                        row.add("1-qator 2-tugma");
                        row.add("1-qator 3-tugma");

                        rows.add(row);

                        row = new KeyboardRow();

                        row.add("2-qator 1-tugma");
                        row.add("2-qator 2-tugma");
                        row.add("2-qator 3-tugma");

                        rows.add(row);
                        replyKeyboardMarkup.setKeyboard(rows);
                        msg.setReplyMarkup(replyKeyboardMarkup);
                    }else
                        if (command.equals("/hide")){
                            SendMessage sendMessage = new SendMessage()
                                    .setChatId(update.getMessage().getChatId())
                                    .setText("Knopkalar yo'qotildi.");
                            ReplyKeyboardRemove ochirish = new ReplyKeyboardRemove();
                            sendMessage.setReplyMarkup(ochirish);
                            try{
                                execute(sendMessage);
                            } catch (TelegramApiException e){
                                e.printStackTrace();
                            }
                        } else if (command.equalsIgnoreCase("getlink")){
                            msg.setText("https://t.me/my_birinchi_amazing_robot");
                            System.out.println("Link " + update.getMessage().getFrom().getUserName() + " ga berildi.");
                        } else

                    return;

            msg.setChatId(update.getMessage().getChatId());
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if(update.hasMessage() && update.getMessage().hasPhoto()){
            String xabar = update.getMessage().getText();
            chat_id = update.getMessage().getChatId();
            List<PhotoSize> rasmlar = update.getMessage().getPhoto();
            String f_id = rasmlar.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            int f_width = rasmlar.stream()
                    .sorted(Comparator.comparing(PhotoSize::getWidth).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
            int f_height = rasmlar.stream()
                    .sorted(Comparator.comparing(PhotoSize::getHeight).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
            String caption = "File id\uD83C\uDD94 " + f_id + "\nEni: "+Integer.toString(f_width) + "\nBo'yi: " + Integer.toString(f_height);
            SendPhoto msg_photo = new SendPhoto()
                    .setChatId("535746741")
                    .setPhoto(f_id)
                    .setCaption(caption);

            try{
                execute(msg_photo);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
            }
    }

    public String getBotUsername() {
        return "my_birinchi_amazing_robot";
    }

    public String getBotToken() {
        return "708735688:AAEFE_h-a2O1iTlrZW73B2kLrg1o51lTVB0";
    }
}
