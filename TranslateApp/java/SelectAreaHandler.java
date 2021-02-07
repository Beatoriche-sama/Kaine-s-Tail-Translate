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
    private Point pPressed, pointSelected;
    private Dimension dimensionSelected;
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
        container.add(selectArea, "hidemode 3");
        container.setLayout(new MigLayout());
        this.container = container;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectArea.setVisible(true);
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
        pointSelected = selectArea.getLocation();
        dimensionSelected = selectArea.getSize();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        container.setBackground(transparentColor);
        selectArea.setVisible(false);
        container.repaint();
        BufferedImage bufferedImage;
        ArrayList <String> translatedText = new ArrayList<>();
        String recognizedText = "";
        try {
            bufferedImage = areaScreenshot.screenshot(pointSelected, dimensionSelected);
            recognizedText = textFromPic.recognizeText("eng+jpn+rus", bufferedImage);
            translatedText = translate.translateDeeplFree(recognizedText);
        } catch (Exception awtException) {
            awtException.printStackTrace();
        }
        JScrollPane panelText = Utils.createTranslatePanel(container, translatedText, recognizedText);
        container.setBackground(alphaTransparentColor);
        container.add(panelText, "w " + dimensionSelected.width +
                ", pos " + pointSelected.x + " " + pointSelected.y);
        container.setBackground(transparentColor);
        container.removeMouseListener(this);
        container.removeMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        System.out.println("clicked");
    }


}
