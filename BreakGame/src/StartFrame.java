import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
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
	
	StartFrame() {
		setTitle("Break Game");
		XMLReader xml = new XMLReader("start.xml"); // start.xml파일 객체 생성
		Node StartFrameNode = xml.getStartFrameElement();
		Node sizeNode = XMLReader.getNode(StartFrameNode, XMLReader.E_SIZE);
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(w), Integer.parseInt(h));
		createStartMenu();
		Container c = getContentPane();
		
		c.add(new StartPanel(xml.getStartPanelElement()));
		c.addKeyListener(new StartKeyLisetener());
		setResizable(false); // 크기 고정
		setVisible(true);
		
		// 키 이벤트 포커스 주기(스윙프레임이 생기자마자 실행)
		c.setFocusable(true);
		c.requestFocus();
		
		
				
	}

	public static void main(String[] args) {
		new StartFrame();

	}
	// 키 이벤트
	public class StartKeyLisetener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar()== '\n') {
				dispose();
				new MainGame();
			}
		}
	}
	
	// 메뉴바
	private void createStartMenu() {
		JMenuBar startMenuBar = new JMenuBar();
		JMenu audioMenu = new JMenu("Audio");
		JMenu fileMenu = new JMenu("File");
		
		// 오디오 메뉴아이템
		audioMenu.add(new JMenuItem("On"));
		audioMenu.add(new JMenuItem("Off"));
		startMenuBar.add(audioMenu);
		
		// 
		startMenuBar.add(fileMenu);
		
		setJMenuBar(startMenuBar);
	}

}

class Label extends JLabel {
	Image img;
	private int x,y,w,h;
	public Label(int x, int y, int w, int h, ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.setBounds(x,y,w,h);
		img = icon.getImage();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public void setX(int newX) {x = newX;}
}

class StartPanel extends JPanel {
	ImageIcon titleImg1;
	ImageIcon titleImg2;
	
	public StartPanel(Node startPanelNode) {
		setLayout(null);
		setBackground(Color.black);
		
		// Label 노드
		Node labelNode = XMLReader.getNode(startPanelNode, XMLReader.E_LABEL);
		NodeList nodeList = labelNode.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				Label label = new Label(x,y,w,h, icon);
				add(label);
			}
		}

		
		
		
		
	}
}
