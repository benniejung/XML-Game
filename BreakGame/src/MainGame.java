import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MainGame extends JFrame {
	GamePanel gamePanel;
	GameInfoPanel gameInfoPanel = new GameInfoPanel();
	Container c;
	Music music = new Music();
	MainGame() {
		setTitle("Break Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		XMLReader xml = new XMLReader(GameManagement.xmlFile);
		
		Node mainGameNode = xml.getMainGameElement();
		Node sizeNode = XMLReader.getNode(mainGameNode,XMLReader.E_SIZE);
		
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(w), Integer.parseInt(h)+100);
		 
		c = getContentPane();
		gamePanel = new GamePanel(xml.getGamePanelElement(), gameInfoPanel, music);
	    c.add(gamePanel);
	    makeSplitPane();
	    gamePanel.requestFocus();
	    gameInfoPanel.requestFocus();
		createGameMenu();
		setResizable(false);  // 크기 고정
		setVisible(true);
		
	}	// 스플릿팬 만들기 (영역 만들고 패널 부착)
	private void makeSplitPane() {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.VERTICAL_SPLIT); // 수평으로 분할
		hPane.setDividerLocation(50); // 디바이더 초기 위치 설정
		hPane.setDividerSize(0); // 스플릿팬 선 안보이게
		c.add(hPane, BorderLayout.CENTER); // ContentPane 불러서 가운데에 부착

		hPane.setBottomComponent(gamePanel); // gamePanel을 hPane 왼쪽에 부착
		hPane.setTopComponent(gameInfoPanel);
	}

	private void createGameMenu() {
		JMenuBar gameMenuBar = new JMenuBar();
		//String[] fileItemTitle = {"game1", "game2", "game3" };
		String[] audioItemTitle = {"on", "off"};
		
		JMenu audioMenu = new JMenu("Audio");
		JMenu fileMenu = new JMenu("File");
		JMenu startMenu = new JMenu("Start");
		JMenu stopMenu = new JMenu("Stop");
		
		
		JMenuItem [] audioItem = new JMenuItem[2];
		//JMenuItem [] fileItem = new JMenuItem[3];
		
		FileMenuActionListener fileMenuActionListener = new FileMenuActionListener();
		AudioMenuActionListener audioMenuActionListener = new AudioMenuActionListener();
		StopMenuActionListener stopMenuActionListener = new StopMenuActionListener();
		StartMenuActionListener startMenuActionListener = new StartMenuActionListener();
		
		
		JMenuItem fileItem = new JMenuItem("파일 선택");
		fileItem.addActionListener(fileMenuActionListener);
		fileMenu.add(fileItem);

		// 파일 메뉴아이템
		
//		for(int i =0; i<fileItemTitle.length; i++) {
//			fileItem[i] = new JMenuItem(fileItemTitle[i]);
//			fileItem[i].addActionListener(fileMenuActionListener);
//			fileMenu.add(fileItem[i]);
//
//		}
		gameMenuBar.add(fileMenu);
		// 오디오 메뉴아이템
		for(int i =0; i<audioItemTitle.length; i++) {
			audioItem[i] = new JMenuItem(audioItemTitle[i]);
			audioItem[i].addActionListener(audioMenuActionListener);
			audioMenu.add(audioItem[i]);

		}
		gameMenuBar.add(audioMenu);
		// 게임 시작메뉴
		JMenuItem startItem = new JMenuItem("Start");
		startItem.addActionListener(startMenuActionListener);
		startMenu.add(startItem);
		gameMenuBar.add(startMenu);
		// 게임 중지메뉴
		JMenuItem stopItem = new JMenuItem("Stop");
		JMenuItem resumeItem = new JMenuItem("Resume");
		stopMenu.add(stopItem);
		stopMenu.add(resumeItem);
		stopItem.addActionListener(stopMenuActionListener);
		resumeItem.addActionListener(stopMenuActionListener);
		
		gameMenuBar.add(stopMenu);
		
		setJMenuBar(gameMenuBar);
	}
	class FileMenuActionListener implements ActionListener {
		private JFileChooser chooser;
		public FileMenuActionListener() {
			chooser = new JFileChooser("C:\\Users\\User\\git\\XML-Game\\BreakGame"); // 해당 경로에서 파일 찾기

		}
		public void actionPerformed(ActionEvent e) {
//			String cmd = e.getActionCommand(); // 선택한 메뉴아이템
//			GameManagement.xmlFile = cmd;
//			dispose();
//			new MainGame();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
			chooser.setFileFilter(filter); // xml파일만 선택할 수 있음
			int ret = chooser.showSaveDialog(gamePanel);
			if (ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 저장해주세요", "파일 저장", JOptionPane.WARNING_MESSAGE);
				return;
			}
	
		}
	}
	class AudioMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand(); // 선택한 메뉴아이템
			if(cmd.equals("off")) {
				music.stopAudio("all");
			}

		}
	}
	class StopMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand(); // 선택한 메뉴아이템
			if(cmd == "Stop") {
				gamePanel.stopGame();
				
			}else {
				gamePanel.resumeGame();
			}
		}
	}
	class StartMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand(); // 선택한 메뉴아이템
			if(cmd == "Start") {
				gamePanel.resumeGame();
				
			}
		}
	}
}

