/**
 * Created by Janusz on 2014-11-02.
 */

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long timeBegin = System.currentTimeMillis();
        File plik = new File("C:\\Users\\Janusz\\IdeaProjects\\Shape_detection\\0_images.bmp");

        BufferedImage picture = null;
        try {
            picture = ImageIO.read(plik);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int red, green, blue, grey;
        int pocz_i = -1, pocz_j = -1, i1 = 0, i2 = 0, i3 = 0, i4 = 0, j1 = 0, j2 = 0, j3 = 0, j4 = 0;
        int k1 = 0, k2 = 0, k3 = 0, k4 = 0, k_max = 0, side_avg = 0;
        int srodek_i, srodek_j;
        int width = picture.getWidth();
        int height = picture.getHeight();

        //co najmniej n bialych sasiadow
        int neighbors_white = 4;
        int neighbors_smoothed_edge = 5;
        int neighbors_filled_edges = 5;

        //ilosc powtorzen
        int amount_of_smoothing = 1;
        int amount_of_filling = 1;

        Color[][] tab_RGB = new Color[width][height];
        int[][] tab_Red = new int[width][height];
        int[][] tab_Green = new int[width][height];
        int[][] tab_Blue = new int[width][height];

        Color[][] tab_grey_scale = new Color[width][height];

        Color[][] Sobel1 = new Color[width][height];
        Color[][] Sobel2 = new Color[width][height];
        Color[][] Sobel3 = new Color[width][height];
        Color[][] Sobel4 = new Color[width][height];
        Color[][] Sobel5 = new Color[width][height];
        Color[][] Sobel6 = new Color[width][height];
        Color[][] Sobel7 = new Color[width][height];
        Color[][] Sobel8 = new Color[width][height];

        Color[][] tab_edges = new Color[width][height];
        Color[][] tab_smoothed_edges = new Color[width][height];
        Color[][] tab_filled_edges = new Color[width][height];
        Color[][] tab_beautiful_edges = new Color[width][height];
        Color[][] tab_green_edges = new Color[width][height];

        //wczytanie ze zdjecia kolorow do tab_RGB
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_RGB[i][j] = new Color(picture.getRGB(i, j));
                tab_Red[i][j] = tab_RGB[i][j].getRed();
                tab_Green[i][j] = tab_RGB[i][j].getGreen();
                tab_Blue[i][j] = tab_RGB[i][j].getBlue();
            }

