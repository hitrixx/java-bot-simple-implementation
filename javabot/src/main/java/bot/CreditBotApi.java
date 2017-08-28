package bot;

import com.google.gson.Gson;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import common.Common;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmesterov on 05-Jul-17.
 */

@WebServlet("/tbotapi")
public class CreditBotApi extends HttpServlet {
    Logger logger = Logger.getLogger("tbotapi");

    private static Bot bot = new TelegramBotImpl("bot token");
    private static Map<String,Long> phoneOTP= new HashMap<>();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("get");
        String qString = req.getQueryString();
        logger.info(req.getRequestURL()+"?"+qString);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String request = Common.read(req.getInputStream());
        logger.info(request);
        Chat currChat = new Chat();
        Chat prevChat = null;
        Message message = null;
        User user = null;
        SendMessage sendMessage = null;
        Step nextStep = null;
        boolean is_new_process = false;
        Command command = null;
        Map<String, Object> params = new HashMap<>();
        try {
            JSONObject jReq = new JSONObject(request);
            message = new Gson().fromJson(jReq.getJSONObject("message").toString(),Message.class);
            logger.info("chat_id = "+message.chat().id()+" text = "+message.text());

            user = new User().loadByChatId(message.chat().id());
            if (user==null){ // new client
                user = new User();
                user.setChat_id(message.chat().id());
                user.setLanguage_code(message.from().languageCode());
                user.setFirst_name(message.from().firstName());
                user.setLast_name(message.from().lastName());
                user.setChannel("telegrambotapi");
                user.save();
            }
                // existing client
            if (message.entities()!=null && message.entities().length > 0 && message.entities()[0].type() == MessageEntity.Type.bot_command) {
                //TODO start new process instance
                logger.info("Started new process = "+ message.text());


                command = new Command().loadByName(message.text());
                is_new_process=true;
            }
            prevChat = new Chat().loadLastByFromUserId(user.getId());

            logger.info(new Gson().toJson(prevChat));

            if (prevChat != null && !is_new_process) {
                Step prevStep = prevChat.getStep();

                Question prevQ = prevStep.getQuestion();
                if (!prevStep.validate(message.text())) { //check client's answer
                    sendMessage =  new SendMessage(message.chat().id(), prevQ.getInvalid_answer_text())
                                    .parseMode(ParseMode.HTML)
                                    .disableWebPagePreview(true)
                                    .disableNotification(true);
                    currChat.setStep_id(prevStep.getId());
                    return;
                }
                params = new ClientAnswer().getParams(user.getId());
                nextStep = new Step().loadNextStepById(prevChat.getStep_id());
                
            } else if (is_new_process) {
                nextStep = new Step().loadFirstStepByCommandId(new Command().loadByName(message.text()).getId());
            } else {
                sendMessage = new SendMessage(message.chat().id(), "select action")
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true);
                return;
            }

            Question nextQ = nextStep.getQuestion();
            nextQ.setParams(params);
            sendMessage = new SendMessage(message.chat().id(), nextQ.getQuestion_text_loc1()) //ask question
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
            //TODO otp_code
            currChat.setStep_id(nextStep.getId());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            resp.setStatus(200);
            resp.setHeader("Content-Type", "application/json; charset=UTF-8");
            StringBuilder sb= new StringBuilder();

            int command_id = 0;
            if (prevChat!=null) {
               command_id=prevChat.getCommand_id();
            } else {
                if (command!=null) {
                    command_id=command.getId();
                }
            }
            currChat.setCommand_id(command_id);
            currChat.setReq(request);
            currChat.setMessage_text(message.text());
            currChat.setMessage_date(new Timestamp(message.date()*1000L));
            currChat.setTo_user_id(1);
            currChat.setFrom_user_id(user.getId());
            currChat.setMessage_id(message.messageId());
            currChat.setChat_id(message.chat().id());

            if (!is_new_process && prevChat!=null) {
                ClientAnswer answer = new ClientAnswer().load(user.getId(), prevChat.getStep().getQuestion_id());
                if (answer == null) {
                    answer = new ClientAnswer();
                }
                answer.setQuestion_id(prevChat.getStep().getQuestion_id());
                answer.setParam_name(prevChat.getStep().getQuestion().getParam_name());
                answer.setText(message.text());
                answer.setBot_user_id(user.getId());
                answer.setClient_id(user.getClient_id());
                answer.save();
            }

            if (sendMessage!=null) {
                bot.sendMessage(sendMessage, getCallBack(currChat));
            }
            currChat.save();
            params.clear();
            params = null;
        }
    }

    private Callback<SendMessage, SendResponse> getCallBack(Chat chat){
        return
                new Callback<SendMessage, SendResponse>() {
                    @Override
                    public void onResponse(SendMessage request, SendResponse response) {
                        chat.setResp(new Gson().toJson(response, response.getClass()));
                        chat.save();
                    }
                    @Override
                    public void onFailure(SendMessage request, IOException e) {
                        logger.info("onFailure");
                        e.printStackTrace();
                    }
                };
    }

}
