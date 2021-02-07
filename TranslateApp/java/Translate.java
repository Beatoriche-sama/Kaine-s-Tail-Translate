import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Translate {
    private String language, key, serviceName, pyLibsPath;
    private final Language[] languages =
            {new Language("en", "English"),
                    new Language("ru", "Russian"),
                    new Language("ja", "Japanese")};

    public Translate() {
        language = "en";
        serviceName = "DeepL (free)";
        URL libsURL = Translate.class.getResource("pyLibs");
        try {
            pyLibsPath = Paths.get(libsURL.toURI()).toFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> translateDeeplFree(String textToTranslate) throws IOException,
            JSONException, URISyntaxException {
        ArrayList <String> translatedArrayList;
        URL resource = switch (serviceName) {
            case "DeepL (free)" -> Translate.class.getResource("DeepLAPI.py");
            case "Google (free)" -> Translate.class.getResource("GoogleAPI.py");
            default -> throw new IllegalStateException("Unexpected value: " + serviceName);
        };
        String filePath = Paths.get(resource.toURI()).toFile().getPath();
        String response = runPythonScrypt(textToTranslate, filePath);
        if (serviceName.equals("DeepL (free)")){
            translatedArrayList = parseJSONResponse(response);
        }else {
            translatedArrayList = new ArrayList<>();
            translatedArrayList.add(response);
        }
        return translatedArrayList;
    }

    private String runPythonScrypt(String requestText, String filepath) throws IOException {
        String[] executable = {
                "python", filepath,
                " ", requestText, " ", language, " ", pyLibsPath
        };
        Process process = Runtime.getRuntime().exec(executable);
        InputStream stdout = process.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout,
                StandardCharsets.UTF_8));
        String line;
        StringBuilder jsonString = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (IOException e) {
            System.out.println("Exception in reading output" + e.toString());
        }
        return String.valueOf(jsonString);
    }

    private ArrayList<String> parseJSONResponse(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        JSONArray translation = obj.getJSONObject("result").getJSONArray("translations");
        JSONArray beams = translation.getJSONObject(0).getJSONArray("beams");
        ArrayList<String> translatedStrings = new ArrayList<>();

        for (int i = 0; i < beams.length(); i++) {
            JSONObject postProcess = beams.getJSONObject(i);
            translatedStrings.add(postProcess.getString("postprocessed_sentence"));
        }
        System.out.println(translatedStrings);
        return translatedStrings;
    }

    public void translateWithKey(String text, String targetLang) throws IOException {
        String jsonInputString = "data = {\n" +
                "                'target_lang' : '" + targetLang + "',  \n" +
                "                            'auth_key' : '" + key + "',\n" +
                "                'text': '" + text + "'\n" +
                "                }";

        final Content postResult = Request.Post("https://api.deepl.com/v2/translate")
                .connectTimeout(10000)
                .bodyString(jsonInputString, ContentType.APPLICATION_JSON)
                .execute().returnContent();
        System.out.println(postResult.asString());
    }
     /*
     translate(text, source_lang = NULL, target_lang = "EN",
  tag_handling = NULL, split_sentences = TRUE,
  preserve_formatting = FALSE, get_detect = FALSE, auth_key = "your_key")
      */

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLanguage(String selectedLang) {
        String language = null;
        for (Language lang : languages) {
            if (lang.getFullName().equals(selectedLang))
                language = lang.getShortName();
        }
        return language;
    }
}
