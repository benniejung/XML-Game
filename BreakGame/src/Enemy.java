import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Enemy extends Label{
	Image img;
	private int life,speed, type;
	public Enemy(int x, int y, int w, int h, int type, int life, int speed,ImageIcon icon) {
		super(x, y, w, h, icon);
		this.setBounds(x,y,w,h);
		this.life = life;
		this.speed = speed;
		this.type = type;
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public int getLife() {return life;}
	public int getSpeed() {return speed;}
	public int getType() {return type;}
	
	public void setLife(int newLife) {life = newLife;} 

}
