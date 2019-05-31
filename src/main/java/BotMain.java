import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BotMain extends TelegramLongPollingBot {

    private static String BOT_TOKEN = "...";
    private static String BOT_USERNAME = "visa_info_bot";
    // TODO add more readable bot description
    private static String HELLO_MESSAGE = "Привет! Введите название интересующей вас страны."
            + "Бот покажет информацию, имеющуюся на портале Travel.ru относительно вопроса получения визы выбранного государства.";
    private static String SEARCH_SUCCESS = "Результаты поиска: ";
    private static String SEARCH_FAILURE = "Страна не найдена. Попробуйте изменить поиск.";

    private static int BUTTONS_PER_ROW = 2;

    // instance of database class
    static Datasourse datasourse = Datasourse.getInstance();

    public static void main(String[] args) {
        // initializing API
        ApiContextInitializer.init();
        // creating object of TelegramBotsApi
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        // register a bot
        try {
            telegramBotsApi.registerBot(new BotMain());
        } catch (TelegramApiRequestException e) {
            System.out.println("Ошибка при регистрации бота " + e.getMessage());
            e.printStackTrace();
        }
    }

    // the method that monitors any bot state updates (e.g. new message received)
    @Override
    public void onUpdateReceived(Update update) {
        // retrieving text received from update object
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String strMessage = message.getText().trim();
            List<Country> countries = new ArrayList<>();

            if (strMessage.equals("/start")) {
                sendResponse(message, countries, HELLO_MESSAGE);
            } else {
                datasourse.openConnection();
                datasourse.useDatabase();
                Country country = datasourse.queryCountryDataPrecise(strMessage);
                if (country != null) {
                    String visaInfoUrl = urlFixedForTelegram(country.getVisaInfoLink());
                    System.out.println(visaInfoUrl);
                    countries.add(country);
                    sendResponse(message, countries, visaInfoUrl);
                } else {
                    countries.addAll(datasourse.queryCountryDataEstimate(strMessage).values());
                    if (!countries.isEmpty()) {
                        sendResponse(message, countries, SEARCH_SUCCESS);
                    } else {
                        sendResponse(message, countries, SEARCH_FAILURE);
                    }
                }
                datasourse.closeConnection();
                System.out.println("DB connection closed");
            }
        }
    }

    public void sendResponse(Message message, List<Country> countries, String text) {
        // this object is for replying on client's message
        SendMessage sendMessage = new SendMessage();
        // включаем возможность разметки
        sendMessage.enableMarkdown(true);
        // detecting and setting chat id of the client so bot know where to respond
        sendMessage.setChatId(message.getChatId().toString());
        // detecting and setting the exact message to respond of that client
        sendMessage.setReplyToMessageId(message.getMessageId());
        // setting text for bot's response
        sendMessage.setText(text);
        // sending bot's response
        try {
            if (countries.size() > 1) {
                List<String> countriesNames = countries.stream()
                        .map(Country::getCountryNameRu)
                        .collect(Collectors.toList());
                setKeyboard(sendMessage, countriesNames);
            }
            // execute() sends the response
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Exception while responding to the client's message " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void setKeyboard(SendMessage sendMessage, List<String> countriesNames) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        // connecting sendMessage with keyboard
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        // выводить клавиатуру всем пользователлям
        replyKeyboardMarkup.setSelective(true);
        // adjust keyboard size automatically
        replyKeyboardMarkup.setResizeKeyboard(true);
        // hide keyboard after usage
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // list of keys rows
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        // adding n buttons per row
        for (int i = 0; i < countriesNames.size(); i++) {
            String buttonName = countriesNames.get(i);
            if (i % BUTTONS_PER_ROW == 0) {
                keyboardRowList.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
            keyboardRow.add(new KeyboardButton(buttonName));
        }
        // adding what's left behind
        if (!keyboardRow.isEmpty()) {
            keyboardRowList.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    private String urlFixedForTelegram(String url) {
        return url.replace("_", "\\_");
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
