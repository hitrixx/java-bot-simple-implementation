package common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rmesterov on 28-Aug-17.
 */
public class Common{

    public static String generateTemplate(String template, Map<String, Object> params){
        if (params==null) return null;
        Set<String> keySet =params.keySet();
        for (String k : keySet){
            String param ="${"+k+"}";
            template = template.replace(template,param);
        }

        return template;
    }
    public static String read(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }
    }
    public static boolean checkReqExp(String text, String pattern){
        Pattern main_pattern = Pattern.compile(pattern); //"([0-9]+)-([0-9]+)"
        Matcher main_matcher = main_pattern.matcher(text);
        return main_matcher.find();
    }


}
