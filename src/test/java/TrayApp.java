import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.net.URL;

public class TrayApp {

    public TrayApp() {
        if (!SystemTray.isSupported()) {
            return;
        }

        PopupMenu trayMenu = new PopupMenu();
        MenuItem selectItem = new MenuItem("Select area");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem testItem = new MenuItem("Test");
        FrameTransparent frameTransparent = new FrameTransparent();
        SelectAreaHandler selectArea = new SelectAreaHandler(frameTransparent);

        trayMenu.add(testItem);
        testItem.addActionListener(e -> {
            System.out.println("МЯУ");
            JPanel panel = new JPanel();
            JLabel label = new JLabel("<html><p>Hello World! blah blah blah</p></html>", SwingConstants.CENTER);
            panel.setBounds(new Rectangle(400, 40, 100, 400));
            panel.setBackground(Color.CYAN);
            frameTransparent.setBackground(new Color(0, 0, 0, 1));
            frameTransparent.add(panel);
            panel.add(label);
            frameTransparent.setBackground(new Color(0, 0, 0, 0));
        });
        selectItem.addActionListener(e -> {
            if (selectItem.getLabel().equals("Select area")) {
                selectItem.setLabel("Stop selecting");
                frameTransparent.addMouseListener(selectArea);
                frameTransparent.addMouseMotionListener(selectArea);
                /*
                frameTransparent.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyChar() == 'x') {
                            System.out.println("Exiting");
                            System.exit(0);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                 */
                frameTransparent.setBackground(new Color(0, 0, 0, 1));
            } else {
                frameTransparent.removeMouseListener(selectArea);
                frameTransparent.removeMouseMotionListener(selectArea);
            }
        });
        exitItem.addActionListener(e -> System.exit(0));
        trayMenu.add(selectItem);
        trayMenu.add(exitItem);

        URL imageURL = TrayApp.class.getResource("trayIcon.jpg");

        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);

        TrayIcon trayIcon = new TrayIcon(icon, "Kaine's Tail", trayMenu);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        trayIcon.displayMessage("Kaine's Tail", "Kaine's Tail is ready to translate",
                TrayIcon.MessageType.INFO);
    }
}
