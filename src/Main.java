import java.io.File;

import net.sourceforge.tess4j.Tesseract;


public class Main {
	public static final String IMG_NAME = "invoice.gif";
	public static void main(String[] args) {
		readImage();
		View view = new View();
		view.setVisible(true);
	}
	
	public static void readImage() {
		File imageFile = new File(IMG_NAME);
		System.out.println(imageFile.exists() + "\n====================================");
		Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
		// Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
		try {
			String result = instance.doOCR(imageFile);
			System.out.println(result);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
