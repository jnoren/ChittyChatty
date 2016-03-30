import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SendImage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6734927336296353481L;
	BufferedImage bi;
    JButton btn;
    ServerSocket serverSocket1;
    Socket socket1;
    JFileChooser chooser; 
    File file; 
    String img;

    public SendImage() {
        super("SendImage");
        setLayout(new BorderLayout());
        
        bi = createImage();
        
        add(btn = new JButton("Send"), BorderLayout.NORTH);
        add(new JLabel(new ImageIcon(bi)), BorderLayout.CENTER);
        
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        try{
            serverSocket1 = new ServerSocket(8000);
             socket1 = serverSocket1.accept();
        }
        catch(IOException ex){
            System.err.println(ex);
        }
        

        btn.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent arg0) {

                try {
                    ImageIO.write(bi, "JPG", socket1.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
	public BufferedImage createImage() {
    	JFileChooser chooser = new JFileChooser();        
       
		chooser.showOpenDialog(getContentPane());
       
            try {
                bi = ImageIO.read(chooser.getSelectedFile());
            } catch (IOException e) {
            	e.printStackTrace();
            }        

        return bi;
    }

   public static void main(String[] args) {        
	   new SendImage();
    }

}
