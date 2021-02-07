import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {

    public static JScrollPane createTranslatePanel(Container container,
                                                   ArrayList<String> translatedText,
                                                   String recognizedText){
        BufferedImage image = null;
        try {
            image = ImageIO.read(Utils.class.getResource("background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage finalImage = image;
        JPanel panelText = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalImage, 0, 0, null);
            }
        };

        JScrollPane scrollPane = new JScrollPane(panelText,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Font font = new Font("TimesRoman", Font.BOLD, 20);

        JTextArea translatedTextArea = createTextArea(translatedText.get(0), font);
        JTextArea originalTextArea = createTextArea(recognizedText, font);
        originalTextArea.setVisible(false);

        JLabel originalLabel = new JLabel("Original:");
        originalLabel.setVisible(false);

        JButton deleteButton = new JButton("Delete");
        JButton nextButton = new JButton(">>");
        JButton previousButton = new JButton("<<");
        JButton recognizedTextButton = new JButton("Show original");

        panelText.setLayout(new MigLayout());
        panelText.add(previousButton, "flowx, split 3");
        panelText.add(nextButton);
        panelText.add(recognizedTextButton);
        panelText.add(deleteButton, "wrap");
        panelText.add(new JLabel("Translate:"));
        panelText.add(originalLabel, "wrap");

        panelText.add(translatedTextArea);
        panelText.add(originalTextArea, "hidemode 3");

        recognizedTextButton.addActionListener(ee2 -> {
            if (recognizedTextButton.getText().equals("Show original")) {
                recognizedTextButton.setText("Hide original");
            } else {
                recognizedTextButton.setText("Show original");
            }
            originalTextArea.setVisible(!originalTextArea.isVisible());
            originalLabel.setVisible(!originalLabel.isVisible());
        });

        deleteButton.addActionListener(e1 -> {
            container.remove(scrollPane);
            container.repaint();
        });

        nextButton.addActionListener(ee -> {
            String newText;
            String oldText = translatedTextArea.getText();
            int index = translatedText.indexOf(oldText);
            if (index == translatedText.size() - 1) {
                newText = translatedText.get(0);
            } else {
                newText = translatedText.get(index + 1);
            }
            translatedTextArea.setText(newText);
        });

        previousButton.addActionListener(ee1 -> {
            String newText;
            String oldText = translatedTextArea.getText();
            int index = translatedText.indexOf(oldText);
            if (index == 0) {
                newText = translatedText.get(translatedText.size() - 1);
            } else {
                newText = translatedText.get(index - 1);
            }
            translatedTextArea.setText(newText);
        });

        return scrollPane;
    }

    private static JTextArea createTextArea(String text, Font font) {
        JTextArea textArea = new JTextArea(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBackground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setFont(font);
        textArea.setBorder(BorderFactory.createLineBorder(Color.black));
        return textArea;
    }
}
