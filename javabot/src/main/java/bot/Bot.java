package bot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * Created by rmesterov on 10-Aug-17.
 */
public abstract class Bot {
    /**
     * Returns current client's path
     * @param message message from bot api
     */
    abstract String getPath(Message message);


    /**
     * Returns current client's step
     * @param message message rom bot api
     */
    abstract String getStep(Message message);

    /**
     * Returns current client if exist, if no - null
     * @param message message from bot api
     */
    abstract String getClient(Message message);

    /**
     * Returns current question to client
     * @param message message from bot api
     */
    abstract String getQuestion(Message message);
    abstract public String sendMessage (SendMessage request);
    abstract public String sendDocument (SendDocument request);
    abstract public void sendMessage (SendMessage request,  Callback<SendMessage, SendResponse> callback);
    abstract public void sendMessage (String channel, String text);

}
