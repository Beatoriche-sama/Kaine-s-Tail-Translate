import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SelectAreaHandler extends MouseAdapter {
    private final JPanel selectArea;
    private final Container container;
    private Point pPressed;
    private final AreaScreenshot areaScreenshot;
    private final TextFromPic textFromPic;
    private final Translate translate;
    private final Color transparentColor, alphaTransparentColor;

    public SelectAreaHandler(Container container, TextFromPic textFromPic,
                             Translate translate) throws AWTException {
        areaScreenshot = new AreaScreenshot();
        this.textFromPic = textFromPic;
        this.translate = translate;
        selectArea = new JPanel();
        transparentColor = new Color(0,0,0,0);
        alphaTransparentColor = new Color(0,0,0, 1);
        selectArea.setBackground(transparentColor);
        selectArea.setBorder(BorderFactory.createLineBorder(Color.red));
        container.setLayout(new MigLayout());
        //добавлять не здесь select
        container.add(selectArea);
        this.container = container;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //add(selected)
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
            bufferedImage = areaScreenshot.screenshot(pointSelected, dimensionSelected);
            String recognizedText = textFromPic.recognizeText("eng+jpn+rus", bufferedImage);
            //remove(Selected) вместо setBounds
            selectArea.setBounds(0, 0, 0, 0);
            ArrayList <String> translatedText = translate.translateDeeplFree(recognizedText);
            Font font = new Font("TimesRoman", Font.BOLD, 20);

            JLabel translatedTextLabel = new JLabel(translatedText.get(0));
            translatedTextLabel.setLayout(new MigLayout());
            translatedTextLabel.setFont(font);

            JLabel originalTextLabel = new JLabel(recognizedText);
            originalTextLabel.setFont(font);
            originalTextLabel.setVisible(false);

            JPanel panelText = new JPanel();
            JButton deleteButton = new JButton("Delete");
            JButton nextButton = new JButton("Next variant");
            JButton previousButton = new JButton("Previous variant");
            JButton recognizedTextButton = new JButton("Show recognized");

            panelText.setLayout(new MigLayout());
            panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));
            panelText.add(recognizedTextButton);
            panelText.add(deleteButton);
            panelText.add(previousButton, "back");
            panelText.add(nextButton, "next");
            panelText.add(translatedTextLabel, "wmax " + dimensionSelected.width +
                    " pos " + pointSelected.x + " " + pointSelected.y);
            panelText.add(originalTextLabel);
            panelText.setBackground(Color.lightGray);

            recognizedTextButton.addActionListener(ee2->{
                if(recognizedTextButton.getText().equals("Show recognized")){
                    recognizedTextButton.setText("Hide recognized");
                }else {
                    recognizedTextButton.setText("Show recognized");
                }
                originalTextLabel.setVisible(!originalTextLabel.isVisible());
            });

            deleteButton.addActionListener(e1 -> {
                container.remove(panelText);
                container.repaint();
            });

            nextButton.addActionListener(ee->{
                String newText;
                String oldText = translatedTextLabel.getText();
                int index = translatedText.indexOf(oldText);
                if(index == translatedText.size() - 1){
                    newText = translatedText.get(0);
                }else {
                    newText = translatedText.get(index + 1);
                }
                translatedTextLabel.setText(newText);
            });

            previousButton.addActionListener(ee1->{
                String newText;
                String oldText = translatedTextLabel.getText();
                int index = translatedText.indexOf(oldText);
                if(index == 0){
                    newText = translatedText.get(translatedText.size() - 1);
                }else {
                    newText = translatedText.get(index - 1);
                }
                translatedTextLabel.setText(newText);
            });

            container.setBackground(alphaTransparentColor);
            //, "pos " + pointSelected.x + " " +
            //                    pointSelected.y
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
        //selectArea.setBounds(0, 0, 0, 0);
    }


}
