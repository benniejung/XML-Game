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
		GamePanel gamePanel = new GamePanel(xml.getGamePanelElement());
	    c.add(gamePanel);
		

		createGameMenu();
		setResizable(false); // 크기 고정
		setVisible(true);
		
        Thread thread = new Thread(gamePanel);
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

class GamePanel extends JPanel implements Runnable {
	private Label player;
	private GameManagement time, life;
	private Bullet bullet;
	private Meteor meteor;
	private Enemy enemy;
	ArrayList<Enemy> enemyArr = new ArrayList<Enemy>();
	boolean flag = true;
	boolean crash = false;
	String crashType;
	ImageIcon bgImg;
	
	public GamePanel(Node gamePanelNode) {
		setLayout(null);
		setFocusable(true);
		Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BG);
		bgImg = new ImageIcon(bgNode.getTextContent());
		Node activeScreenNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ACTIVESCREEN);


		// Player노드
		Node playerNode = XMLReader.getNode(activeScreenNode, XMLReader.E_PLAYER);
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
		

		// Enemy노드
		Node enemiesNode = XMLReader.getNode(activeScreenNode, XMLReader.E_ENEMIES);
		NodeList enemiesNodeList = enemiesNode.getChildNodes();
		for(int i=0; i<enemiesNodeList.getLength(); i++) {
			Node node = enemiesNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_ENEMY)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				enemy = new Enemy(x,y,w,h,icon);
				enemyArr.add(enemy);
				add(enemy);
				
			}
		}
		

		addKeyListener(new GameKeyListener());

	}
	// 키 이벤트
	public class GameKeyListener extends KeyAdapter {
		//private BulletThread bulletThread;
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
//				if (bulletThread == null || !bulletThread.isAlive()) {
//					if (bullet.getY() <= 0) {
//                        add(bullet);
//                        bullet.setLocation(player.getX(), player.getY());
//                    }else {
//                        add(bullet);
//                        bullet.setLocation(player.getX(), bullet.getY());
//
//                    }
//                    
//
//                    bulletThread = new BulletThread(bullet);
//                    bulletThread.start();
//                }
				
				break;
			}
		}
	}
	public void moveEnemy() {
		for(Enemy enemy : enemyArr) {
			int x = (int)(Math.random()*this.getWidth()+20);
			int y = (int)(Math.random()*this.getHeight()+20);
        	crashCheck(enemy);
        	if(crash) {
//        		switch(crashType) {
//        		case "left-crash":
//        			enemy.setLocation(enemy.getX() + enemy.getSpeed(), enemy.getY());
//        			break;
//        		case "right-crash":
//        			meteo.setLocation(enemy.getX() - enemy.getSpeed(), enemy.getY());
//        			break;
//        		}
        	}
            else {
            	enemy.setLocation(x+5, y+5);
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
		moveEnemy();
		
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
	public void crashCheck(Enemy enemy) {
		if(enemy.getX() <= 0) { 
            crash = true;
            crashType = "left-crash";
        } 
		else if(enemy.getX() + enemy.getWidth() >= 800) {
			crash = true;
			crashType = "right-crash";
		}
	}
	

//	private class BulletThread extends Thread {
//		private Bullet bullet;
//		ImageIcon crashEffectIcon = new ImageIcon("image/crash_effect.png");
//		Image crashEffectImg = crashEffectIcon.getImage();
//		public BulletThread(Bullet bullet) {
//			this.bullet = bullet;
//		}
//	    @Override
//	    public void run() {
//	    	
//	    	while (flag) {
//		        if(bullet.getY() <=0) {
//		        	
//		        	bullet.setY(player.getY());
//		        	bullet.setLocation(player.getX(), player.getY());
//		        	
//		        }
//
//	            bullet.setLocation(bullet.getX(), bullet.getY() - 10);
//	            for (Iterator<Meteor> iterator = meteorArr.iterator(); iterator.hasNext();) {
//	                Meteor meteo = iterator.next();
//	                if (bullet.getBounds().intersects(meteo.getBounds())) {
//	                    
//	                    handleCollision(meteo, iterator);
//	                    remove(bullet);
//	                    
//	                    
//			        	System.out.println(bullet.getY());
//	                    return; 
//	                }
//	            }
//	            repaint();
//		        
//		        try {
//		            Thread.sleep(50);
//		        } catch (InterruptedException e) {
//		        	e.printStackTrace(); // 예외 로그 출력
//		            return;
//		        }
//		    }
//	    }
//	    private void handleCollision(Meteor meteo, Iterator it) {
//	        int currentLife = meteo.getLife();
//	        if (currentLife > 0) {
//	            meteo.setLife(currentLife - 1);
//	            if(meteo.getLife() == 0) {
//	            	remove(meteo);
//		        	it.remove();
//	            }
//	            System.out.println(meteo.getLife());
//	        }
//	        bullet.setY(player.getY());
//	        bullet.setLocation(player.getX(), player.getY());
//	        
//
//	    }
//	}
	public void paintComponent(Graphics g) {
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
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
