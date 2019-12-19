import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage(); // Это нам понадобится
        String txt = msg.getText();
        switch (txt) {
            case ("/start"):
                sendMsg(msg,  "Привет! Я бот лучшего сайта о беге RunningLIFE.ru! \n" + "Выбери нужный пункт меню");
                break;

            case ("Помощь"):
                sendMsg(msg, "Бот сайта \"RunningLIFE\" версия 1.0. \n" +
                        "Умеет находить пять последних статей рубрики \"Про бег\"" +
                                   ", или последние пять постов из авторского блога. \n" +
                        "Управление осуществляется кнопочным меню. \n" +
                                   "Напишите мне, если у вас есть вопросы или предложения по новому функционалу @alexanderkholod");
                break;

            case ("Блог автора"):
                sendMsg(msg, "Я нашел для тебя пять постов из блога. \nЗагружаю...");
                Parser.postType = "myblog/";

                try {
                    sendMsg(msg, init(Parser.postType).parsePosts().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case ("Статьи о беге"):
                sendMsg(msg, "Я нашел для тебя пять актуальных статей про бег. \nЗагружаю...");
                Parser.postType = "howtorun/";
                try {
                    sendMsg(msg, init(Parser.postType).parsePosts().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + txt);
        }
    }

    public String getBotUsername() {
        return "@RunningLifeSiteBot";
    }

    public String getBotToken() {
        return "763717881:AAFIK6_aQZlS6--pxXnL2zQ-Y4N1I0-IZPQ";
    }

    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        //Чтобы не крашнулась программа при вылете Exception
        try {
            setButtons(s);
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Блог автора"));
        keyboardFirstRow.add(new KeyboardButton("Статьи о беге"));
/*
        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Блог автора"));
        keyboardSecondRow.add(new KeyboardButton("Статьи о беге"));

 */
        // Еще строчка клавиатуры
        KeyboardRow keyboardHelpRow = new KeyboardRow();
        keyboardHelpRow.add(new KeyboardButton("Помощь"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
       //keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardHelpRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public Parser init(String postType){
        Parser parser = new Parser(postType);
        return parser;
    }
/*
    public static String listToString(List<Parser.Post> list) {
        String result = "+";
        for (int i = 0; i < list.size(); i++) {
            result += " " + list.get(i);
        }
        return result;
    }

 */
}
