package com.HarmyIndustries.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestBot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "@HIndustry_bot";
    }

    @Override
    public String getBotToken() {
        return "token";
    }

    public static void main(String[] args) throws TelegramApiException {
        TestBot bot = new TestBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasSticker()) {
                try {
                    execute(SendSticker.builder().chatId(message.getChatId().toString()).emoji(message.getSticker().getEmoji()).build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println(message.getText());
            if (message.hasText() && (message.getText().equals("анекдот")  || message.getText().equals("Анекдот"))) {
                try {
//                    execute(SendMessage.builder().chatId(message.getChatId().toString() ).text(message.getText()).build());

                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(getJoke()).build());

                } catch (TelegramApiException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Message message = update.getMessage();
                try {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text("не розумию").build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    public String getJoke() throws IOException {

        List<String> jokes = getJokes();

        String joke = jokes.get((int) (Math.random() * 100));
        jokes.remove(joke);
        return joke;
    }

    public List<String> getJokes() throws IOException {

        Document doc = Jsoup.connect("https://www.anecdote.tv/no-category/6566-anekdoty-kategorii-b-samye-smeshnye-130-shtuk.html").get();

        Elements jokes = doc.getElementsByClass("su-note-inner su-u-clearfix su-u-trim");

        List<String> jokesList = new ArrayList<>();

        for (Element el : jokes) {
            String jokeTxt = el.text();
            jokesList.add(jokeTxt);
        }

        return jokesList;

    }


}
