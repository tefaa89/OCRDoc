import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageContainer extends JPanel {
	private BufferedImage image;
	public int imgH;
	public int imgW;
	public ImageContainer() {
		try {
			image = ImageIO.read(new File(Main.IMG_NAME));
			imgH = image.getHeight();
			imgW = image.getWidth();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
		g.setColor(Color.RED);
	}
}
