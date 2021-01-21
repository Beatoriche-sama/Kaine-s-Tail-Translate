import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class SelectAreaHandler extends MouseAdapter {
    private final JPanel selectArea;
    private final Container container;
    private Point pPressed;
    private final AreaScreenshot areaScreenshot;
    private final TextFromPic textFromPic;
    private final Translate translate;
    private final Color transparentColor, alphaTransparentColor;

    public SelectAreaHandler(Container container, TextFromPic textFromPic, Translate translate) {
        areaScreenshot = new AreaScreenshot();
        this.textFromPic = textFromPic;
        this.translate = translate;
        selectArea = new JPanel();
        transparentColor = new Color(0,0,0,0);
        alphaTransparentColor = new Color(0,0,0, 1);
        selectArea.setBackground(transparentColor);
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
        container.setBackground(transparentColor);
        Point pointSelected = selectArea.getLocation();
        Dimension dimensionSelected = selectArea.getSize();
        BufferedImage bufferedImage;
        try {
            bufferedImage = areaScreenshot.screenshot(selectArea.getLocation(), selectArea.getSize());
            selectArea.setBounds(0, 0, 0, 0);
            String text = textFromPic.recognizeText("jpn", bufferedImage);
            System.out.println("Распознанный текст: " + text);
            String translatedText = translate.translate(text);
            System.out.println("ПЕРЕВЕДЕННЫЙ ТЕКСТ: " + translatedText);

            Font font = new Font("TimesRoman", Font.BOLD, 20);
            FontMetrics fontMetrics = selectArea.getFontMetrics(font);
            String wrappedString = wordWrap(translatedText, fontMetrics, dimensionSelected);
            JLabel labelText = new JLabel(String.valueOf(wrappedString));
            labelText.setFont(font);

            JPanel panelText = new JPanel();
            JButton deleteButton = new JButton("Delete");
            Dimension buttonD = deleteButton.getPreferredSize();
            Dimension labelSize = labelText.getPreferredSize();
            panelText.add(deleteButton);
            panelText.add(labelText);

            panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));
            panelText.setBounds(pointSelected.x, pointSelected.y,
                    labelSize.width, labelSize.height + buttonD.height);
            panelText.setBackground(Color.lightGray);
            deleteButton.addActionListener(e1 -> {
                container.remove(panelText);
                container.repaint();
            });

            container.setBackground(alphaTransparentColor);
            container.add(panelText);
            container.setBackground(transparentColor);
            container.removeMouseListener(this);
            container.removeMouseMotionListener(this);
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

    private String wordWrap(String oririginalString, FontMetrics fontMetrics,
                            Dimension dimension){
        String[] words = oririginalString.split(" ");
        StringBuilder resultWrapped = new StringBuilder("<html>");
        String checkableString = "";
        for (String s : words) {
            checkableString += " " + s;
            System.out.println("Сейчас проверяемая строка такая: "
                    + checkableString);

            if (fontMetrics.stringWidth(checkableString) > dimension.width) {
                resultWrapped.append("<br/>").append(s);
                checkableString = "";
                System.out.println("Перенос строки.");
            } else {
                resultWrapped.append(" ").append(s);
            }

        }
        return String.valueOf(resultWrapped.append("</html>"));
    }

}
