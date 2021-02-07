import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AreaScreenshot {
    private Map<BufferedImage, String> screenshotMap;
    private final Robot robot;

    public AreaScreenshot() throws AWTException {
        screenshotMap = new HashMap<>();
        robot = new Robot();
    }

    public BufferedImage screenshot(Point point, Dimension dimension) throws IOException {
        System.out.println(point.x);
        Rectangle captureRect = new Rectangle(point.x, point.y,
                dimension.width, dimension.height);
        BufferedImage screenshot = robot.createScreenCapture(captureRect);
        //screenshotMap.put(screenshot, "screenshotName");
        ImageIO.write(screenshot, "png", new File("D:\\area.png"));
        return screenshot;
    }

    public BufferedImage screenshotFullSize() {
        return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize()));
    }

    public Map<BufferedImage, String> getScreenshotMap() {
        return screenshotMap;
    }
}
