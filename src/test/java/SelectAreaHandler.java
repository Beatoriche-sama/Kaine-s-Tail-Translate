import net.sourceforge.tess4j.TesseractException;
import org.davidmoten.text.utils.WordWrap;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;

public class SelectAreaHandler extends MouseAdapter {
    private final JPanel selectArea;
    private final Container container;
    private Point pPressed;
    private final AreaScreenshot areaScreenshot;
    private final TextFromPic textFromPic;
    private final Translate translate;

    public SelectAreaHandler(Container container) {
        areaScreenshot = new AreaScreenshot();
        textFromPic = new TextFromPic();
        translate = new Translate();
        selectArea = new JPanel();
        selectArea.setBackground(new Color(0, 0, 0, 0));
        selectArea.setBorder(BorderFactory.createLineBorder(Color.red));
        container.add(selectArea);
        this.container = container;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pPressed = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        Point pDragged = e.getPoint();
        int leftX = Math.min(pPressed.x, pDragged.x);
        int rightX = Math.max(pPressed.x, pDragged.x);
        int leftY = Math.min(pPressed.y, pDragged.y);
        int rightY = Math.max(pPressed.y, pDragged.y);
        selectArea.setBounds(leftX, leftY, rightX - leftX, rightY - leftY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //результат работы метода сунуть
        //в TextFromPic -> recognize();
        //String-значение в DeeplTranslate -> translate
        //String-значение в Label для новой панельки прозрачного экрана
        container.setBackground(new Color(0, 0, 0, 0));
        Point pointSelected = selectArea.getLocation();
        Dimension dimensionSelected = selectArea.getSize();
        BufferedImage bufferedImage;
        try {
            bufferedImage = areaScreenshot.screenshot(selectArea.getLocation(), selectArea.getSize());
            selectArea.setBounds(0, 0, 0, 0);
            String text = textFromPic.recognizeText("eng", bufferedImage);
            System.out.println("Распознанный текст: " + text);
            String translatedText = translate.translate(text);
            System.out.println("ПЕРЕВЕДЕННЫЙ ТЕКСТ: " + translatedText);
            /*
            Font labelFont = label.getFont();
            String labelText = label.getText();

            int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
            int componentWidth = label.getWidth();

            // Find out how much the font can grow in width.
            double widthRatio = (double)componentWidth / (double)stringWidth;

            int newFontSize = (int)(labelFont.getSize() * widthRatio);
            int componentHeight = label.getHeight();

            // Pick a new font size so it will not be larger than the height of label.
            int fontSizeToUse = Math.min(newFontSize, componentHeight);

            // Set the label's font size to the newly determined size.
            label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));

            AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform,
                    true, true);
            Font font = new Font("TimesRoman", Font.BOLD, 12);
            int textwidth = (int) (font.getStringBounds(text, frc).getWidth());
            int textheight = (int) (font.getStringBounds(text, frc).getHeight());
            */


            AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform,
                    true, true);
            Font font = new Font("TimesRoman", Font.BOLD, 14);
            int textwidth = (int) (font.getStringBounds(text, frc).getWidth());
            //int textheight = (int) (font.getStringBounds(text, frc).getHeight());
            JLabel labelText = new JLabel();
            labelText.setFont(font);

            FontMetrics fm = labelText.getFontMetrics(labelText.getFont());

            String[] words = translatedText.split(" ");
            StringBuilder resultWrapped = new StringBuilder("<html>");
            String checkableString = "";
            for (String s : words) {
                checkableString += " " + s;
                System.out.println("Сейчас проверяемая строка такая: "
                + checkableString);

                if (fm.stringWidth(checkableString) > dimensionSelected.width) {
                    resultWrapped.append("<br/>").append(s);
                    checkableString = "";
                    System.out.println("Перенос строки.");
                } else {
                    resultWrapped.append(" ").append(s);
                }

            }
            resultWrapped.append("</html>");
            System.out.println("Результат: " + resultWrapped);
            labelText.setText(String.valueOf(resultWrapped));
            JPanel panelText = new JPanel();
            //panelText.setLocation();
            panelText.setBounds(pointSelected.x, pointSelected.y,
                    dimensionSelected.width, dimensionSelected.height);
            panelText.setBackground(Color.lightGray);

            //translatePanel.setBorder(new LineBorder(Color.BLACK));
            panelText.add(labelText);
            container.setBackground(new Color(0, 0, 0, 1));
            container.add(panelText);
            container.setBackground(new Color(0, 0, 0, 0));

        } catch (Exception awtException) {
            awtException.printStackTrace();
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        System.out.println("clicked");
        selectArea.setBounds(0, 0, 0, 0);
    }

    /* для панельки
    @Override
    public void mouseEntered(MouseEvent e) {
        //можно ввести предложение удалить область
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
     */

}
