package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MainPanel extends JPanel{
	private int w,h;
	DrawPanel drawPanel;
	
	public MainPanel() {
		this.setLayout(null);
		this.setSize(800,700);
		this.setOpaque(true);
		
		
	}
	public int getW() {return w;}
	public int getH() {return h;}

	public void setSize(int w, int h) {
		this.w = w;
		this.h = h;
	}
	

}

class DrawPanel extends JPanel{
	private int w,h;
	MainPanel mainPanel;
	private ImageIcon bgIcon;
	private boolean flag = true;
	public DrawPanel(int w, int h) {
		this.w = w;
		this.h = h;
		
		this.setLayout(null);
		this.setSize(w,h);
		this.setOpaque(true);
		System.out.println("width: " + this.w+ " Height: "+this.h);

	}
	public void setBgIcon(ImageIcon bgIcon) {
		this.bgIcon = bgIcon;
	}
	public ImageIcon getBgIcon() {
		return bgIcon;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgIcon != null) {
			g.drawImage(bgIcon.getImage(), 0, 0,this.getWidth(),this.getHeight(), this);

		}

	}

}
class RightPanel extends JPanel{
	public RightPanel() {
		setLayout(new BorderLayout());
		this.setSize(600,800);

	}
}