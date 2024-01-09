import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Enemy extends Label{

	public Enemy(int x, int y, int w, int h, ImageIcon icon) {
		super(x, y, w, h, icon);

	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}

}
