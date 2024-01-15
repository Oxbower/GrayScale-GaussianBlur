import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Coverts given image to equivalent grayscale
 * using BufferedImage class and adds convolution to it
 *
 * @author : Mark Santiago
 * */
public class GaussianBlur
{
    public static void main(String[] args) throws Exception {
        BufferedImage image = null;

        try
        {
            for (String arg : args)
            {
                image = ImageIO.read(new File(arg));
            }
        } catch (IOException e)
        {
            //End program if input is not valid
            throw new IOException("--Input is invalid! Check the path being passed in the arguments.");
        }

        //2D array to hold grayscale converted RGB
        int[][] imageArray = new int[image.getTileHeight()][image.getTileWidth()];
        //System.out.println("Height: " + image.getTileHeight() + " Width: " + image.getTileWidth());

        /*
         * Loop through the image and convert each pixel to grayscale equivalent
         * using the formula => Y = (.2989 * R) + (.587 * G) + (.114 * B)
         * */
        for (int row = 0; row < image.getTileHeight(); row++) //Prints row
        {
            for (int col = 0; col < image.getTileWidth(); col++) //Prints col
            {
                //Convert to grayscale!!
                Color convert = new Color(image.getRGB(col, row));

                //Grayscale conversion formula
                // Y = .2989 * R + .587 * G + .114 * B
                int getRed = (int)((convert.getRed())*(.2989));
                int getGreen = (int)((convert.getGreen())*(.587));
                int getBlue = (int)((convert.getBlue())*(.114));

                int newRGB = getRed + getGreen + getBlue;

                Color newColor = new Color(newRGB, newRGB, newRGB); //Convert back to RGB
                //int grayScale = newColor.getRGB(); //Usable RGB

                imageArray[row][col] = newRGB;

                //image.setRGB(col, row, grayScale); //Set new Color!
                //if (row == 10 && col == 6) System.out.println(newRGB);
            }
        }
        //Store the returned 2D array inside another 2D array
        int[][] blurredImage = gaussianBlur(imageArray);
        for (int row = 0; row < blurredImage.length; row++) //Prints row
        {
            for (int col = 0; col < blurredImage[0].length; col++) //Prints col
            {
                int indexColor = blurredImage[row][col];
                Color newColor = new Color(indexColor, indexColor, indexColor); //Convert back to RGB
                int grayScale = newColor.getRGB(); //Usable RGB
//                Color black = Color.black;
//                if (row == 5) { image.setRGB(col, row, black.getRGB()); } else { image.setRGB(col, row, grayScale); } //Set new Color!
                image.setRGB(col, row, grayScale);
            }
        }

        for (int row = 0; row < blurredImage.length; row++)
        {
            if (row == 0) System.out.print("[");
            System.out.print(blurredImage[row][4]);
            if (!(row == blurredImage.length - 1)) System.out.print(", ");
            if (row == blurredImage.length - 1) System.out.print("]");
        }

        renderImage(image);
    }

    public static void writeImage(int[][] imageArray) {
        String path = "./lighthouseblur.jpeg";
        BufferedImage image = new BufferedImage(imageArray[0].length, imageArray.length, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < imageArray.length; row++) {
            for (int col = 0; col < imageArray[row].length; col++) {
                Color newColor = new Color(imageArray[row][col], imageArray[row][col], imageArray[row][col]); //Convert back to RGB
                int grayScale = newColor.getRGB(); //Usable RGB
                image.setRGB(col, row, grayScale);
            }
        }

        File ImageFile = new File(path);
        try {
            ImageIO.write(image, "jpeg", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Check if row exists in the image
     * */
    public static boolean checkRow (int row, int maxRow)
    {
        if (row >= 0 && row < maxRow)
        {
            return true;
        }
        return false;
    }

    /*
     * Check if col exists in the image
     * */
    public static boolean checkCol (int col, int maxCol)
    {
        if (col >= 0 && col < maxCol)
        {
            return true;
        }
        return false;
    }

    /*
     * Checks for out-of-bounds pixels
     * */
    public static int whatValue (int row, int col, int[][] grayScaleImage)
    {
        if (checkRow(row,grayScaleImage.length)
                && checkCol(col,grayScaleImage[0].length))
        {
            //Returns pixel if it exists in the picture
            return grayScaleImage[row][col];
        }
        return 0;
    }

    public static int[][] gaussianBlur (int[][] grayScaleImage) //Pass-by ref of 2D array
    {
        int maxRow = grayScaleImage.length;
        int maxCol = grayScaleImage[0].length;
        //2D array to hold blurred image
        int[][] blurredImageArray = new int[maxRow][maxCol];

        for (int row = 0; row < maxRow; row++)
        {
            for (int col = 0; col < maxCol; col++)
            {
                //Rounding error again
                blurredImageArray[row][col] = (
                        (whatValue(row - 1, col - 1, grayScaleImage)) +
                                (whatValue(row - 1, col, grayScaleImage) * 2) +
                                (whatValue(row - 1, col + 1, grayScaleImage)) +

                                (whatValue(row, col - 1, grayScaleImage) * 2) +
                                ((grayScaleImage[row][col]) * 4) +
                                (whatValue(row, col + 1, grayScaleImage) * 2) +

                                (whatValue(row + 1, col - 1, grayScaleImage)) +
                                (whatValue(row + 1, col, grayScaleImage) * 2) +
                                (whatValue(row + 1, col + 1, grayScaleImage))
                ) / 16;
            }
        }
        return blurredImageArray;
    }

    public static void renderImage(BufferedImage image)
    {
        JFrame frame;
        JLabel label;
        frame=new JFrame();
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        label=new JLabel();
        label.setIcon(new ImageIcon(image));

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}