import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StartFrame extends JFrame{
	
	public StartFrame() {
		setTitle("Break Game");
		setSize(800, 600);
		createStartMenu();
		Container c = getContentPane();
		
		c.add(new StartPanel());
		setResizable(false); // 크기 고정
		setVisible(true);
		
		// 키 이벤트 포커스 주기(스윙프레임이 생기자마자 실행)
		c.setFocusable(true);
		c.requestFocus();
		
		
				
	}

	public static void main(String[] args) {
		new StartFrame();

	}
	
	class StartPanel extends JPanel {
		ImageIcon titleImg1;
		ImageIcon titleImg2;
		
		public StartPanel() {
			setLayout(null);
			setBackground(Color.black);
			
			JLabel titleLabel1 = new JLabel(new ImageIcon("image/title1.png"));
			JLabel titleLabel2 = new JLabel(new ImageIcon("image/title2.png"));
			
			titleLabel1.setBounds(170, 64, 461, 178);
			titleLabel2.setBounds(150,251,500,22);
			
			
			add(titleLabel1);
			add(titleLabel2);
			
			
		}
	}

	// 메뉴바
	private void createStartMenu() {
		JMenuBar startMenuBar = new JMenuBar();
		String[] fileItemTitle = {"game1", "game2", "game3" };
		String[] audioItemTitle = {"on", "off"};
		
		JMenu audioMenu = new JMenu("Audio");
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem [] audioItem = new JMenuItem[2];
		JMenuItem [] fileItem = new JMenuItem[3];
		
		MenuActionListener menuActionListener = new MenuActionListener();
		// 파일 메뉴아이템
		for(int i =0; i<fileItemTitle.length; i++) {
			fileItem[i] = new JMenuItem(fileItemTitle[i]);
			fileItem[i].addActionListener(menuActionListener);
			fileMenu.add(fileItem[i]);

		}
		startMenuBar.add(fileMenu);
		// 오디오 메뉴아이템
		for(int i =0; i<audioItemTitle.length; i++) {
			audioItem[i] = new JMenuItem(audioItemTitle[i]);
			audioItem[i].addActionListener(menuActionListener);
			audioMenu.add(audioItem[i]);

		}
		startMenuBar.add(audioMenu);
		
		setJMenuBar(startMenuBar);
	}
	
	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand(); // 선택한 메뉴아이템
			GameManagement.xmlFile = cmd;
			
			dispose();
			new MainGame();
		}
	}

}

class Label extends JLabel {
	Image img;
	private int x, y, w, h;

	public Label(int x, int y, int w, int h, ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

		this.setBounds(x, y, w, h);
		img = icon.getImage();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int newX) {
		x = newX;
	}

	public void setY(int newY) {
		y = newY;
	}

	public void setXY(int newX, int newY) {
		x = newX;
		y = newY;
	}
}

