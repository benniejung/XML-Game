import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Timer;

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
		setTitle("Break Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	private GameManagement time, life;
	private Bullet bullet;
	private Meteor meteor;
	private Enemy enemy;
	ArrayList<Meteor> meteorArr = new ArrayList<Meteor>();
	boolean flag = true;
	boolean crash = false;
	String crashType;

	
	public MainPanel(Node mainPanelNode) {
		setLayout(null);
		setBackground(Color.black);
		setFocusable(true);
		
		// GameManagement노드
		Node gameManagementNode = XMLReader.getNode(mainPanelNode, XMLReader.E_GAMEMANAGEMENT);
		NodeList gameManagementNodeList = gameManagementNode.getChildNodes();
		for(int i=0; i<gameManagementNodeList.getLength(); i++) {
			Node node = gameManagementNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_TIME)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				int num = Integer.parseInt(XMLReader.getAttr(node, "num"));
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				time = new GameManagement(x,y,w,h,num, icon);
				add(time);
			}
			else if(node.getNodeName().equals(XMLReader.E_LIFE)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				int num = Integer.parseInt(XMLReader.getAttr(node, "num"));
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				life = new GameManagement(x,y,w,h,num, icon);
				add(life);
			}
		}
		int timeNum = time.getNum();
		int lifeNum = life.getNum();
		

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
		
		//Bullet노드
		Node bulletNode = XMLReader.getNode(mainPanelNode, XMLReader.E_BULLET);
		NodeList bulletNodeList = bulletNode.getChildNodes();
		for(int i=0; i<bulletNodeList.getLength(); i++) {
			Node node = bulletNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				//int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				bullet = new Bullet(y,w,h, icon);
			}
		}
		// Enemy노드
		Node enemyNode = XMLReader.getNode(mainPanelNode, XMLReader.E_ENEMY);
		NodeList enemyNodeList = enemyNode.getChildNodes();
		for(int i=0; i<enemyNodeList.getLength(); i++) {
			Node node = enemyNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = (int)(Math.random()*this.getWidth());
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				enemy = new Enemy(x,y,w,h,icon);
				
			}
		}
		//EnemyShootThread enemyShootTh = new EnemyShootThread(enemy);
		
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
		private BulletThread bulletThread;
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
			case KeyEvent.VK_SPACE:
				if (bulletThread == null || !bulletThread.isAlive()) {
					if (bullet.getY() <= 0) {
                        add(bullet);
                        bullet.setLocation(player.getX(), player.getY());
                    }else {
                        add(bullet);
                        bullet.setLocation(player.getX(), bullet.getY());

                    }
                    

                    bulletThread = new BulletThread(bullet);
                    bulletThread.start();
                }
				
				break;
			}
		}
	}
	public void moveMeteo() {
		for(Meteor meteo: meteorArr) {
        	crashCheck(meteo);
        	if(crash) {
        		switch(crashType) {
        		case "left-crash":
        			meteo.setLocation(meteo.getX() + meteo.getSpeed(), meteo.getY());
        			break;
        		case "right-crash":
        			meteo.setLocation(meteo.getX() - meteo.getSpeed(), meteo.getY());
        			break;
        		}
        	}
            else {
                meteo.setLocation(meteo.getX() - meteo.getSpeed(), meteo.getY());
            }
        }

	}

