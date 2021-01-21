import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate {
    private String language;
    //"auto" - from
    //to - выбор языка внутри трея
    //en - english, ja - japanese, ru - russian

    public Translate(){
        language = "en";
    }

    private String regex(String text){
        Pattern spliteArraysPattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = spliteArraysPattern.matcher(text);
        Pattern spliteTranslatedPattern = Pattern.compile("\".*?\"");

        StringBuilder regexResult = new StringBuilder();
        while (m.find()) {
            String s = m.group(1);
            s = s.replaceAll("\\\\n", "");
            Matcher m1 = spliteTranslatedPattern.matcher(s);
            if (m1.find()) {
                String ss = m1.group(0);
                regexResult.append(ss);
            }
        }

        return regexResult.toString()
                .replaceAll("cce7c67b3f2439089dd6b428e0b83b88|\"|" +
                        "f9f74f6684a8d2cc5e759eb391adbee9", "");
    }

    public String translate(String textToTranslate) throws IOException {
        textToTranslate = URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8);
        URL url = new URL("http://translate.googleapis.com/translate_a/single?client=gtx&sl=" + "ja" + "&tl="
                + language + "&dt=t&q=" + textToTranslate + "&ie=UTF-8&oe=UTF-8");
        System.out.println(url);
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

    public void setLanguage(String language){
        this.language = language;
    }
}
