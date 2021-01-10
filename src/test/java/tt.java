import org.davidmoten.text.utils.WordWrap;

import java.io.UnsupportedEncodingException;

public class tt {
    public static void main(String[] args) throws UnsupportedEncodingException {
        //пофиксить иконку для трея
        //разобраться с OCR Tesseract (плохо распознает текст)
        //и дать ему японский и другие языки для распознания

        new TrayApp();


        //выделенная область скриншотится
        //-> bufferedImage отправляется в Tesseract
        //-> текст отправляется в Deepl
        //-> на месте выделенной области появляется JPanel с текстом внутри нее
    }
}
