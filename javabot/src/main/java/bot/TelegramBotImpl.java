package bot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created by rmesterov on 10-Aug-17.
 */
public class TelegramBotImpl extends Bot {
    TelegramBot bot = null;

    public TelegramBotImpl(String token){
        if (bot == null){
            bot = TelegramBotAdapter.build(token);
        }

    }

    @Override
    public String getPath(Message message) {
        return null;
    }

    @Override
    public String getStep(Message message) {
        return null;
    }

    @Override
    public String getClient(Message message) {
        return null;
    }

    @Override
    public String getQuestion(Message message){
        return null;
    }

    @Override
    public void sendMessage (SendMessage request,  Callback<SendMessage, SendResponse> callback){
            bot.execute(request, callback);
    }

    /**
     * Returns Bot answer
     * @param request message to bot api
     */
    public String sendMessage(SendMessage request) {
        return bot.execute(request).toString();
    }
    public String sendDocument (SendDocument request) {
        return bot.execute(request).toString();
    }
    @Override
    public void sendMessage(String channel, String text) {
        try {

            List<User> users = new User().loadByChannel(channel);

            for (User u : users) {
                SendMessage sendMessage = new SendMessage(u.getChat_id(), text)
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true);

                bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
                            @Override
                            public void onResponse(SendMessage request, SendResponse response) {
                            }
                            @Override
                            public void onFailure(SendMessage request, IOException e) {
                                try {
                                    throw (e);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                );
            }
        } catch (Exception e) {
            throw e;
        }
    }

}
