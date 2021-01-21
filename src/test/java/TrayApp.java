import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;

public class TrayApp {
    private Translate translateInstance;
    private TextFromPic ocrInstance;

    public TrayApp() {
        if (!SystemTray.isSupported()) {
            return;
        }
        ocrInstance = new TextFromPic();
        translateInstance = new Translate();
        PopupMenu trayMenu = new PopupMenu();
        MenuItem selectItem = new MenuItem("Select area");
        MenuItem exitItem = new MenuItem("Exit");
        Menu serviceSubMenu = new Menu("Translate service");
        Menu languageItem = new Menu("Target language");
        FrameTransparent frameTransparent = new FrameTransparent();
        SelectAreaHandler selectArea = new SelectAreaHandler(frameTransparent, ocrInstance, translateInstance);

        trayMenu.add(serviceSubMenu);
        trayMenu.add(languageItem);

        MenuItem googleSubMenu = new MenuItem("Google (default)");
        MenuItem deeplSubMenu = new MenuItem("Deepl (need key)");
        serviceSubMenu.add(googleSubMenu);
        serviceSubMenu.add(deeplSubMenu);
        deeplSubMenu.addActionListener(e -> {
            //проверка на поиск файла с ключом Deepl API
            //если ключа нет -> окошко с вводом ключа
        });

        MenuItem englishItem = new MenuItem("English");
        MenuItem russianItem = new MenuItem("Russian");
        MenuItem japaneseItem = new MenuItem("Japanese");

        ActionListener languageListener = e -> {
            MenuItem item = (MenuItem) e.getSource();
            String lang = item.getLabel().toLowerCase(Locale.ROOT).substring(0,2);
            System.out.println("Language is " + lang);
            translateInstance.setLanguage(lang);
        };

        englishItem.addActionListener(languageListener);
        russianItem.addActionListener(languageListener);
        japaneseItem.addActionListener(languageListener);

        languageItem.add(englishItem);
        languageItem.add(russianItem);
        languageItem.add(japaneseItem);

        selectItem.addActionListener(e -> {
                frameTransparent.addMouseListener(selectArea);
                frameTransparent.addMouseMotionListener(selectArea);
                frameTransparent.setBackground(new Color(0, 0, 0, 1));
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
