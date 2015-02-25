import java.io.File;
import java.io.IOException;

import net.sourceforge.tess4j.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class OCR {

	public static void main(String[] args) throws IOException {
		
		String filepath = new String("C:\\Users\\Tan Zong Ying\\Desktop\\OCRChecker\\");
		String filename = new String("blastoise");
		String fileext = new String(".png");
		
        File imageFile = new File(filepath + filename + fileext);
        Tesseract instance = Tesseract.getInstance();
        BufferedImage bimg = ImageIO.read(imageFile);
        
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        System.out.println(width + "\t" + height);
        
        Rectangle allmovesrect = new Rectangle((int) (width * 0.59), (int) (height * 2 / 3), (int) (width * 0.35), height - (int) (height * 2 / 3));
        Rectangle move1rect = new Rectangle((int) (width * 0.59), (int) (height * 2 / 3), (int) (width * 0.35), (int) (height / 12));
        Rectangle move2rect = new Rectangle((int) (width * 0.59), (int) (height * 9 / 12), (int) (width * 0.35), (int) (height / 12));
        Rectangle move3rect = new Rectangle((int) (width * 0.59), (int) (height * 10 / 12), (int) (width * 0.35), (int) (height / 12));
        Rectangle move4rect = new Rectangle((int) (width * 0.59), (int) (height * 11 / 12), (int) (width * 0.35), (int) (height / 12));
        
        bimg = OtsuBinarize.binarizeImage(bimg);
        Graphics2D graph = bimg.createGraphics();
        graph.setColor(Color.BLACK);
        graph.draw(move1rect);
        graph.setColor(Color.RED);
        graph.draw(move2rect);
        graph.setColor(Color.YELLOW);
        graph.draw(move3rect);
        graph.setColor(Color.GREEN);
        graph.draw(move4rect);
        graph.dispose();
        ImageIO.write(bimg, "png", new File(filepath + filename + "_2" + fileext));
        
        try {
            String move1 = instance.doOCR(bimg, move1rect);
            String move2 = instance.doOCR(bimg, move2rect);
            String move3 = instance.doOCR(bimg, move3rect);
            String move4 = instance.doOCR(bimg, move4rect);
            System.out.println(move1 + ";\t" + move2 + ";\t" + move3 + ";\t" + move4);
            //System.out.println(move3);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
	
}