class GamePanel extends JPanel {
//	 노드 변수들
	private Player player;
	private Meteor meteor;
	private Enemy enemy;
	private Label item;
	private ShieldBlock shieldBlock;
	private JLabel lifeTextLabel = new JLabel(new ImageIcon("image/life.png"));
	JLabel lifeLabel;
	JLabel scoreLabel;
	ImageIcon playerBulletIcon = new ImageIcon("image/bullet.png");

	// 음악
	private Music music = null;
	
	// 패널
	private JPanel topPanel;
	private JPanel activeScreenPanel;
	private GameInfoPanel gameInfoPanel;
	
	
	Vector<Label> itemArr = new Vector<Label>();
	Vector<Enemy> enemyArr = new Vector<Enemy>();
	Vector<ShieldBlock> shieldBlockArr = new Vector<ShieldBlock>();
	boolean flag = true;
	ImageIcon bgImg;
	
	private Bullet bullet;
//	MoveEnemyThread moveEnemyTh;
	EnemyShootThread enemyShootTh;
	ItemThread itemTh;
	 
	private boolean stopFlag = false;
	private boolean getStopFlag() {return stopFlag;}
	public void setStopFlag() {
		if(stopFlag==true) {
			stopFlag = false;
		} else {
			stopFlag = true;
		}
	}
	
	public GamePanel(Node gamePanelNode, GameInfoPanel gameInfoPanel, Music music) {
		setLayout(new BorderLayout());
		setFocusable(true); 
		addKeyListener(new GameKeyListener());
		Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BG);
		bgImg = new ImageIcon(bgNode.getTextContent());
		Node soundNode = XMLReader.getNode(gamePanelNode, XMLReader.E_SOUND);
		this.music = music;
		music.playAudio(soundNode.getTextContent());

		Node activeScreenNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ACTIVESCREEN);
		this.gameInfoPanel = gameInfoPanel;
		

//		 Player노드
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
				int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				player = new Player(x,y,w,h, life, icon);
				add(player);
			}
		}
		GameManagement.life = player.getLife();
		
//		 Enemy노드
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
				int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
				int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
				String type = XMLReader.getAttr(node, "type");
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				
				enemy = new Enemy(x,y,w,h,type,life,speed, icon, this);
				enemyArr.add(enemy);
				add(enemy);
				
			}
		}


//		 아이템 노드
		Node itemsNode = XMLReader.getNode(activeScreenNode, XMLReader.E_ITEMS);
		NodeList itemsNodeList = itemsNode.getChildNodes();
		for(int i=0; i<itemsNodeList.getLength(); i++) {
			Node node = itemsNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_ITEM)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));

				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				Label item = new Label(x,y,w,h,icon);
				itemArr.add(item);
				
				
			}
		}

		// shieldBlock 노드
