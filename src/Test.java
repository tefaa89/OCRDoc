import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TessAPI.TessPageSegMode;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.vietocr.ImageIOHelper;

import com.sun.jna.Pointer;

public class Test {

	public static TessAPI api;
	public static TessAPI1.TessBaseAPI handle;

	public static void main(String[] args) {
		handle = TessAPI1.TessBaseAPICreate();
		try {
			testResultIterator();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readImage() {
		File imageFile = new File("image.png");
		System.out.println(imageFile.exists() + "\n====================================");
		Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
		instance.setPageSegMode(TessPageSegMode.PSM_SPARSE_TEXT);
		// Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
		try {
			String result = instance.doOCR(imageFile);
			System.out.println(result);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	public static void tessRec()
	{
		System.out.println("TessBaseAPIRect");
        String lang = "eng";
        File tiff = new File("image.png");
        BufferedImage image = null;
		try {
			image = ImageIO.read(tiff);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // require jai-imageio lib to read TIFF
        ByteBuffer buf = ImageIOHelper.convertImageData(image);
        int bpp = image.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
        TessAPI1.TessBaseAPIInit3(handle, "tessdata", lang);
        TessAPI1.TessBaseAPISetPageSegMode(handle, TessAPI1.TessPageSegMode.PSM_AUTO);
        Pointer utf8Text = TessAPI1.TessBaseAPIRect(handle, buf, bytespp, bytespl, 0, 0, 1024, 800);
        String result = utf8Text.getString(0);
        TessAPI1.TessDeleteText(utf8Text);
        System.out.println(result);
	}
	
	public static void testResultIterator() throws Exception {
		System.out.println("TessBaseAPIGetIterator");
        String lang = "eng";
        File tiff = new File("invoice.gif");
        BufferedImage image = ImageIO.read(new FileInputStream(tiff)); // require jai-imageio lib to read TIFF
        ByteBuffer buf = ImageIOHelper.convertImageData(image);
        int bpp = image.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
        TessAPI1.TessBaseAPIInit3(handle, "tessdata", lang);
        TessAPI1.TessBaseAPISetPageSegMode(handle, TessAPI1.TessPageSegMode.PSM_AUTO);
        TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
        TessAPI1.TessBaseAPIRecognize(handle, null);
        TessAPI1.TessResultIterator ri = TessAPI1.TessBaseAPIGetIterator(handle); //EACH WORD in a BLOCK
        TessAPI1.TessPageIterator pi = TessAPI1.TessResultIteratorGetPageIterator(ri); // EACH BLOCK in A PAGE
        TessAPI1.TessPageIteratorBegin(pi);
        System.out.println("Bounding boxes:\nchar(s) left top right bottom confidence font-attributes");

//        int height = image.getHeight();
        do {
            Pointer ptr = TessAPI1.TessResultIteratorGetUTF8Text(ri, TessAPI1.TessPageIteratorLevel.RIL_BLOCK);
            String word = ptr.getString(0);
            TessAPI1.TessDeleteText(ptr);
            float confidence = TessAPI1.TessResultIteratorConfidence(ri, TessAPI1.TessPageIteratorLevel.RIL_BLOCK);
            IntBuffer leftB = IntBuffer.allocate(1);
            IntBuffer topB = IntBuffer.allocate(1);
            IntBuffer rightB = IntBuffer.allocate(1);
            IntBuffer bottomB = IntBuffer.allocate(1);
            TessAPI1.TessPageIteratorBoundingBox(pi, TessAPI1.TessPageIteratorLevel.RIL_BLOCK, leftB, topB, rightB, bottomB);
            int left = leftB.get();
            int top = topB.get();
            int right = rightB.get();
            int bottom = bottomB.get();
            System.out.print(String.format("%s %d %d %d %d %f", word, left, top, right, bottom, confidence));
//            System.out.println(String.format("%s %d %d %d %d", str, left, height - bottom, right, height - top)); // training box coordinates     

            IntBuffer boldB = IntBuffer.allocate(1);
            IntBuffer italicB = IntBuffer.allocate(1);
            IntBuffer underlinedB = IntBuffer.allocate(1);
            IntBuffer monospaceB = IntBuffer.allocate(1);
            IntBuffer serifB = IntBuffer.allocate(1);
            IntBuffer smallcapsB = IntBuffer.allocate(1);
            IntBuffer pointSizeB = IntBuffer.allocate(1);
            IntBuffer fontIdB = IntBuffer.allocate(1);
            String fontName = TessAPI1.TessResultIteratorWordFontAttributes(ri, boldB, italicB, underlinedB,
                    monospaceB, serifB, smallcapsB, pointSizeB, fontIdB);
            boolean bold = boldB.get() == TessAPI1.TRUE;
            boolean italic = italicB.get() == TessAPI1.TRUE;
            boolean underlined = underlinedB.get() == TessAPI1.TRUE;
            boolean monospace = monospaceB.get() == TessAPI1.TRUE;
            boolean serif = serifB.get() == TessAPI1.TRUE;
            boolean smallcaps = smallcapsB.get() == TessAPI1.TRUE;
            int pointSize = pointSizeB.get();
            int fontId = fontIdB.get();
            System.out.println(String.format("  font: %s, size: %d, font id: %d, bold: %b," +
                       " italic: %b, underlined: %b, monospace: %b, serif: %b, smallcap: %b", 
                    fontName, pointSize, fontId, bold, italic, underlined, monospace, serif, smallcaps));            
        } while (TessAPI1.TessPageIteratorNext(pi, TessAPI1.TessPageIteratorLevel.RIL_BLOCK) == TessAPI1.TRUE);
        
	}
}

