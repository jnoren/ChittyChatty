import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class ReceiveImage extends JFrame {

  
	private static final long serialVersionUID = 8874332343998700060L;


	@SuppressWarnings("resource")
	public ReceiveImage() {
        super("ReceiveImage");
        this.setSize(500, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        //JFrame frame = new JFrame();

        try {
           // @SuppressWarnings("resource")
			Socket socket1 = new Socket("localhost", 8000);
            BufferedImage image = ImageIO.read(socket1.getInputStream());
            this.add(new JLabel(new ImageIcon(image)), BorderLayout.CENTER);
            
            revalidate();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println(e);
        }
    }


    public static void main(String[] args) {        
                new ReceiveImage();
    } 

}
