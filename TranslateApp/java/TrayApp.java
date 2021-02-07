import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;

public class TrayApp {
    private Translate translateInstance;
    private TextFromPic ocrInstance;

    public TrayApp() throws AWTException {
        if (!SystemTray.isSupported()) {
            return;
        }
        ocrInstance = new TextFromPic();
        translateInstance = new Translate();
        PopupMenu trayMenu = new PopupMenu();
        Menu ocrSettings = new Menu("OCR settings");
        MenuItem selectItem = new MenuItem("Select area");
        MenuItem exitItem = new MenuItem("Exit");
        Menu serviceSubMenu = new Menu("Translate service");
        Menu languageItem = new Menu("Target language");
        FrameTransparent frameTransparent = new FrameTransparent();
        SelectAreaHandler selectArea = new SelectAreaHandler(frameTransparent, ocrInstance, translateInstance);

        trayMenu.add(selectItem);
        trayMenu.add(serviceSubMenu);
        trayMenu.add(languageItem);
        trayMenu.add(ocrSettings);
        trayMenu.add(exitItem);

        MenuItem improveOCR = new MenuItem("Improve OCR");
        ocrSettings.add(improveOCR);
        improveOCR.addActionListener(eee->{
            String labelText = improveOCR.getLabel();
            if(labelText.contains("✓")){
                System.out.println("Улучшение изображения отклонено.");
                improveOCR.setLabel("Improve OCR");
            }else {
                System.out.println("Улучшение изображения запрошено.");
                improveOCR.setLabel("✓ Improve OCR");
            }
            ocrInstance.setImproveNeeded(!ocrInstance.isImproveNeeded);
        });

        MenuItem googleSubMenuFree = new MenuItem("Google (free)");
        MenuItem deeplSubMenuFree = new MenuItem("DeepL (free)");
        MenuItem deeplSubMenu = new MenuItem("DeepL (need key)");
        serviceSubMenu.add(googleSubMenuFree);
        serviceSubMenu.add(deeplSubMenuFree);
        serviceSubMenu.add(deeplSubMenu);

        ActionListener freeServiceListener = e -> {
            MenuItem menuItem = (MenuItem) e.getSource();
            translateInstance.setServiceName(menuItem.getLabel());
        };
        googleSubMenuFree.addActionListener(freeServiceListener);
        deeplSubMenuFree.addActionListener(freeServiceListener);
        deeplSubMenu.addActionListener(e -> {
            //проверка на поиск файла с ключом Deepl API
            //если ключа нет -> окошко с вводом ключа
            String key = JOptionPane.showInputDialog(frameTransparent,
                    "Enter your key",
                    "Key input (￣ω￣)",
                    JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(frameTransparent,
                    "Your key is: " + key);
            translateInstance.setKey(key);
        });

        MenuItem englishItem = new MenuItem("English");
        MenuItem russianItem = new MenuItem("Russian");
        MenuItem japaneseItem = new MenuItem("Japanese");

        ActionListener languageListener = e -> {
            MenuItem item = (MenuItem) e.getSource();
            String lang = translateInstance.getLanguage(item.getLabel());
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
