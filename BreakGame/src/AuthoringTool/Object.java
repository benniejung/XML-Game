package AuthoringTool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Object extends JLabel{
	private int x,y,w,h;
	ImageIcon icon;
	Image img;
	abstract public String toString();
	public Object() {}
	public Object(int x, int y, int w, int h, ImageIcon icon) { 
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.icon = icon;
		setIcon(icon);
		img = icon.getImage();
		this.setBounds(x,y,w,h);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getW() {return w;}
	public int getH() {return h;}
	
	public void setX(int newX) { x= newX;}
	public void setY(int newY) { y= newY;}
	public void setW(int newW) { w= newW;}
	public void setH(int newH) { h= newH;}
}
class PolygonObj extends JLabel {
	int x1, x2, y1, y2;

	Point [] point = new Point[4];
	Polygon poly = new Polygon();

	public PolygonObj(int x1, int y1, int x2, int y2) {
		if(x2 < x1) {
			int temp = x1;
			this.x1 = x2;
			this.x2 = temp;
		}
		else {
			this.x1= x1;
			this.x2 = x2;
		}
		if(y2 < y1) {
			int temp = y1;
			this.y1 = y2;
			this.y2 = temp;
		}
		else {
			this.y1= y1;
			this.y2= y2;
		}				
		
		this.setBounds(x1, y1, x2-x1 , y2-y1);
		int [] xpoints = {x1,x1,x2,x2};
		int [] ypoints = {y1,y2,y2,y1};
		poly= new Polygon(xpoints, ypoints, 4);
	}
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRoundRect(0, 0, getWidth() -1, getHeight() - 1, 3, 3);
  	  super.paintComponent(g);
  	  
    }

}
class EnemyObj extends Object {
	private String type;
	private int speed,life;
	static Vector<EnemyObj> enemys = new Vector<EnemyObj>();
	public EnemyObj() {}
	public EnemyObj(int x, int y, int w, int h, String type, int speed, int life, ImageIcon icon) {
		super(x,y,w,h,icon);
		this.type = type;
		this.speed = speed;
		this.life = life;
		
	}
	@Override
	public String toString() {
		String text = "<Enemies>\n";
		for(int i =0; i<enemys.size(); i++) {
			EnemyObj enemy = enemys.get(i);
			String iconDescription = enemy.icon.getDescription();
			text+= "<Enemy x=\""+enemy.getX()+"\" y=\""+enemy.getY()+"\" w=\""+enemy.getW()+"\" h=\""+enemy.getH()+"\"";
			text+= " type=\""+enemy.getType()+"\" speed=\""+enemy.getSpeed()+"\" life=\""+enemy.getLife()+"\"";
			text+= " icon=\""+iconDescription+"\"></Enemy>\n";
			

		}
		text+="</Enemies>\n";
		return text;
	}
	public int getLife() {return life;}
	public String getType() {return type;}
	public int getSpeed() {return speed;}
	
	public void setLife(int newLife) {life = newLife;}
	public void setType(String newType) {type = newType;}
	public void setSpeed(int newSpeed) {speed = newSpeed;}
}
class PlayerObj extends Object {
	private int life;
	static Vector<PlayerObj> player = new Vector<PlayerObj>(1); // 플레이어는 한개만 저장하도록
	public PlayerObj() {};
	public PlayerObj(int x, int y, int w, int h, int life, ImageIcon icon) {
		super(x,y,w,h,icon);
		this.life = life;
		
	}
	@Override
	public String toString() {
		String text = "<Player>\n";
		for(int i =0; i<player.size(); i++) {
			PlayerObj playerObj = player.get(i);
			String iconDescription = playerObj.icon.getDescription();
			text+= "<Obj x=\""+playerObj.getX()+"\" y=\""+playerObj.getY()+"\" w=\""+playerObj.getW()+"\" h=\""+playerObj.getH()+"\"";
			text+= " life=\""+playerObj.getLife()+"\"";
			text+= " icon=\""+iconDescription+"\"></Obj>\n";
			

		}
		text+="</Player>\n";
		return text;
	}
	public int getLife() {return life;}
	
	public void setLife(int newLife) {life = newLife;}
}
class ShieldBlockObj extends Object {
	private String type;
	static Vector<ShieldBlockObj> shieldBlocks = new Vector<ShieldBlockObj>(); // 플레이어는 한개만 저장하도록
	public ShieldBlockObj() {};
	public ShieldBlockObj(int x, int y, int w, int h, String type, ImageIcon icon) {
		super(x,y,w,h,icon);
		this.type = type;
	}
	@Override
	public String toString() {
		String text = "<ShieldBlocks>\n";
		for(int i =0; i<shieldBlocks.size(); i++) {
			ShieldBlockObj shieldBlock = shieldBlocks.get(i);
			String iconDescription = shieldBlock.icon.getDescription();
			text+= "<ShieldBlock x=\""+shieldBlock.getX()+"\" y=\""+shieldBlock.getY()+"\" w=\""+shieldBlock.getW()+"\" h=\""+shieldBlock.getH()+"\"";
			text+= " type=\""+shieldBlock.getType()+"\"";
			text+= " icon=\""+iconDescription+"\"></ShieldBlock>\n";
			

		}
		text+="</ShieldBlocks>\n";
		return text;
	}
	public String getType() {return type;}
	
	public void setType(String newType) {type = newType;}

}
