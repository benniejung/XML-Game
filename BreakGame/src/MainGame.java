import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainGame extends JFrame{
	MainGame() {
		XMLReader xml = new XMLReader("stage1.xml"); // start.xml파일 객체 생성
		Node mainGameNode = xml.getMainGameElement();
		Node sizeNode = XMLReader.getNode(mainGameNode,XMLReader.E_SIZE);
		
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(w), Integer.parseInt(h));
		
		Container c = getContentPane();
		MainPanel mainPanel = new MainPanel(xml.getMainPanelElement());
	    c.add(mainPanel);
		

		createGameMenu();
		setResizable(false); // 크기 고정
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create and start the thread
        Thread thread = new Thread(mainPanel);
        thread.start();
		
	}
	private void createGameMenu() {
		JMenuBar gameMenuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("FILE");
		
		fileMenu.add(new JMenuItem("STAGE1"));
		fileMenu.add(new JMenuItem("STAGE2"));
		fileMenu.add(new JMenuItem("STAGE3"));
		fileMenu.add(new JMenuItem("STAGE4"));
		fileMenu.add(new JMenuItem("STAGE5"));
		
		gameMenuBar.add(fileMenu);
		
		setJMenuBar(gameMenuBar);
	}
	
}

class MainPanel extends JPanel implements Runnable {
	private Label player;
	private Meteor meteor;
	ArrayList<Meteor> meteorArr = new ArrayList<Meteor>();
	boolean flag = true;
	
	public MainPanel(Node mainPanelNode) {
		setLayout(null);
		setBackground(Color.black);
		setFocusable(true);


		// Player노드
		Node playerNode = XMLReader.getNode(mainPanelNode, XMLReader.E_PLAYER);
		NodeList playerNodeList = playerNode.getChildNodes();
		for(int i=0; i<playerNodeList.getLength(); i++) {
			Node node = playerNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				player = new Label(x,y,w,h, icon);
				add(player);
			}
		}
		
		// Meteor노드
		Node meteorNode = XMLReader.getNode(mainPanelNode, XMLReader.E_METEOR);
		NodeList meteorNodeList = meteorNode.getChildNodes();
		for(int i=0; i<meteorNodeList.getLength(); i++) {
			Node node = meteorNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
				int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				meteor = new Meteor(x,y,w,h,life,speed,icon);
				meteorArr.add(meteor);
				add(meteor);
				
			}
		}
		addKeyListener(new GameKeyListener());

	}
	// 키 이벤트
	public class GameKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			int x;
			switch(keyCode) {
			case KeyEvent.VK_LEFT:
				x = player.getX() -10;
				if(x>=0) {
					player.setLocation(x, player.getY());
				}
				break;
			case KeyEvent.VK_RIGHT:
				x = player.getX() + 10;
				int playerWidth = player.getWidth();
				if(x+playerWidth <= 800) {
					player.setLocation(x, player.getY());
				}
				break;
			}
		}
	}
	
	@Override
	public void run() {
	    while(flag) {
	        for(Meteor meteo: meteorArr) {
	            if(meteo.getX() == 0 || meteo.getX() + meteo.getWidth() == 800) {
	                meteo.setLocation(meteo.getX() + meteo.getSpeed(), meteo.getY());
	            } else {
	                meteo.setLocation(meteo.getX() - meteo.getSpeed(), meteo.getY());
	            }
	            System.out.println("Meteo X: " + meteo.getX()); // 로그 추가
	        }

	        repaint(); // 화면 갱신

	        try {
	            Thread.sleep(500);
	        } catch (InterruptedException e) {
	        	e.printStackTrace(); // 예외 로그 출력
	            return;
	        }
	    }
	}

	
	
	
	

}
