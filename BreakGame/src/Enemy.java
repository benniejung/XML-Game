import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Enemy extends Label{
	Image img;
	public Enemy(int x, int y, int w, int h, ImageIcon icon) {
		super(x, y, w, h, icon);
		this.setBounds(x,y,w,h);
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}

}
