import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
 
public class ImageManip {
 
    private static BufferedImage original, grayscale, binarized;
 
    public static void main(String[] args) throws IOException {
 
        File original_f = new File(args[0]+".jpg");
        String output_f = args[0]+"_bin";
        original = ImageIO.read(original_f);
        grayscale = toGray(original);
        binarized = binarize(grayscale);
        writeImage(output_f);         
 
    }
    
    public static BufferedImage binarizeImage(BufferedImage bimg) {
    	 
        grayscale = toGray(bimg);
        binarized = binarize(grayscale);      
        return binarized;
        
    }
    
    public static BufferedImage greyscaleImage(BufferedImage bimg) {
   	    
		return toGray(bimg);
        
    }
    
    public static BufferedImage stitchImages(BufferedImage[] bimgarr, int cols, String filename){
    	
    	int piecewidth = bimgarr[0].getWidth();
    	int rows = (int) Math.ceil((double) bimgarr.length / cols);
    	int pieceheight = bimgarr[0].getHeight();
    	int type = bimgarr[0].getType();
    	
    	BufferedImage finalImg = new BufferedImage(piecewidth*cols, (int)(pieceheight*rows*1.05), type);
    	
        int n = 0;  
        for (int i = 0; i < rows; i++) {  
            for (int j = 0; j < cols; j++) {  
                finalImg.createGraphics().drawImage(bimgarr[n], piecewidth * j, pieceheight * i, null);  
                n++;  
            }  
        } 
        
        Graphics2D g = finalImg.createGraphics();
        g.setPaintMode();
        g.setFont(g.getFont().deriveFont(80f));
        g.drawString(filename, 100, (int)(pieceheight*rows*1.04));
        g.dispose();
        
        return finalImg;
    	
    }
 
    private static void writeImage(String output) throws IOException {
        File file = new File(output+".jpg");
        ImageIO.write(binarized, "jpg", file);
    }
 
    // Return histogram of grayscale image
    public static int[] imageHistogram(BufferedImage input) {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }
 
    // The luminance method
    private static BufferedImage toGray(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);
 
                // Write pixels into image
                lum.setRGB(i, j, newPixel);
 
            }
        }
 
        return lum;
 
    }
 
    // Get binary treshold using Otsu's method
    private static int otsuTreshold(BufferedImage original) {
 
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();
 
        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
 
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
 
        return threshold;
 
    }
 
    private static BufferedImage binarize(BufferedImage original) {
 
        int red;
        int newPixel;
 
        int threshold = otsuTreshold(original);
 
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel); 
 
            }
        }
 
        return binarized;
 
    }
 
    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
 
}