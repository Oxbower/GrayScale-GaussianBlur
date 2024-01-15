import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Coverts given image to equivalent grayscale
 * using BufferedImage class
 *
 * @author : Mark Santiago
 * */
public class GrayScaleConvert
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

        System.out.println((new Color(image.getRGB(6, 10))).getRed());
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
                int grayScale = newColor.getRGB(); //Usable RGB

                imageArray[row][col] = newRGB;

                image.setRGB(col, row, grayScale); //Set new Color!
                //if (row == 10 && col == 6) System.out.println(newRGB);
            }
        }
        writeImage(imageArray);
        renderImage(image);
    }

    public static void writeImage(int[][] imageArray) {
        String path = "./imageNameNew2.jpg";
        BufferedImage image = new BufferedImage(imageArray[0].length, imageArray.length, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < imageArray.length; x++) {
            for (int y = 0; y < imageArray[x].length; y++) {
                Color newColor = new Color(imageArray[x][y], imageArray[x][y], imageArray[x][y]); //Convert back to RGB
                int grayScale = newColor.getRGB(); //Usable RGB
                image.setRGB(y, x, grayScale);
            }
        }

        File ImageFile = new File(path);
        try {
            ImageIO.write(image, "png", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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