/*----------------------------------------------------------------------------------------------------
POCZATEK ZABAWY ZE ZDJECIEM
----------------------------------------------------------------------------------------------------*/

        //utworzenie zdjecia w skali szarosci
        BufferedImage image_grey = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                //grey = (int) (0.3 * tab_Red[i][j] + 0.59 * tab_Green[i][j] + 0.11 * tab_Blue[i][j]);
                grey = (int) (0.33 * tab_Red[i][j] + 0.33 * tab_Green[i][j] + 0.33 * tab_Blue[i][j]);
                tab_grey_scale[i][j] = new Color(grey, grey, grey);
                image_grey.setRGB(i, j, tab_grey_scale[i][j].getRGB());
            }
        File outputfile = new File("1_image_grey.bmp");
        ImageIO.write(image_grey, "bmp", outputfile);

        for (int i = 0; i < width; i++)
            Sobel1[i][0] = Sobel2[i][0] = Sobel3[i][0] = Sobel4[i][0] = Sobel5[i][0] = Sobel6[i][0] = Sobel7[i][0] = Sobel8[i][0]
                    = Sobel1[i][height - 1] = Sobel2[i][height - 1] = Sobel3[i][height - 1] = Sobel4[i][height - 1]
                    = Sobel5[i][height - 1] = Sobel6[i][height - 1] = Sobel7[i][height - 1] = Sobel8[i][height - 1]
                    = new Color(0, 0, 0);

        for (int i = 0; i < height; i++)
            Sobel1[0][i] = Sobel2[0][i] = Sobel3[0][i] = Sobel4[0][i] = Sobel5[0][i] = Sobel6[0][i] = Sobel7[0][i] = Sobel8[0][i]
                    = Sobel1[width - 1][i] = Sobel2[width - 1][i] = Sobel3[width - 1][i] = Sobel4[width - 1][i]
                    = Sobel5[width - 1][i] = Sobel6[width - 1][i] = Sobel7[width - 1][i] = Sobel8[width - 1][i]
                    = new Color(0, 0, 0);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 1
        BufferedImage image_Sobel1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = -1 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 0 * tab_grey_scale[i - 1][j].getRed()
                        + 1 * tab_grey_scale[i - 1][j + 1].getRed()
                        - 2 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        + 2 * tab_grey_scale[i][j + 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j - 1].getRed()
                        + 0 * tab_grey_scale[i + 1][j].getRed()
                        + 1 * tab_grey_scale[i + 1][j + 1].getRed();

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel1[i][j] = new Color(red, green, blue);
                Sobel1[i][j] = (Sobel1[i][j].getRed() < 24 && Sobel1[i][j].getGreen() < 24
                        && Sobel1[i][j].getBlue() < 24) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel1.setRGB(i, j, Sobel1[i][j].getRGB());
            }
        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel1[i - 1][j - 1].getRed() + Sobel1[i - 1][j].getRed() + Sobel1[i - 1][j + 1].getRed() + Sobel1[i][j - 1].getRed() + Sobel1[i][j + 1].getRed() + Sobel1[i + 1][j - 1].getRed() + Sobel1[i + 1][j].getRed() + Sobel1[i + 1][j + 1].getRed() <= 255) {
                    Sobel1[i][j] = new Color(0, 0, 0);
                    image_Sobel1.setRGB(i, j, Sobel1[i][j].getRGB());
                }

        outputfile = new File("2_image_Sobel1.bmp");
        ImageIO.write(image_Sobel1, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 2
        BufferedImage image_Sobel2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = 0 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 1 * tab_grey_scale[i - 1][j].getRed()
                        + 2 * tab_grey_scale[i - 1][j + 1].getRed()
                        - 1 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        + 1 * tab_grey_scale[i][j + 1].getRed()
                        - 2 * tab_grey_scale[i + 1][j - 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j].getRed()
                        + 0 * tab_grey_scale[i + 1][j + 1].getRed();

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel2[i][j] = new Color(red, green, blue);
                Sobel2[i][j] = (Sobel2[i][j].getRed() < 22 && Sobel2[i][j].getGreen() < 22
                        && Sobel2[i][j].getBlue() < 22) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel2.setRGB(i, j, Sobel2[i][j].getRGB());
            }
        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel2[i - 1][j - 1].getRed() + Sobel2[i - 1][j].getRed() + Sobel2[i - 1][j + 1].getRed() + Sobel2[i][j - 1].getRed() + Sobel2[i][j + 1].getRed() + Sobel2[i + 1][j - 1].getRed() + Sobel2[i + 1][j].getRed() + Sobel2[i + 1][j + 1].getRed() <= 255) {
                    Sobel2[i][j] = new Color(0, 0, 0);
                    image_Sobel2.setRGB(i, j, Sobel2[i][j].getRGB());
                }
        outputfile = new File("3_image_Sobel2.bmp");
        ImageIO.write(image_Sobel2, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 3
        BufferedImage image_Sobel3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = 1 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 2 * tab_grey_scale[i - 1][j].getRed()
                        + 1 * tab_grey_scale[i - 1][j + 1].getRed()
                        + 0 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        + 0 * tab_grey_scale[i][j + 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j - 1].getRed()
                        - 2 * tab_grey_scale[i + 1][j].getRed()
                        - 1 * tab_grey_scale[i + 1][j + 1].getRed();

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel3[i][j] = new Color(red, green, blue);
                Sobel3[i][j] = (Sobel3[i][j].getRed() < 20 && Sobel3[i][j].getGreen() < 20
                        && Sobel3[i][j].getBlue() < 20) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel3.setRGB(i, j, Sobel3[i][j].getRGB());
            }
        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel3[i - 1][j - 1].getRed() + Sobel3[i - 1][j].getRed() + Sobel3[i - 1][j + 1].getRed() + Sobel3[i][j - 1].getRed() + Sobel3[i][j + 1].getRed() + Sobel3[i + 1][j - 1].getRed() + Sobel3[i + 1][j].getRed() + Sobel3[i + 1][j + 1].getRed() <= 255) {
                    Sobel3[i][j] = new Color(0, 0, 0);
                    image_Sobel3.setRGB(i, j, Sobel3[i][j].getRGB());
                }
        outputfile = new File("4_image_Sobel3.bmp");
        ImageIO.write(image_Sobel3, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 4
        BufferedImage image_Sobel4 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = 2 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 1 * tab_grey_scale[i - 1][j].getRed()
                        + 0 * tab_grey_scale[i - 1][j + 1].getRed()
                        + 1 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        - 1 * tab_grey_scale[i][j + 1].getRed()
                        + 0 * tab_grey_scale[i + 1][j - 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j].getRed()
                        - 2 * tab_grey_scale[i + 1][j + 1].getRed();

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel4[i][j] = new Color(red, green, blue);
                Sobel4[i][j] = (Sobel4[i][j].getRed() < 23 && Sobel4[i][j].getGreen() < 23
                        && Sobel4[i][j].getBlue() < 23) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel4.setRGB(i, j, Sobel4[i][j].getRGB());
            }

        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel4[i - 1][j - 1].getRed() + Sobel4[i - 1][j].getRed() + Sobel4[i - 1][j + 1].getRed() + Sobel4[i][j - 1].getRed() + Sobel4[i][j + 1].getRed() + Sobel4[i + 1][j - 1].getRed() + Sobel4[i + 1][j].getRed() + Sobel4[i + 1][j + 1].getRed() <= 255) {
                    Sobel4[i][j] = new Color(0, 0, 0);
                    image_Sobel4.setRGB(i, j, Sobel4[i][j].getRGB());
                }
        outputfile = new File("5_image_Sobel4.bmp");
        ImageIO.write(image_Sobel4, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 5
        BufferedImage image_Sobel5 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = -(-1 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 0 * tab_grey_scale[i - 1][j].getRed()
                        + 1 * tab_grey_scale[i - 1][j + 1].getRed()
                        - 2 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        + 2 * tab_grey_scale[i][j + 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j - 1].getRed()
                        + 0 * tab_grey_scale[i + 1][j].getRed()
                        + 1 * tab_grey_scale[i + 1][j + 1].getRed());

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel5[i][j] = new Color(red, green, blue);
                Sobel5[i][j] = (Sobel5[i][j].getRed() < 22 && Sobel5[i][j].getGreen() < 22
                        && Sobel5[i][j].getBlue() < 22) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel5.setRGB(i, j, Sobel5[i][j].getRGB());
            }
        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel5[i - 1][j - 1].getRed() + Sobel5[i - 1][j].getRed() + Sobel5[i - 1][j + 1].getRed() + Sobel5[i][j - 1].getRed() + Sobel5[i][j + 1].getRed() + Sobel5[i + 1][j - 1].getRed() + Sobel5[i + 1][j].getRed() + Sobel5[i + 1][j + 1].getRed() <= 255) {
                    Sobel5[i][j] = new Color(0, 0, 0);
                    image_Sobel5.setRGB(i, j, Sobel5[i][j].getRGB());
                }
        outputfile = new File("6_image_Sobel5.bmp");
        ImageIO.write(image_Sobel5, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 6
        BufferedImage image_Sobel6 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = -(0 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 1 * tab_grey_scale[i - 1][j].getRed()
                        + 2 * tab_grey_scale[i - 1][j + 1].getRed()
                        - 1 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        + 1 * tab_grey_scale[i][j + 1].getRed()
                        - 2 * tab_grey_scale[i + 1][j - 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j].getRed()
                        + 0 * tab_grey_scale[i + 1][j + 1].getRed());

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel6[i][j] = new Color(red, green, blue);
                Sobel6[i][j] = (Sobel6[i][j].getRed() < 21 && Sobel6[i][j].getGreen() < 21
                        && Sobel6[i][j].getBlue() < 21) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel6.setRGB(i, j, Sobel6[i][j].getRGB());
            }
        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel6[i - 1][j - 1].getRed() + Sobel6[i - 1][j].getRed() + Sobel6[i - 1][j + 1].getRed() + Sobel6[i][j - 1].getRed() + Sobel6[i][j + 1].getRed() + Sobel6[i + 1][j - 1].getRed() + Sobel6[i + 1][j].getRed() + Sobel6[i + 1][j + 1].getRed() <= 255) {
                    Sobel6[i][j] = new Color(0, 0, 0);
                    image_Sobel6.setRGB(i, j, Sobel6[i][j].getRGB());
                }
        outputfile = new File("7_image_Sobel6.bmp");
        ImageIO.write(image_Sobel6, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 7
        BufferedImage image_Sobel7 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = -(1 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 2 * tab_grey_scale[i - 1][j].getRed()
                        + 1 * tab_grey_scale[i - 1][j + 1].getRed()
                        + 0 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        + 0 * tab_grey_scale[i][j + 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j - 1].getRed()
                        - 2 * tab_grey_scale[i + 1][j].getRed()
                        - 1 * tab_grey_scale[i + 1][j + 1].getRed());

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel7[i][j] = new Color(red, green, blue);
                Sobel7[i][j] = (Sobel7[i][j].getRed() < 17 && Sobel7[i][j].getGreen() < 17
                        && Sobel7[i][j].getBlue() < 17) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel7.setRGB(i, j, Sobel7[i][j].getRGB());
            }
        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel7[i - 1][j - 1].getRed() + Sobel7[i - 1][j].getRed() + Sobel7[i - 1][j + 1].getRed() + Sobel7[i][j - 1].getRed() + Sobel7[i][j + 1].getRed() + Sobel7[i + 1][j - 1].getRed() + Sobel7[i + 1][j].getRed() + Sobel7[i + 1][j + 1].getRed() <= 255) {
                    Sobel7[i][j] = new Color(0, 0, 0);
                    image_Sobel7.setRGB(i, j, Sobel7[i][j].getRGB());
                }
        outputfile = new File("8_image_Sobel7.bmp");
        ImageIO.write(image_Sobel7, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela 8
        BufferedImage image_Sobel8 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {

                red = green = blue = -(2 * tab_grey_scale[i - 1][j - 1].getRed()
                        + 1 * tab_grey_scale[i - 1][j].getRed()
                        + 0 * tab_grey_scale[i - 1][j + 1].getRed()
                        + 1 * tab_grey_scale[i][j - 1].getRed()
                        + 0 * tab_grey_scale[i][j].getRed()
                        - 1 * tab_grey_scale[i][j + 1].getRed()
                        + 0 * tab_grey_scale[i + 1][j - 1].getRed()
                        - 1 * tab_grey_scale[i + 1][j].getRed()
                        - 2 * tab_grey_scale[i + 1][j + 1].getRed());

                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                Sobel8[i][j] = new Color(red, green, blue);
                Sobel8[i][j] = (Sobel8[i][j].getRed() < 23 && Sobel8[i][j].getGreen() < 23
                        && Sobel8[i][j].getBlue() < 23) ? new Color(0, 0, 0) : new Color(255, 255, 255);
                image_Sobel8.setRGB(i, j, Sobel8[i][j].getRGB());
            }

        //usuwanie pojedynczych bialych pikseli
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++)
                if (Sobel8[i - 1][j - 1].getRed() + Sobel8[i - 1][j].getRed() + Sobel8[i - 1][j + 1].getRed() + Sobel8[i][j - 1].getRed() + Sobel8[i][j + 1].getRed() + Sobel8[i + 1][j - 1].getRed() + Sobel8[i + 1][j].getRed() + Sobel8[i + 1][j + 1].getRed() <= 255) {
                    Sobel8[i][j] = new Color(0, 0, 0);
                    image_Sobel8.setRGB(i, j, Sobel8[i][j].getRGB());
                }

        outputfile = new File("9_image_Sobel8.bmp");
        ImageIO.write(image_Sobel8, "bmp", outputfile);

        //polaczenie Sobel1 - Sobel8
        BufferedImage image_sobel_1_8 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_edges[i][j] = (Sobel1[i][j].getBlue() == 255 || Sobel2[i][j].getBlue() == 255 || Sobel3[i][j].getBlue() == 255 || Sobel4[i][j].getBlue() == 255 || Sobel5[i][j].getBlue() == 255 || Sobel6[i][j].getBlue() == 255 || Sobel7[i][j].getBlue() == 255 || Sobel8[i][j].getBlue() == 255) ? new Color(255, 255, 255) : new Color(0, 0, 0);

                image_sobel_1_8.setRGB(i, j, tab_edges[i][j].getRGB());
            }

        outputfile = new File("10_image_sobel_1_8.bmp");
        ImageIO.write(image_sobel_1_8, "bmp", outputfile);

        //usuwanie pojedynczych bialych pikseli
        BufferedImage image_white_edges = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width - 1; i++)
            for (int j = 1; j < height - 1; j++) {
                if (tab_edges[i - 1][j - 1].getRed() + tab_edges[i - 1][j].getRed() + tab_edges[i - 1][j + 1].getRed()
                        + tab_edges[i][j - 1].getRed() + tab_edges[i][j + 1].getRed()
                        + tab_edges[i + 1][j - 1].getRed() + tab_edges[i + 1][j].getRed() + tab_edges[i + 1][j + 1].getRed()
                        < neighbors_white * 255)
                    tab_edges[i][j] = new Color(0, 0, 0);
                image_white_edges.setRGB(i, j, tab_edges[i][j].getRGB());
            }

        outputfile = new File("11_image_white_edges.bmp");
        ImageIO.write(image_white_edges, "bmp", outputfile);

        //wygladzanie krawedzi
        BufferedImage image_smoothed_edges = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tab_smoothed_edges[i][j] = tab_edges[i][j];

        for (int t = 0; t < amount_of_smoothing; t++) {
            for (int i = 1; i < width - 1; i++)
                for (int j = 1; j < height - 1; j++)
                    if (tab_edges[i - 1][j - 1].getRed() + tab_edges[i - 1][j].getRed() + tab_edges[i - 1][j + 1].getRed()
                            + tab_edges[i][j - 1].getRed() + tab_edges[i][j + 1].getRed() + tab_edges[i + 1][j - 1].getRed()
                            + tab_edges[i + 1][j].getRed() + tab_edges[i + 1][j + 1].getRed() < neighbors_smoothed_edge * 255)
                        tab_smoothed_edges[i][j] = new Color(0, 0, 0);
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++) {
                    tab_edges[i][j] = tab_smoothed_edges[i][j];
                    image_smoothed_edges.setRGB(i, j, tab_edges[i][j].getRGB());
                }
        }

        outputfile = new File("12_image_smoothed_edges.bmp");
        ImageIO.write(image_smoothed_edges, "bmp", outputfile);

        //zamalowanie czarnych dziur w krawedziach
        BufferedImage image_filled_edges = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int t = 0; t < amount_of_filling; t++)
            for (int i = 1; i < width - 1; i++)
                for (int j = 1; j < height - 1; j++) {
                    if (tab_edges[i - 1][j - 1].getRed() + tab_edges[i - 1][j].getRed() + tab_edges[i - 1][j + 1].getRed()
                            + tab_edges[i][j - 1].getRed() + tab_edges[i][j + 1].getRed() + tab_edges[i + 1][j - 1].getRed()
                            + tab_edges[i + 1][j].getRed() + tab_edges[i + 1][j + 1].getRed() < neighbors_filled_edges * 255) {
                        tab_filled_edges[i][j] = new Color(0, 0, 0);
                    } else {
                        tab_filled_edges[i][j] = new Color(255, 255, 255);
                    }
                    image_filled_edges.setRGB(i, j, tab_filled_edges[i][j].getRGB());
                }

        outputfile = new File("13_image_filled_edges.bmp");
        ImageIO.write(image_filled_edges, "bmp", outputfile);

        //praca na poczatkowym obrazku bez jakiejkolwiek obrobki - test
        /*for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tab_beautiful_edges[i][j] = tab_green_edges[i][j] = new Color(picture.getRGB(i, j));*/