//		Node shieldBlocksNode = XMLReader.getNode(activeScreenNode, XMLReader.E_SHIELDBLOCKS);
//		NodeList shieldBlocksNodeList = shieldBlocksNode.getChildNodes();
//		for(int i=0; i<shieldBlocksNodeList.getLength(); i++) {
//			Node node = shieldBlocksNodeList.item(i);
//			if(node.getNodeType() != Node.ELEMENT_NODE)
//				continue;
//			// found!!, <Obj> tag
//			if(node.getNodeName().equals(XMLReader.E_SHIELDBLOCK)) {
//				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
//				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
//				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
//				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
//				String type = XMLReader.getAttr(node, "type");
//				
//				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
//				shieldBlock = new ShieldBlock(x,y,w,h,icon,type);
//				shieldBlockArr.add(shieldBlock);
//				add(shieldBlock);
//				
//			}
//		}

////		 스레드 생성
//		moveEnemyTh = new MoveEnemyThread(this,enemyArr);
//		moveEnemyTh.start();
//	
		
//		enemyShootTh = new EnemyShootThread(this,enemyArr, player,lifeLabel, gameInfoPanel, shieldBlockArr);
//		enemyShootTh.start();
	
		itemTh = new ItemThread(this, itemArr);
		itemTh.start();

//		Thread th = new Thread(this);
//		th.start();	
 
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);		
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public Vector<Label> getItemArr() {
        return itemArr;
    }

    public void addItem(Label item) {
        itemArr.add(item);
    }
   
//	 키 이벤트
	public class GameKeyListener extends KeyAdapter {
		private BulletThread bulletThread;
		ImageIcon bulletIcon1 = new ImageIcon("image/bullet.png");
		Image bullet1 = bulletIcon1.getImage();
		ImageIcon bulletIcon2 = new ImageIcon("image/bullet2.png");
		Image bullet2 = bulletIcon2.getImage();
		PlayerBullet playerBullet;
		
		@Override
		public void keyPressed(KeyEvent e) {
	        int keyCode = e.getKeyCode();
	        int x;

	        switch(keyCode) {
	            case KeyEvent.VK_LEFT:
	            	x = player.getX() - 10;
	                if (x >= 0) {
	                    player.setLocation(x, player.getY());
	                    player.setX(x);
	                }
	                break;
	            case KeyEvent.VK_RIGHT:
	                x = player.getX() + 10;
	                int playerWidth = player.getWidth();
	                if (x + playerWidth <= getWidth()) {
	                    player.setLocation(x, player.getY());
	                    player.setX(x);
	                }
	                break;

				case KeyEvent.VK_SPACE:
					playerBullet = new PlayerBullet(0,0,25,30, playerBulletIcon);
	
					if (bulletThread == null || !bulletThread.isAlive()) {
						if (playerBullet.getY() <= 0) {
							remove(playerBullet);
							//add(playerBullet);
	                        playerBullet.setLocation(player.getX(), player.getY());
	                    }else {
	                    	add(playerBullet);
	                        playerBullet.setLocation(player.getX(), player.getY());
	 
	                    } 

	
	                    bulletThread = new BulletThread(playerBullet);
	                    bulletThread.start();
	                }
					
					break;
			}
	       

		}
	}
	
