import java.awt.*;
import java.awt.image.BufferedImage;

public class AreaScreenshot {

    public BufferedImage screenshot(Point point, Dimension dimension) throws AWTException {
        Robot robot = new Robot();
        System.out.println(point.x);
        Rectangle captureRect = new Rectangle(point.x, point.y, dimension.width, dimension.height);
        return robot.createScreenCapture(captureRect);
    }
}