/*----------------------------------------------------------------------------------------------------
KONIEC ZABAWY ZE ZDJECIEM

POCZATEK WYSZUKIWANIA KSZTALTOW
----------------------------------------------------------------------------------------------------*/

        //kopia tab_filled_edges do tab_beautiful_edges i tab_green_edges
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tab_green_edges[i][j] = tab_beautiful_edges[i][j] = tab_filled_edges[i][j];

        //malowanie czarnej ramki, zeby ksztalty nie wychodzily poza obrazek
        for (int i = 0; i < width; i++)
            tab_beautiful_edges[i][0] = tab_beautiful_edges[i][height - 1] =
                    tab_green_edges[i][0] = tab_green_edges[i][height - 1] = new Color(0, 0, 0);
        for (int i = 0; i < height; i++)
            tab_beautiful_edges[0][i] = tab_beautiful_edges[width - 1][i] =
                    tab_green_edges[0][i] = tab_green_edges[width - 1][i] = new Color(0, 0, 0);

        //szukanie wierzcholkow startowych
        for (int l = 0; l < height; l++)
            for (int k = 0; k < width; k++) {
                if (tab_beautiful_edges[k][l].getBlue() == 255) {
                    k1 = k2 = k3 = k4 = 0;
                    if (pocz_i == -1) pocz_i = k;
                    if (pocz_j == -1) pocz_j = l;
                    int i = k;
                    int j = l;

                    //prawo, dol
                    while (tab_beautiful_edges[i + 1][j].getBlue() == 255
                            || tab_beautiful_edges[i + 1][j + 1].getBlue() == 255
                            || tab_beautiful_edges[i][j + 1].getBlue() == 255) {
                        k1++;
                        if (tab_beautiful_edges[i + 1][j].getBlue() == 255) i++;
                        else {
                            if (tab_beautiful_edges[i + 1][j + 1].getBlue() == 255) {
                                i++;
                                j++;
                            } else j++;
                        }
                        tab_green_edges[i][j] = tab_beautiful_edges[i][j] = new Color(0, 255, 0);
                        i1 = i;
                        j1 = j;
                    }

                    //lewo, dol
                    while (tab_beautiful_edges[i][j + 1].getBlue() == 255
                            || tab_beautiful_edges[i - 1][j + 1].getBlue() == 255
                            || tab_beautiful_edges[i - 1][j].getBlue() == 255) {
                        k2++;
                        if (tab_beautiful_edges[i][j + 1].getBlue() == 255) j++;
                        else if (tab_beautiful_edges[i - 1][j + 1].getBlue() == 255) {
                            i--;
                            j++;
                        } else i--;
                        tab_green_edges[i][j] = tab_beautiful_edges[i][j] = new Color(0, 255, 0);
                        i2 = i;
                        j2 = j;
                    }

                    //lewo, gora
                    while (tab_beautiful_edges[i - 1][j].getBlue() == 255
                            || tab_beautiful_edges[i - 1][j - 1].getBlue() == 255
                            || tab_beautiful_edges[i][j - 1].getBlue() == 255) {
                        k3++;
                        if (tab_beautiful_edges[i - 1][j].getBlue() == 255) i--;
                        else if (tab_beautiful_edges[i - 1][j - 1].getBlue() == 255) {
                            i--;
                            j--;
                        } else j--;
                        tab_green_edges[i][j] = tab_beautiful_edges[i][j] = new Color(0, 255, 0);
                        i3 = i;
                        j3 = j;
                    }

                    //prawo, gora
                    while (tab_beautiful_edges[i][j - 1].getBlue() == 255
                            || tab_beautiful_edges[i + 1][j - 1].getBlue() == 255
                            || tab_beautiful_edges[i + 1][j].getBlue() == 255) {
                        k4++;
                        if (tab_beautiful_edges[i][j - 1].getBlue() == 255) j--;
                        else if (tab_beautiful_edges[i + 1][j - 1].getBlue() == 255) {
                            i++;
                            j--;
                        } else i++;
                        tab_green_edges[i][j] = tab_beautiful_edges[i][j] = new Color(0, 255, 0);
                        i4 = i;
                        j4 = j;
                    }

                    k_max = Math.max(Math.max(k1, k2), Math.max(k3, k4));

                    side_avg = (int) (0.25 * (Math.sqrt((pocz_i - i1) * (pocz_i - i1) + (pocz_j - j1) * (pocz_j - j1))
                            + Math.sqrt((i1 - i2) * (i1 - i2) + (j1 - j2) * (j1 - j2))
                            + Math.sqrt((i2 - i3) * (i2 - i3) + (j2 - j3) * (j2 - j3))
                            + Math.sqrt((i3 - pocz_i) * (i3 - pocz_i) + (j3 - pocz_j) * (j3 - pocz_j))));

                    //wyswietlenie wspolrzednych wyszukanych wierzcholkow - test
                    /*System.out.println("pocz i " + pocz_i);
                    System.out.println("pocz j " + pocz_j);
                    System.out.println("i1 " + i1);
                    System.out.println("j1 " + j1);
                    System.out.println("i2 " + i2);
                    System.out.println("j2 " + j2);
                    System.out.println("i3 " + i3);
                    System.out.println("j3 " + j3);
                    System.out.println("i4 " + i4);
                    System.out.println("j4 " + j4);*/

                    //sprawdzam czy wierzcholki sie nie powtarzaja
                    //nie chce wykrywac malutkich krawedzi/ksztaltow
                    if (pocz_i < i1 + 50 && pocz_i > i1 - 50 && pocz_j < j1 + 50 && pocz_j > j1 - 50) pocz_i = 0;
                    if (pocz_i < i2 + 50 && pocz_i > i2 - 50 && pocz_j < j2 + 50 && pocz_j > j2 - 50) pocz_i = 0;
                    if (pocz_i < i3 + 50 && pocz_i > i3 - 50 && pocz_j < j3 + 50 && pocz_j > j3 - 50) pocz_i = 0;
                    if (i1 < i2 + 50 && i1 > i2 - 50 && j1 < j2 + 50 && j1 > j2 - 50) i1 = 0;
                    if (i1 < i3 + 50 && i1 > i3 - 50 && j1 < j3 + 50 && j1 > j3 - 50) i1 = 0;
                    if (i2 < i3 + 50 && i2 > i3 - 50 && j2 < j3 + 50 && j2 > j3 - 50) i2 = 0;

                    if (pocz_i != 0 && i1 != 0 && i2 != 0 && i3 != 0
                            && Math.sqrt((pocz_i - i4) * (pocz_i - i4) + (pocz_j - j4) * (pocz_j - j4)) < 20
                            && Math.abs(i1 - pocz_i) > 0.9 * Math.abs(i2 - i3)
                            && Math.abs(i1 - pocz_i) < 1.3 * Math.abs(i2 - i3)
                            && Math.abs(pocz_j - j3) > 0.9 * Math.abs(j1 - j2)
                            && Math.abs(pocz_j - j3) < 1.3 * Math.abs(j1 - j2)
                            && i1 != 0 && i2 != 0 && i3 != 0 && i4 != 0
                            && j1 != 0 && j2 != 0 && j3 != 0 && j4 != 0) { //czworokat
                        if (Math.sqrt((i1 - i2) * (i1 - i2) + (j1 - j2) * (j1 - j2)) >
                                0.95 * Math.sqrt((i3 - i2) * (i3 - i2) + (j3 - j2) * (j3 - j2))
                                && Math.sqrt((i1 - i2) * (i1 - i2) + (j1 - j2) * (j1 - j2)) <
                                1.05 * Math.sqrt((i3 - i2) * (i3 - i2) + (j3 - j2) * (j3 - j2))) {

                            if (k_max > 1.03 * side_avg) {
                                System.out.println("\nZnalazlem kolo ");
                                srodek_i = (int) (0.25 * (pocz_i + i1 + i2 + i3));
                                srodek_j = (int) (0.25 * (pocz_j + j1 + j2 + j3));
                                System.out.println("Srodek: [" + srodek_i + "; " + srodek_j + "]");
                                System.out.println("Promien: " + (int) (0.25 * (Math.sqrt((srodek_i - pocz_i) * (srodek_i - pocz_i) + (srodek_j - pocz_j) * (srodek_j - pocz_j))
                                        + Math.sqrt((srodek_i - i1) * (srodek_i - i1) + (srodek_j - j1) * (srodek_j - j1))
                                        + Math.sqrt((srodek_i - i2) * (srodek_i - i2) + (srodek_j - j2) * (srodek_j - j2))
                                        + Math.sqrt((srodek_i - i3) * (srodek_i - i3) + (srodek_j - j3) * (srodek_j - j3)))));
                            } else {
                                System.out.println("\nZnalazlem kwadrat, jego wierzcholki to: ");
                                System.out.println("[" + pocz_i + "; " + pocz_j + "]");
                                System.out.println("[" + i1 + "; " + j1 + "]");
                                System.out.println("[" + i2 + "; " + j2 + "]");
                                System.out.println("[" + i3 + "; " + j3 + "]");
                            }
                        } else {
                            if (k_max > 1.03 * side_avg) {
                                System.out.println("\nZnalazlem elipse ");
                                srodek_i = (int) (0.25 * (pocz_i + i1 + i2 + i3));
                                srodek_j = (int) (0.25 * (pocz_j + j1 + j2 + j3));
                                System.out.println("Srodek: [" + srodek_i + "; " + srodek_j + "]");
                                System.out.println("Promien pierwszy: " + (int) (0.5 * (Math.sqrt((srodek_i - pocz_i) * (srodek_i - pocz_i) + (srodek_j - pocz_j) * (srodek_j - pocz_j))
                                        + Math.sqrt((srodek_i - i2) * (srodek_i - i2) + (srodek_j - j2) * (srodek_j - j2)))));
                                System.out.println("Promien drugi: " + (int) (0.5 * (Math.sqrt((srodek_i - i1) * (srodek_i - i1) + (srodek_j - j1) * (srodek_j - j1))
                                        + Math.sqrt((srodek_i - i3) * (srodek_i - i3) + (srodek_j - j3) * (srodek_j - j3)))));
                            } else {
                                System.out.println("\nZnalazlem prostokat, jego wierzcholki to: ");
                                System.out.println("[" + pocz_i + "; " + pocz_j + "]");
                                System.out.println("[" + i1 + "; " + j1 + "]");
                                System.out.println("[" + i2 + "; " + j2 + "]");
                                System.out.println("[" + i3 + "; " + j3 + "]");
                            }
                        }

                        //niszczenie znalezionego ksztaltu
                        for (int a = 1; a < width - 2; a++)
                            for (int b = 1; b < height - 2; b++)
                                if (a > i3 && a < i1 && b > pocz_j && b < j2) {
                                    tab_beautiful_edges[a][b] = new Color(0, 255, 0);
                                    tab_beautiful_edges[a + 1][b] = new Color(0, 255, 0);
                                    tab_beautiful_edges[a][b + 1] = new Color(0, 255, 0);
                                    tab_beautiful_edges[a + 1][b + 1] = new Color(0, 255, 0);
                                }
                        i1 = i2 = i3 = i4 = j1 = j2 = j3 = j4 = 0;
                    }

                    if (((pocz_i != 0 && i1 != 0 && i2 != 0 && i3 == 0)
                            || (pocz_i != 0 && i1 != 0 && i3 != 0 && i2 == 0)
                            || (pocz_i != 0 && i2 != 0 && i3 != 0 && i1 == 0))
                            && (pocz_i > 0.9 * i4 && pocz_i < 1.1 * i4 && pocz_j > 0.9 * j4 && pocz_j < 1.1 * j4)
                            && Math.sqrt((pocz_i - i1) * (pocz_i - i1) + (pocz_j - j1) * (pocz_j - j1)) > 30
                            && Math.sqrt((pocz_i - i2) * (pocz_i - i2) + (pocz_j - j2) * (pocz_j - j2)) > 30
                            && Math.sqrt((pocz_i - i3) * (pocz_i - i3) + (pocz_j - j3) * (pocz_j - j3)) > 30
                            && Math.sqrt((i1 - i2) * (i1 - i2) + (j1 - j2) * (j1 - j2)) > 30
                            && Math.sqrt((i1 - i3) * (i1 - i3) + (j1 - j3) * (j1 - j3)) > 30
                            && Math.sqrt((i1 - i4) * (i1 - i4) + (j1 - j4) * (j1 - j4)) > 30
                            && Math.sqrt((i2 - i3) * (i2 - i3) + (j2 - j3) * (j2 - j3)) > 30
                            && Math.sqrt((i2 - i4) * (i2 - i4) + (j2 - j4) * (j2 - j4)) > 30
                            && Math.sqrt((i3 - i4) * (i3 - i4) + (j3 - j4) * (j3 - j4)) > 30) {
                        if (i1 == 0 || j1 == 0) {
                            if ((pocz_i < (int) (0.33 * (pocz_i + i2 + i3) - 5) || (pocz_i > (int) (0.33 * (pocz_i + i2 + i3) + 5)))
                                    && ((i2 < (int) (0.33 * (pocz_i + i2 + i3) - 5)) || (i2 > (int) (0.33 * (pocz_i + i2 + i3) + 5)))
                                    && ((i3 < (int) (0.33 * (pocz_i + i2 + i3) - 5)) || (i3 > (int) (0.33 * (pocz_i + i2 + i3) + 5)))
                                    && ((pocz_j < (int) (0.33 * (pocz_j + j2 + j3) - 5)) || (pocz_j > (int) (0.33 * (pocz_j + j2 + j3) + 5)))
                                    && ((j2 < (int) (0.33 * (pocz_j + j2 + j3) - 5)) || (j2 > (int) (0.33 * (pocz_j + j2 + j3) + 5)))
                                    && ((j3 < (int) (0.33 * (pocz_j + j2 + j3) - 5)) || (j3 > (int) (0.33 * (pocz_j + j2 + j3) + 5)))) {
                                System.out.println("\nZnalazlem trojkat, jego wierzcholki to: ");
                                System.out.println("[" + pocz_i + "; " + pocz_j + "]");
                                System.out.println("[" + i2 + "; " + j2 + "]");
                                System.out.println("[" + i3 + "; " + j3 + "]");
                            }
                        } else if (i2 == 0 || j2 == 0) {
                            if ((pocz_i < (int) (0.33 * (pocz_i + i1 + i3) - 5) || (pocz_i > (int) (0.33 * (pocz_i + i1 + i3) - 5)))
                                    && ((i1 < (int) (0.33 * (pocz_i + i1 + i3) - 5)) || (i1 > (int) (0.33 * (pocz_i + i1 + i3) + 5)))
                                    && ((i3 < (int) (0.33 * (pocz_i + i1 + i3) - 5)) || (i3 > (int) (0.33 * (pocz_i + i1 + i3) + 5)))
                                    && ((pocz_j < (int) (0.33 * (pocz_j + j1 + j3) - 5)) || (pocz_j > (int) (0.33 * (pocz_j + j1 + j3) + 5)))
                                    && ((j1 < (int) (0.33 * (pocz_j + j1 + j3) - 5)) || (j1 > (int) (0.33 * (pocz_j + j1 + j3) + 5)))
                                    && ((j3 < (int) (0.33 * (pocz_j + j1 + j3) - 5)) || (j3 > (int) (0.33 * (pocz_j + j1 + j3) + 5)))
                                    ) {
                                System.out.println("\nZnalazlem trojkat, jego wierzcholki to: ");
                                System.out.println("[" + pocz_i + "; " + pocz_j + "]");
                                System.out.println("[" + i1 + "; " + j1 + "]");
                                System.out.println("[" + i3 + "; " + j3 + "]");
                            }
                        } else if (i3 == 0 || j3 == 0) {
                            if ((pocz_i < (int) (0.33 * (pocz_i + i1 + i2) - 5) || (pocz_i > (int) (0.33 * (pocz_i + i1 + i2) + 5)))
                                    && ((i1 < (int) (0.33 * (pocz_i + i1 + i2) - 5)) || (i1 > (int) (0.33 * (pocz_i + i1 + i2) + 5)))
                                    && ((i2 < (int) (0.33 * (pocz_i + i1 + i2) - 5)) || (i2 > (int) (0.33 * (pocz_i + i1 + i2) + 5)))
                                    && ((pocz_j < (int) (0.33 * (pocz_j + j1 + j2) - 5)) || (pocz_j > (int) (0.33 * (pocz_j + j1 + j2) + 5)))
                                    && ((j1 < (int) (0.33 * (pocz_j + j1 + j2) - 5)) || (j1 > (int) (0.33 * (pocz_j + j1 + j2) + 5)))
                                    && ((j2 < (int) (0.33 * (pocz_j + j1 + j2) - 5)) || (j2 > (int) (0.33 * (pocz_j + j1 + j2) + 5)))) {
                                System.out.println("\nZnalazlem trojkat, jego wierzcholki to: ");
                                System.out.println("[" + pocz_i + "; " + pocz_j + "]");
                                System.out.println("[" + i1 + "; " + j1 + "]");
                                System.out.println("[" + i2 + "; " + j2 + "]");
                            }
                        }

                        //niszczenie znalezionego ksztaltu
                        for (int a = 1; a < width - 2; a++)
                            for (int b = 1; b < height - 2; b++)
                                if (a > i3 && a < i1 && b > pocz_j && b < j2)
                                    tab_beautiful_edges[a][b] = new Color(0, 255, 0);
                        i1 = i2 = i3 = i4 = j1 = j2 = j3 = j4 = 0;
                    }
                }
                pocz_i = -1;    //wierzcholek poczatkowy ponownie ustawiam na [-1, -1]
                pocz_j = -1;    //zebym mogl szukac kolejnej figury
            }

/*----------------------------------------------------------------------------------------------------
KONIEC WYSZUKIWANIA KSZTALTOW
----------------------------------------------------------------------------------------------------*/

        //przejscie po obwodzie - test
        BufferedImage image_circuit_green = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) image_circuit_green.setRGB(i, j, tab_green_edges[i][j].getRGB());
        outputfile = new File("14_image_circuit_green.bmp");
        ImageIO.write(image_circuit_green, "bmp", outputfile);

        //zamalowanie ksztaltow - test
        BufferedImage image_destroyed_shapes = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) image_destroyed_shapes.setRGB(i, j, tab_beautiful_edges[i][j].getRGB());
        outputfile = new File("15_destroyed_shapes.bmp");
        ImageIO.write(image_destroyed_shapes, "bmp", outputfile);

        //wyswietlenie czasu potrzebnego do wykonania programu
        long timeEnd = System.currentTimeMillis();
        System.out.println("\nCzas wykonywania programu " + 0.001 * (timeEnd - timeBegin) + "[s]");
    }
}