// 플레이어 총알스레드
	private class BulletThread extends Thread {

		private PlayerBullet bullet;
		
		public BulletThread(PlayerBullet bullet) {
			this.bullet = bullet;
			music.playAudio("shoot");

			//add(bullet);
		}
	    @Override
	    public void run() {
	    	while (bullet.getY() >0) {
	    		add(bullet);
	            bullet.setLocation(bullet.getX(), bullet.getY() - 10);
	            for (Iterator<Enemy> iterator = enemyArr.iterator(); iterator.hasNext();) {
	        		
	                Enemy enemy = iterator.next();
	                if (bullet.getBounds().intersects(enemy.getBounds())) {

	                    handleCollision(enemy, iterator);
	                    remove(bullet);
                    
	                    return; 
	                }
	            }
	            repaint();
		        
		        try {
		            Thread.sleep(10);
		        } catch (InterruptedException e) {
		        	e.printStackTrace(); 
		            return;
		        }
		    }
	    	remove(bullet);

	    }
	    private void handleCollision(Enemy enemy, Iterator it) {
	        int currentLife = enemy.getLife();
	        if (currentLife > 0) {
	        	enemy.setLife(currentLife - 1);
	            if(enemy.getLife() == 0) {
	            	music.playAudio("die");

	            	remove(enemy);
		        	it.remove();
	            }
	        }
	        bullet.setY(player.getY());
	        bullet.setLocation(player.getX(), player.getY());
	        

	    }
	
	}
	
    public void stopGame() {
        GameManagement.stopFlag = true;
    	for(Enemy enemy:enemyArr) {
    		enemy.setStopFlag();
    	}
        if(itemTh.stopFlag == false) {itemTh.setStopFlag();}
        if(enemyShootTh.stopFlag ==false) {enemyShootTh.setStopFlag();}
        this.waitFlag();
        

    }
    public synchronized void resumeGame() {
    	GameManagement.stopFlag = false;
    	for(Enemy enemy:enemyArr) {
    		enemy.resumeFlag();
    	}
    	itemTh.resumeFlag();
    	enemyShootTh.resumeFlag();
        this.notify();
    }
    // 스레드 대기
	synchronized public void waitFlag() {
		
		try { this.wait(); } 
		catch (InterruptedException e) { } 
		
	}


// GamePanel 스레드 동작 
//    public synchronized void run() {
//    	if(stopFlag == true) {
//    		stopGame();
//    	}
//        if (GameManagement.life == 0) {
//            itemTh.interrupt();
//            enemyShootTh.interrupt();
//        	for(Enemy enemy:enemyArr) {
//        		enemy.waitFlag();
//        	}
//        	this.waitFlag();
//
//        }
//    }	

// enemy가 총알 발사하는 스레드
class EnemyShootThread extends Thread {
    private GamePanel gamePanel;
    private Vector<Enemy> enemyArr;
    private Bullet bullet;
    private Player player;
    private JLabel lifeLabel;
    private ImageIcon bulletIcon = new ImageIcon("image/enemy_bullet.png");
    private GameInfoPanel gameInfoPanel;
    private Vector<ShieldBlock> shieldBlockArr;
    
	private boolean stopFlag = false;
	private boolean getStopFlag() {return stopFlag;}
	private void setStopFlag() {
		if(stopFlag==true) {
			stopFlag = false;
		} else {
			stopFlag = true;
		}
	}

    public EnemyShootThread(GamePanel gamePanel, Vector<Enemy> enemyArr, Player player, JLabel lifeLabel, GameInfoPanel gameInfoPanel, Vector<ShieldBlock> shieldBlockArr) {
        this.gamePanel = gamePanel;
        this.enemyArr = enemyArr;
        this.player = player;
        this.lifeLabel = lifeLabel;
        this.gameInfoPanel = gameInfoPanel;
        this.shieldBlockArr = shieldBlockArr;
        //this.bullet = new Bullet(0, 0, 40, 43, bulletIcon); // bullet 초기 위치 설정
    }
    
    public void setBullet() {
    	int r = (int)(Math.random()*2+2);
    	for(int i = 0; i<r; i++) {
          int enemyNum = (int) (Math.random() * enemyArr.size());
          Enemy randomEnemy = enemyArr.get(enemyNum);
//          if(randomEnemy.getType().equals("LeftRight")||randomEnemy.getType().equals("RightLeft") || randomEnemy.getType().equals("Free")) {
//        	  Bullet bullet = new Bullet(randomEnemy.getX(), randomEnemy.getY(), 40,43, bulletIcon, gamePanel, player, shieldBlockArr, gameInfoPanel);
////        	  if(GameManagement.stopFlag == true) {
////        		  bullet.setStopFlag();
////        	  }
//          }
//          else i--;
    	} 
    	
    }
    // 스레드 대기
	synchronized private void waitFlag() {
		
		try { this.wait(); } 
		catch (InterruptedException e) { } 
		
	}
    // 스레드 깨우기
	synchronized public void resumeFlag() { 
		
		GameManagement.stopFlag = false;
		stopFlag = false;
		this.notify(); 
		
	}

