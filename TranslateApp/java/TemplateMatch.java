import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TemplateMatch {
    private final AreaScreenshot areaScreenshot;

    public TemplateMatch() throws AWTException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        areaScreenshot = new AreaScreenshot();
    }

    public Point matchImages(BufferedImage imgToMatch) throws IOException {
        Mat source;
        Mat template;
        String filePath = "D:\\";
        source = bufferedImageToMat(areaScreenshot.screenshotFullSize());
        //буферная картинка из map (for each)
        template = bufferedImageToMat(areaScreenshot.screenshot(new java.awt.Point(40, 50),
                new Dimension(100, 100))
        );

        Mat outputImage = new Mat();
        int machMethod = Imgproc.TM_CCOEFF;
        Imgproc.matchTemplate(source, template, outputImage, machMethod);

        Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc = mmr.maxLoc;
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + template.cols(),
                matchLoc.y + template.rows()), new Scalar(255, 0, 0));
        Imgcodecs.imwrite(filePath + "sonuc.jpg", source);
        System.out.println("Completed.");
        return matchLoc;
    }

    public static Mat bufferedImageToMat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()),
                Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

}
