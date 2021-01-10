import com.google.common.net.UrlEscapers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.UnsupportedEncodingException;

public class Translate {
    //"auto" - from
    //to - выбор языка внутри трея
    //en - english, ja - japanese, ru - russian
    /*
     String url = "https://www.deepl.com/translator#"
                        + from.getLanguageCode()
                        + "/"
                        + to.getLanguageCode()
                        + "/"
                        +  UrlEscapers.urlFragmentEscaper().escape(text);
     */
    public String urlEncode(String baseURL, String query) {
        String encodeStr = UrlEscapers.urlFragmentEscaper().escape(query);
        //String encodeStr = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        return baseURL + encodeStr;
    }

    public String translate(String textToTranslate) throws InterruptedException, UnsupportedEncodingException {
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver.exe");
        /*
        JBrowserDriver driver = new JBrowserDriver(Settings.builder().
                timezone(Timezone.EUROPE_MOSCOW).build());
        */

        DesiredCapabilities desireCaps = new DesiredCapabilities();
        desireCaps.setJavascriptEnabled(true);
        desireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "D:/libs/phantomjs/bin/phantomjs.exe");
        WebDriver driver = new PhantomJSDriver(desireCaps);

        //WebDriver driver = new ChromeDriver(options);
        String url = urlEncode("https://www.deepl.com/translator#en/ru/", textToTranslate);
        driver.get(url);
        Thread.sleep(10000);
        WebElement webElement = driver.findElement(By.className("lmt__translations_as_text__text_btn"));
        String translatedText = webElement.getAttribute("textContent");
        driver.quit();
        return translatedText;
    }
}
