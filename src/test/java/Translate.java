import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate {
    //"auto" - from
    //to - выбор языка внутри трея
    //en - english, ja - japanese, ru - russian


    private static String regex(String text){
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(text);
        String regexResult = null;
        if (m.find()) {
            regexResult = m.group(1);
        }
        return regexResult;
    }

    public String translate(String textToTranslate) throws IOException {
        textToTranslate = URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8);
        URL url = new URL("http://translate.googleapis.com/translate_a/single?client=gtx&sl=" + "en" + "&tl="
                + "ru" + "&dt=t&q=" + textToTranslate + "&ie=UTF-8&oe=UTF-8");

        URLConnection uc = url.openConnection();
        uc.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder result = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
        return regex(String.valueOf(result));
    }
}