    @Override
    synchronized public void run() {
        while (true) {
        	if(stopFlag == true) waitFlag();
        	setBullet();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
 // 아이템 스레드
class ItemThread extends Thread {
	private GamePanel gamePanel;
	private Vector<Label> itemArr;
	private Vector<Label> activeItem = new Vector<Label>();
	private boolean stopFlag = false;
	private boolean getStopFlag() {return stopFlag;}
	private void setStopFlag() {
		if(stopFlag==true) {
			stopFlag = false;
		} else {
			stopFlag = true;
		}
	}
	
	public ItemThread(GamePanel gamePanel, Vector<Label> itemArr) {
		this.gamePanel = gamePanel;
		this.itemArr = itemArr;
	}
	public void moveItem(Label itemLabel, int r) {
		int initialX = itemLabel.getX();
		int randomX = (int)(Math.random()*gamePanel.getWidth());
		int randomY = (int)(Math.random()*301+100);
		itemLabel.setLocation(randomX,randomY);
		itemLabel.setXY(randomX, randomY);
		gamePanel.add(itemLabel);
        while (itemLabel.getY()<= gamePanel.getHeight()) { // 아이템이 패널의 높이에 도달할 때까지 떨어진다
			
			itemLabel.setXY(itemLabel.getX(), itemLabel.getY()+10);
			//

			gamePanel.repaint();
	        try {
	            Thread.sleep(30);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

        }
        gamePanel.remove(itemLabel);
        itemLabel.setX(initialX);
	}
    // 스레드 대기
	synchronized private void waitFlag() {
		
		try { this.wait(); } 
		catch (InterruptedException e) { } 
		
	}
    // 스레드 깨우기
	synchronized public void resumeFlag() { 
		
		GameManagement.stopFlag = false;
		stopFlag = false;
		this.notify(); 
		
	}

	@Override
	synchronized public void run() {
		while(true) {
			if(stopFlag == true) waitFlag();
			int r = (int)(Math.random()*10+1);
			Label itemLabel;
			// 40%확률로 아이템 생성
			if(r>=1 && r<5) {
				int random = (int)(Math.random()*itemArr.size());
				itemLabel = itemArr.get(random);
				moveItem(itemLabel,random);
			}
			
	        try {
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
	}
}
}



// 클래스들
class ShieldBlock extends JLabel {
	Image img;
	private String type;
	public ShieldBlock(int x, int y, int w, int h, ImageIcon icon, String type) {

		this.setIcon(icon);
		this.setBounds(x,y,w,h);
		this.type = type;
		img = icon.getImage();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public String getType() {return type;}
}
class Player extends JLabel {
	Image img;
	private int x,y,w,h;
	private int life;
	private ImageIcon icon;
	public Player(int x, int y, int w, int h, int life,ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.icon = icon;
		this.setIcon(icon);
		this.setBounds(this.getX(), y,w,h);
		this.life = life;
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public String getIconText() {return icon.getDescription();}
	public int getX() {return x;}
	public int getY() {return y;}
	public void setX(int newX) {x = newX;}
	public void setXY(int newX, int newY) {x = newX; y = newY;}

	public int getLife() {return life;}
	public void setLife(int newLife) {life = newLife;}

}
class PlayerBullet extends JLabel {
	Image img;
	public int y;
	public PlayerBullet(int x, int y, int w, int h, ImageIcon icon) {
		this.setIcon(icon);
		this.setBounds(this.getX(), y,w,h);
		this.y = y;
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public void setY(int newY) {y =newY;}
	

}