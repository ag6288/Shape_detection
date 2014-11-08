import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Janusz on 2014-11-02.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        int red, green, blue;
        double r;
        File plik = new File("C:\\Users\\Janusz\\IdeaProjects\\Shape_detection\\0_image.bmp");
        BufferedImage picture = null;
        try {
            picture = ImageIO.read(plik);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = picture.getWidth();
        int height = picture.getHeight();
        Color[][] tab_RGB = new Color[width][height];
        Color[][] tab_grey_scale = new Color[width][height];
        Color[][] tab_edge_X = new Color[width][height];
        Color[][] tab_edge_Y = new Color[width][height];
        Color[][] tab_edge_white = new Color[width][height];
        Color[][] tab_edge_white_X = new Color[width][height];
        Color[][] tab_edge_white_Y = new Color[width][height];
        int[][] tab_Red = new int[width][height];
        int[][] tab_Green = new int[width][height];
        int[][] tab_Blue = new int[width][height];
        int[][] tab_Hough = new int [180][(int)Math.sqrt(width*width+height*height)];

        //wczytanie ze zdjecia kolorow do tab_RGB
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_RGB[i][j] = new Color(picture.getRGB(i, j));
                tab_Red[i][j] = tab_RGB[i][j].getRed();
                tab_Green[i][j] = tab_RGB[i][j].getGreen();
                tab_Blue[i][j] = tab_RGB[i][j].getBlue();
            }

        //utworzenie zdjecia w skali szarosci
        BufferedImage image_grey = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_grey_scale[i][j] = new Color((int) (0.3 * tab_Red[i][j]), (int) (0.59 * tab_Green[i][j]), (int) (0.11 * tab_Blue[i][j]));
                image_grey.setRGB(i, j, tab_grey_scale[i][j].getRGB());
            }
        File outputfile = new File("1_image_grey.bmp");
        ImageIO.write(image_grey, "bmp", outputfile);

        for (int i = 0; i < width; i++)
            tab_edge_X[i][0] = tab_edge_Y[i][0] = tab_edge_X[i][height-1] = tab_edge_Y[i][height-1] = new Color (0, 0, 0);

        for (int i = 0; i < height; i++)
            tab_edge_X[0][i] = tab_edge_Y[0][i] = tab_edge_X[width-1][i] = tab_edge_Y[width-1][i] = new Color (0, 0, 0);

        //utworzenie zdjecia po zastosowaniu operatora Sobela X
        BufferedImage image_edge_X = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width-1; i++)
            for (int j = 1; j < height-1; j++) {

                red = tab_grey_scale[i-1][j-1].getRed()
                        -tab_grey_scale[i-1][j+1].getRed()
                        +2*tab_grey_scale[i-1][j].getRed()
                        -2*tab_grey_scale[i+1][j].getRed()
                        +tab_grey_scale[i-1][j-1].getRed()
                        -tab_grey_scale[i+1][j+1].getRed();

                green = tab_grey_scale[i-1][j-1].getGreen()
                        -tab_grey_scale[i-1][j+1].getGreen()
                        +2*tab_grey_scale[i-1][j].getGreen()
                        -2*tab_grey_scale[i+1][j].getGreen()
                        +tab_grey_scale[i-1][j-1].getGreen()
                        -tab_grey_scale[i+1][j+1].getGreen();

                blue = tab_grey_scale[i-1][j-1].getBlue()
                        -tab_grey_scale[i-1][j+1].getBlue()
                        +2*tab_grey_scale[i-1][j].getBlue()
                        -2*tab_grey_scale[i+1][j].getBlue()
                        +tab_grey_scale[i-1][j-1].getBlue()
                        -tab_grey_scale[i+1][j+1].getBlue();

                if (red<0) red = 0;
                if (red>255) red = 255;
                if (green<0) green = 0;
                if (green>255) green = 255;
                if (blue<0) blue = 0;
                if (blue>255) blue = 255;

                tab_edge_X[i][j] = new Color(red, green, blue);
                image_edge_X.setRGB(i, j, tab_edge_X[i][j].getRGB());
            }
        outputfile = new File("2_image_edge_X.bmp");
        ImageIO.write(image_edge_X, "bmp", outputfile);

        //utworzenie zdjecia po zastosowaniu operatora Sobela Y
        BufferedImage image_edge_Y = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < width-1; i++)
            for (int j = 1; j < height-1; j++) {

                red = tab_grey_scale[i-1][j-1].getRed()
                        +2*tab_grey_scale[i][j-1].getRed()
                        +tab_grey_scale[i+1][j-1].getRed()
                        -tab_grey_scale[i-1][j+1].getRed()
                        -2*tab_grey_scale[i][j+1].getRed()
                        -tab_grey_scale[i+1][j+1].getRed();

                green = tab_grey_scale[i-1][j-1].getGreen()
                        +2*tab_grey_scale[i][j-1].getGreen()
                        +tab_grey_scale[i+1][j-1].getGreen()
                        -tab_grey_scale[i-1][j+1].getGreen()
                        -2*tab_grey_scale[i][j+1].getGreen()
                        -tab_grey_scale[i+1][j+1].getGreen();

                blue = tab_grey_scale[i-1][j-1].getBlue()
                        +2*tab_grey_scale[i][j-1].getBlue()
                        +tab_grey_scale[i+1][j-1].getBlue()
                        -tab_grey_scale[i-1][j+1].getBlue()
                        -2*tab_grey_scale[i][j+1].getBlue()
                        -tab_grey_scale[i+1][j+1].getBlue();

                if (red<0) red = 0;
                if (red>255) red = 255;
                if (green<0) green = 0;
                if (green>255) green = 255;
                if (blue<0) blue = 0;
                if (blue>255) blue = 255;

                tab_edge_Y[i][j] = new Color(red, green, blue);
                image_edge_Y.setRGB(i, j, tab_edge_Y[i][j].getRGB());
            }
        outputfile = new File("3_image_edge_Y.bmp");
        ImageIO.write(image_edge_Y, "bmp", outputfile);

        //wybielanie krawedzi po X
        BufferedImage image_white_edge_X = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_edge_white_X[i][j] = (tab_edge_X[i][j].getRed()<100 && tab_edge_X[i][j].getGreen()<100 && tab_edge_X[i][j].getBlue()<100) ? new Color(0, 0, 0) : new Color (255, 255, 255);
                image_white_edge_X.setRGB(i, j, tab_edge_white_X[i][j].getRGB());
            }
        outputfile = new File("4_image_white_edge_X.bmp");
        ImageIO.write(image_white_edge_X, "bmp", outputfile);

        //wybielanie krawedzi po Y
        BufferedImage image_white_edge_Y = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_edge_white_Y[i][j] = (tab_edge_Y[i][j].getRed()<100 && tab_edge_Y[i][j].getGreen()<100 && tab_edge_Y[i][j].getBlue()<100) ? new Color(0, 0, 0) : new Color (255, 255, 255);
                image_white_edge_Y.setRGB(i, j, tab_edge_white_Y[i][j].getRGB());
            }
        outputfile = new File("5_image_white_edge_Y.bmp");
        ImageIO.write(image_white_edge_Y, "bmp", outputfile);

        //polaczenie image_white_edge_X z image_white_edge_Y
        BufferedImage image_white_edge = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                tab_edge_white[i][j] = (tab_edge_white_X[i][j].getBlue() == 255 || tab_edge_white_Y[i][j].getBlue() == 255) ? new Color(255, 255, 255) : new Color (0, 0, 0);
                image_white_edge.setRGB(i, j, tab_edge_white[i][j].getRGB());
            }
        outputfile = new File("6_image_white_edge.bmp");
        ImageIO.write(image_white_edge, "bmp", outputfile);

        int pocz_i = -1, pocz_j = -1, i1 = 0, i2 = 0, i3 = 0, i4 = 0, j1 = 0, j2 = 0, j3 = 0, j4 = 0;
        boolean flaga_obwod = true;

        for (int l = 0; l<height; l++)
            for (int k = 0; k<width; k++) {
                if (tab_edge_white[k][l].getBlue()==255 && flaga_obwod) {
                    if (pocz_i==-1) pocz_i = k;
                    if (pocz_j==-1) pocz_j = l;
                    int i = k;
                    int j = l;
                        while (tab_edge_white[i + 1][j].getBlue() == 255
                                || tab_edge_white[i + 1][j + 1].getBlue() == 255
                                || tab_edge_white[i][j + 1].getBlue() == 255) {
                            if (tab_edge_white[i + 1][j].getBlue() == 255) i++;
                            else {
                                if (tab_edge_white[i + 1][j + 1].getBlue() == 255) {
                                    i++;
                                    j++;
                                } else i++;
                            }
                            tab_edge_white[i][j] = new Color(0, 255, 0);
                            i1 = i;
                            j1 = j;
                        }

                        //lewo, dol
                        while (tab_edge_white[i][j + 1].getBlue() == 255
                                || tab_edge_white[i - 1][j + 1].getBlue() == 255
                                || tab_edge_white[i - 1][j].getBlue() == 255) {
                            if (tab_edge_white[i][j + 1].getBlue() == 255) j++;
                            else if (tab_edge_white[i - 1][j + 1].getBlue() == 255) {
                                i--;
                                j++;
                            } else i--;
                            tab_edge_white[i][j] = new Color(0, 255, 0);
                            i2 = i;
                            j2 = j;
                        }

                        //lewo, gora
                        while (tab_edge_white[i - 1][j].getBlue() == 255
                                || tab_edge_white[i - 1][j - 1].getBlue() == 255
                                || tab_edge_white[i][j - 1].getBlue() == 255) {
                            if (tab_edge_white[i - 1][j].getBlue() == 255) i--;
                            else if (tab_edge_white[i - 1][j - 1].getBlue() == 255) {
                                i--;
                                j--;
                            } else j--;
                            tab_edge_white[i][j] = new Color(0, 255, 0);
                            i3 = i;
                            j3 = j;
                        }

                        //prawo, gora
                        while (tab_edge_white[i][j - 1].getBlue() == 255
                                || tab_edge_white[i + 1][j - 1].getBlue() == 255
                                || tab_edge_white[i + 1][j].getBlue() == 255) {
                            if (tab_edge_white[i][j - 1].getBlue() == 255) j--;
                            else if (tab_edge_white[i + 1][j - 1].getBlue() == 255) {
                                i++;
                                j--;
                            } else i++;
                            tab_edge_white[i][j] = new Color(0, 255, 0);
                            i4 = i;
                            j4 = j;
                        }
                    System.out.println("pocz i " + pocz_i);
                    System.out.println("pocz j " + pocz_j);
                    System.out.println("i1 " + i1);
                    System.out.println("j1 " + j1);
                    System.out.println("i2 " + i2);
                    System.out.println("j2 " + j2);
                    System.out.println("i3 " + i3);
                    System.out.println("j3 " + j3);
                    System.out.println("i4 " + i4);
                    System.out.println("j4 " + j4);
                    flaga_obwod = false;

                    if (pocz_i > 0.9*i4 && pocz_i < 1.1*i4 && pocz_j > 0.9*j4 && pocz_j < 1.1*j4) {
                        if (i1 != 0 && i2 != 0 && i3 != 0 && i4 != 0 && j1 != 0 && j2 != 0 && j3 != 0 && j4 != 0) { //czworokat
                            if (Math.sqrt((i1 - i2) * (i1 - i2) + (j1 - j2) * (j1 - j2)) > 0.95 * Math.sqrt((i3 - i2) * (i3 - i2) + (j3 - j2) * (j3 - j2)) && Math.sqrt((i1 - i2) * (i1 - i2) + (j1 - j2) * (j1 - j2)) < 1.05 * Math.sqrt((i3 - i2) * (i3 - i2) + (j3 - j2) * (j3 - j2)))
                                System.out.println("Znalazlem kwadrat, jego wierzcholki to: ");
                            else System.out.println("Znalazlem prostokat, jego wierzcholki to: ");
                            System.out.println("[" + pocz_i + "; " + pocz_j + "]");
                            System.out.println("[" + i1 + "; " + j1 + "]");
                            System.out.println("[" + i2 + "; " + j2 + "]");
                            System.out.println("[" + i3 + "; " + j3 + "]");
                        } else {
                            System.out.println("Znalazlem trojkat, jego wierzcholki to: ");
                            System.out.println("[" + pocz_i + "; " + pocz_j + "]");
                            if (i1 != 0 && j1 != 0) System.out.println("[" + i1 + "; " + j1 + "]");
                            if (i2 != 0 && j2 != 0) System.out.println("[" + i2 + "; " + j2 + "]");
                            if (i3 != 0 && j3 != 0) System.out.println("[" + i3 + "; " + j3 + "]");
                        }

                        //niszczenie znalezionego ksztaltu
                        for (int a = 1; a<width-2; a++)
                            for (int b = 1; b<height-2; b++)
                                if (a>i3 && a<i1 && b > pocz_j && b<j2)
                                    tab_edge_white[a][b] = new Color(0, 255, 0);
                    }

                }
            }

        //przejscie po obwodzie - test
        BufferedImage image_circuit = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) image_circuit.setRGB(i, j, tab_edge_white[i][j].getRGB());
        outputfile = new File("7_image_circuit.bmp");
        ImageIO.write(image_circuit, "bmp", outputfile);
    }
}