import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class TextFromPic {
    boolean isImproveNeeded;

    public TextFromPic(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        isImproveNeeded = false;
    }

    public File improveImage(Mat image) throws IOException {
        System.out.println("Начинается обработка изображения.");
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //Mat original = Imgcodecs.imread(filePath);
        Mat grey = new Mat();
        Mat blur = new Mat();
        Mat unsharp = new Mat();
        Mat binary = new Mat();

        Imgproc.cvtColor(image, grey, Imgproc.COLOR_RGB2GRAY, 0);
        Imgproc.GaussianBlur(grey, blur, new Size(0, 0), 3);
        Core.addWeighted(grey, 1.5, blur, -0.5, 0, unsharp);
        Imgproc.threshold(unsharp, binary, 127, 255, Imgproc.THRESH_BINARY);

        System.out.println("Обработка изображения окончена.");

        MatOfInt params = new MatOfInt(Imgcodecs.CV_IMWRITE_PNG_COMPRESSION);

        File ocrImage = new File("D:\\ocrImage.png");
        Imgcodecs.imwrite(ocrImage.getAbsolutePath(), binary, params);
        return ocrImage;
    }

    public static Mat bufferedImage2Mat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()),
                Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

    public String recognizeText(String language, BufferedImage image) throws TesseractException,
            IOException, URISyntaxException {
        ITesseract tesseract = new Tesseract();
        URL resource = TextFromPic.class.getResource("ocrTrainedData");
        String dataPath = Paths.get(resource.toURI()).toFile().getPath();
        tesseract.setDatapath(dataPath);
        tesseract.setLanguage(language);
        tesseract.setTessVariable("user_defined_dpi", "300");
        //tesseract.setPageSegMode(1);
        //tesseract.setOcrEngineMode(1);
        if(isImproveNeeded) {
            System.out.println("Улучшение изображения выполняется.");
            File file = improveImage(bufferedImage2Mat(image));
            image = ImageIO.read(file);
        }
        return tesseract.doOCR(image);
    }

    public boolean isImproveNeeded() {
        return isImproveNeeded;
    }

    public void setImproveNeeded(boolean improveNeeded) {
        isImproveNeeded = improveNeeded;
    }
}
