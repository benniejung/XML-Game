package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
	public DrawPanel(int w, int h, MainPanel mainPanel) {
		this.w = w;
		this.h = h;
		this.mainPanel = mainPanel;
		
		this.setLayout(null);
		this.setSize(w,h);
		this.setOpaque(true);
		System.out.println("width: " + this.w+ " Height: "+this.h);
		
		//this.addMouseMotionListener(new drawPanelMouseListener(this));

	}
	public void setBgIcon(ImageIcon bgIcon) {
		this.bgIcon = bgIcon;
	}
	public void removeBgIcon() {
		this.bgIcon = null;
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
	class drawPanelMouseListener extends MouseAdapter  {
		DrawPanel drawPanel;
		public drawPanelMouseListener(DrawPanel drawPanel) {
			this.drawPanel = drawPanel;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			mainPanel.remove(drawPanel);
			drawPanel.setLocation(x,y);
			mainPanel.repaint();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			mainPanel.remove(drawPanel);
			drawPanel.setLocation(e.getX(),e.getY());
			mainPanel.repaint();
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
class RightPanel extends JPanel{
	public RightPanel() {
		setLayout(new BorderLayout());
		this.setSize(600,800);

	}
}