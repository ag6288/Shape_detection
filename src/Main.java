import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Janusz on 2014-11-02.
 */
public class Main {
    public static void main(String[] args) {
        File plik = new File("C:\\Users\\Janusz\\IdeaProjects\\Shape_detection\\square.bmp");
        BufferedImage picture = null;
        try {
            picture = ImageIO.read(plik);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = picture.getWidth();
        System.out.println("szerokosc " + width);
        int height = picture.getHeight();
        Color[][] tab_RGB = new Color[width][height];
        int[][] tab_Red = new int[width][height];
        int[][] tab_Green = new int[width][height];
        int[][] tab_Blue = new int[width][height];
        System.out.println("wysokosc " + height);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_RGB[i][j] = new Color(picture.getRGB(i, j));
                tab_Red[i][j] = tab_RGB[i][j].getRed();
                tab_Green[i][j] = tab_RGB[i][j].getGreen();
                tab_Blue[i][j] = tab_RGB[i][j].getBlue();
            }
    }
}