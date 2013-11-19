import javax.swing.JFrame;


public class View extends JFrame{
	private ImageContainer imageContainer;
	public View() {
		imageContainer = new ImageContainer();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setContentPane(imageContainer);
		setLayout(null);
		setVisible(true);
		setBounds(10, 10, imageContainer.imgW, imageContainer.imgH);
	}
}