//	public void enemyShoot() {
//		Timer timer = new Timer();
//	    timer.schedule(new TimerTask() {
//	        @Override
//	        public void run() {
//	            for (int i = 0; i < 3; i++) {
//	                int randomX = (int) (Math.random() * getWidth()); // 랜덤 X 좌표
//	                int y = enemy.getY(); // 고정된 Y 좌표
//	                enemy.setLocation(randomX, y);
//	                add(enemy);
//
//
//
//	            }
//	        }
//	    }, 0, 10000); // 0초부터 시작해서 2초 간격으로 실행
//	}
	public void process() {
		// 운석 이동스레드
		moveMeteo();
		
//		// 적군 총알스레드
//		enemyShoot();
		
		// 화면 갱신
		repaint();
	}
	@Override
	public void run() {
	    while(flag) {
	        process();

	        try {
	            Thread.sleep(50);
	        } catch (InterruptedException e) {
	        	e.printStackTrace(); // 예외 로그 출력
	            return;
	        }
	    }
	}
	
	// 왼쪽 또는 오른쪽 충돌 확인 메소드
	public void crashCheck(Meteor meteo) {
		if(meteo.getX() <= 0) { 
            crash = true;
            crashType = "left-crash";
        } 
		else if(meteo.getX() + meteo.getWidth() >= 800) {
			crash = true;
			crashType = "right-crash";
		}
	}
	

	private class BulletThread extends Thread {
		private Bullet bullet;
		public BulletThread(Bullet bullet) {
			this.bullet = bullet;
		}
	    @Override
	    public void run() {
	    	
	    	while (flag) {
		        if(bullet.getY() <=0) {
		        	
		        	bullet.setY(player.getY());
		        	bullet.setLocation(player.getX(), player.getY());
		        	
		        }

	            bullet.setLocation(bullet.getX(), bullet.getY() - 10);
	            for (Iterator<Meteor> iterator = meteorArr.iterator(); iterator.hasNext();) {
	                Meteor meteo = iterator.next();
	                if (bullet.getBounds().intersects(meteo.getBounds())) {
	                    // Collision detected
	                    handleCollision(meteo, iterator);
	                    remove(bullet);
	                    
	                    
			        	System.out.println(bullet.getY());
	                    return; // Stop the thread
	                }
	            }
	            repaint();
		        
		        try {
		            Thread.sleep(50);
		        } catch (InterruptedException e) {
		        	e.printStackTrace(); // 예외 로그 출력
		            return;
		        }
		    }
	    }
	    private void handleCollision(Meteor meteo, Iterator it) {
	        // Update the state of the meteor, e.g., decrease its life
	        int currentLife = meteo.getLife();
	        if (currentLife > 0) {
	            meteo.setLife(currentLife - 1);
	            if(meteo.getLife() == 0) {
	            	remove(meteo);
		        	it.remove();
	            }
	            System.out.println(meteo.getLife());
	        }
	        bullet.setY(player.getY());
	        bullet.setLocation(player.getX(), player.getY());
	        

	        // You can also remove the bullet from the panel or reset its position, etc.
	        // For example: remove(bullet);
	        // Depending on your game logic, you might want to handle this differently.
	    }
	}
}
//class EnemyShootThread extends Thread {
//	private Enemy enemy;
//	public EnemyShootThread(Enemy enemy) {
//		this.enemy = enemy;
//	}
//	@Override
//	public void run() {
//		while(true) {
//            for (int i = 0; i < 3; i++) {
//            int randomX = (int) (Math.random() * 800); // 랜덤 X 좌표
//            int y = enemy.getY(); // 고정된 Y 좌표
//            enemy.setLocation(randomX, y);
//            //add(enemy);
//
//			
//	        try {
//	            Thread.sleep(50);
//	        } catch (InterruptedException e) {
//	        	e.printStackTrace(); // 예외 로그 출력
//	            return;
//	        }
//
//		}
//		
//	}
//}

class Bullet extends JLabel {
	Image img;
	public int y;
	public Bullet(int y, int w, int h, ImageIcon icon) {
		this.setIcon(icon);
		this.setBounds(this.getX(), y,w,h);
		this.y = y;
		img = icon.getImage();
	}
	
	public void setY(int newY) {y =newY;}
	
}
class GameManagement extends JLabel {
	Image img;
	private int num;
	public GameManagement(int x, int y, int w, int h, int num, ImageIcon icon) {
		this.setBounds(x,y,w,h);
		this.num = num;
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public int getNum() {return num;}
	public void setNum(int n) {num =n;}
}
