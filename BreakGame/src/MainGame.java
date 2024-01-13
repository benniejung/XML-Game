import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
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
		
	    gamePanel.requestFocus();
		createGameMenu();
		setResizable(false); // 크기 고정
		setVisible(true);
		
  
		
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

class GamePanel extends JPanel  {
	// 노드 변수들
	private Player player;
	private Meteor meteor;
	private Enemy enemy;
	private Label item;
	private ShieldBlock shieldBlock;
	private JLabel lifeTextLabel = new JLabel(new ImageIcon("image/life.png"));
	private JLabel lifeLabel;
	
	
	ArrayList<Label> itemArr = new ArrayList<Label>();
	ArrayList<Enemy> enemyArr = new ArrayList<Enemy>();
	boolean flag = true;
	ImageIcon bgImg;
	
	private Bullet bullet;
	MoveEnemyThread moveEnemyTh;
	EnemyShootThread enemyShootTh;
	ItemThread itemTh;
	
	
	public GamePanel(Node gamePanelNode) {
		setLayout(null);
		setFocusable(true);
		addKeyListener(new GameKeyListener());
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
				int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				player = new Player(x,y,w,h, life, icon);
				add(player);
			}
		}
		
		// 생명 레이블
		lifeTextLabel.setBounds(612, 24, 66, 21);
		add(lifeTextLabel);
		lifeLabel = new JLabel(Integer.toString(player.getLife()));
		lifeLabel.setForeground(Color.white);
		lifeLabel.setFont(new Font("Orbitron", Font.PLAIN, 25));
		lifeLabel.setBounds(695, 24, 45, 21);
		add(lifeLabel);

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
				int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
				int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
				int type = Integer.parseInt(XMLReader.getAttr(node, "type"));

				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				enemy = new Enemy(x,y,w,h,type,life,speed, icon);
				enemyArr.add(enemy);
				add(enemy);
				
			}
		}
		// ShieldBlock 노드
		Node shieldBlocksNode = XMLReader.getNode(activeScreenNode, XMLReader.E_SHIELDBLOCKS);
		NodeList shieldBlocksNodeList = shieldBlocksNode.getChildNodes();
		for(int i=0; i<shieldBlocksNodeList.getLength(); i++) {
			Node node = shieldBlocksNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_SHIELDBLOCK)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				System.out.println(x+y);
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				shieldBlock = new ShieldBlock(x,y,w,h,icon);
		        add(shieldBlock);

				
				
			}
		}

		// 아이템 노드
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
		
	
		

		
		// 스레드 생성
		moveEnemyTh = new MoveEnemyThread(this,enemyArr);
		moveEnemyTh.start();
		
		enemyShootTh = new EnemyShootThread(this,enemyArr, player,lifeLabel);
		enemyShootTh.start();
		
		itemTh = new ItemThread(this, itemArr);
		itemTh.start();
	
	}

	public void paintComponent(Graphics g) {
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
	public ArrayList<Label> getItemArr() {
        return itemArr;
    }

    public void addItem(Label item) {
        itemArr.add(item);
    }
	// 키 이벤트
	public class GameKeyListener extends KeyAdapter {
		private BulletThread bulletThread;
		ImageIcon bulletIcon = new ImageIcon("image/bullet.png");
		Image bullet = bulletIcon.getImage();
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
					Bullet bullet = new Bullet(0,0,45,30, bulletIcon);
	
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
		            Thread.sleep(50);
		        } catch (InterruptedException e) {
		        	e.printStackTrace(); 
		            return;
		        }
		    }
	    }
	    private void handleCollision(Enemy enemy, Iterator it) {
	        int currentLife = enemy.getLife();
	        if (currentLife > 0) {
	        	enemy.setLife(currentLife - 1);
	            if(enemy.getLife() == 0) {
	            	remove(enemy);
		        	it.remove();
	            }
	            System.out.println(enemy.getLife());
	        }
	        bullet.setY(player.getY());
	        bullet.setLocation(player.getX(), player.getY());
	        

	    }
	
	}
	

// 적이 움직이는 스레드
class MoveEnemyThread extends Thread {
	private GamePanel gamePanel;
	private Enemy enemy;
	boolean crash = false;
	String crashType ="";
	ArrayList<Enemy> enemyArr;
	public MoveEnemyThread(GamePanel gamePanel, ArrayList<Enemy> arr) {
		this.gamePanel = gamePanel;
		this.enemyArr = arr;
	}
	// 왼쪽 또는 오른쪽 충돌 확인 메소드
	public void crashCheck(Enemy enemy) {
		if (enemy.getX() <= 0) {
			crash = true;
			crashType = "left-crash";
		}

		else if(enemy.getX() + enemy.getWidth() >= gamePanel.getWidth()) {
			crash = true;
			crashType = "right-crash";

		}
		else {
			crash = false;
		}


	}
	
	public void moveEnemy1(Enemy enemy) {
		while(true) {
	        if(player.getX() < enemy.getX()) { //아바타가 왼쪽에 있으면
	            enemy.setX(enemy.getX() - 5);
	            enemy.setLocation(enemy.getX(), enemy.getY());
	        } else if (player.getX() > enemy.getX()) {//아바타가 오른쪽에 있으면
	        	enemy.setX(enemy.getX() + 5);
	        	enemy.setLocation(enemy.getX(), enemy.getY());
	        }

	        if (player.getY() < enemy.getY()) {//아바타가 위쪽에 있으면
	        	enemy.setY(enemy.getY() - 5);
	        	enemy.setLocation(enemy.getX(), enemy.getY());
	        } else if (player.getY() > enemy.getY()) {//아바타가 아래쪽에 있으면
	        	enemy.setY(enemy.getY() + 5);
	        	enemy.setLocation(enemy.getX(), enemy.getY());
	        }
	        
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace(); // 예외 로그 출력
				return;
			}


		}

	}
	@Override
	public void run() {
			while(true) {
				for(Enemy enemy : enemyArr) {

				
					if(enemy.getType() == 2) {
						
						crashCheck(enemy);
						if(crash) {
							int newX;
			        		switch(crashType) {
			        		case "left-crash":
			        			newX = enemy.getX() + enemy.getSpeed();
			        			enemy.setLocation(newX, enemy.getY());
			        			enemy.setXY(newX, enemy.getY());
			        			System.out.println(enemy.getX());
			        			

			        			break;
			        		case "right-crash":
			        			newX = enemy.getX() - enemy.getSpeed();
			        			enemy.setLocation(newX, enemy.getY());
			        			enemy.setXY(newX, enemy.getY());

			        			break;
			        		}
			        		crash = false; 
						}
						 else {
							 if(crashType.equals("left-crash")) {
				        			int newX = enemy.getX() + enemy.getSpeed();
				        			enemy.setLocation(newX, enemy.getY());
				        			enemy.setXY(newX, enemy.getY());


							 }
							 else if(crashType.equals("right-crash")) {
				        			int newX = enemy.getX() - enemy.getSpeed();
				        			enemy.setLocation(newX, enemy.getY());
				        			enemy.setXY(newX, enemy.getY());
		

							 }
							 else {
				        			int newX = enemy.getX() - enemy.getSpeed();
				        			enemy.setLocation(newX, enemy.getY());
				        			enemy.setXY(newX, enemy.getY());

							 }
							 
				         }
				}

			}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace(); // 예외 로그 출력
					return;
				}

		}
			




}
}

// enemy가 총알 발사하는 스레드
class EnemyShootThread extends Thread {
    private GamePanel gamePanel;
    private ArrayList<Enemy> enemyArr;
    private Bullet bullet;
    private Player player;
    private JLabel lifeLabel;
    private ImageIcon bulletIcon = new ImageIcon("image/enemy_bullet.png");

    public EnemyShootThread(GamePanel gamePanel, ArrayList<Enemy> enemyArr, Player player, JLabel lifeLabel) {
        this.gamePanel = gamePanel;
        this.enemyArr = enemyArr;
        this.player = player;
        this.lifeLabel = lifeLabel;
        this.bullet = new Bullet(0, 0, 40, 43, bulletIcon); // bullet 초기 위치 설정
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        timer.schedule(new EnemyShootTask(), 0, 1500); // 1.5초 동안 적이 총알을 발사한다
    }

	    private class EnemyShootTask extends TimerTask {
	        @Override
	        public void run() {
                int r = (int) (Math.random() * enemyArr.size());
                Enemy randomEnemy = enemyArr.get(r);
                int type = randomEnemy.getType();

                if (type == 1) {
                    return;
                } else { // type이 2인 적만 총알을 발사한다
                    bullet.setLocation(randomEnemy.getX(), randomEnemy.getY()); // 처음에는 선택된 적의 위치에서 발사
                    gamePanel.add(bullet); // 화면에 붙인다
                	
                    BulletMoveThread bulletMoveThread = new BulletMoveThread(gamePanel, bullet, player, lifeLabel); // 총알 스레드 시작
                    bulletMoveThread.start();

              	}
       
        	}

	}
}
// 적이 발산한 총알이 움직이는 스레드
class BulletMoveThread extends Thread {
    private GamePanel gamePanel;
    private Bullet bullet;
    private Player player;
    private JLabel lifeLabel;

    public BulletMoveThread(GamePanel gamePanel, Bullet bullet, Player player, JLabel lifeLabel) {
        this.gamePanel = gamePanel;
        this.bullet = bullet;
        this.player = player;
        this.lifeLabel = lifeLabel;
    }

    @Override
    public void run() {
        while (bullet.getY() <= gamePanel.getHeight()) { // 총알이 패널의 높이에 도달할 때까지 발사한다
            bullet.setLocation(bullet.getX(), bullet.getY() + 10);
            // 총알이 player에 닿았을 때
            if (bullet.getBounds().intersects(player.getBounds())) {
            	int newLife = player.getLife()-1;
            	lifeLabel.setText(Integer.toString(newLife));
                player.setLife(newLife);
                
                // 생명이 0일 때 게임 종료 추가해야함!!
                return; 
            }
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gamePanel.repaint(); // 설정된 총알의 위치대로 총알을 그린다
        }

        //gamePanel.remove(bullet); // 원래있던 총알을 지우고
        
    }
 }

// 아이템 스레드
class ItemThread extends Thread {
	private GamePanel gamePanel;
	private ArrayList<Label> itemArr;
	private ArrayList<Label> activeItem = new ArrayList<Label>();
	public ItemThread(GamePanel gamePanel, ArrayList<Label> itemArr) {
		this.gamePanel = gamePanel;
		this.itemArr = itemArr;
	}
	public void moveItem(Label itemLabel) {
		int initialX = itemLabel.getX();

		System.out.println(itemArr.get(0).getX());
        while (itemLabel.getX()>0) { // 총알이 패널의 높이에 도달할 때까지 발사한다
			gamePanel.add(itemLabel);
			
			itemLabel.setXY(itemLabel.getX()-10, itemLabel.getY());
			gamePanel.repaint();
	        try {
	            Thread.sleep(100);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

        }
        gamePanel.remove(itemLabel);
        itemLabel.setX(initialX);
	}
	@Override
	public void run() {
		while(true) {
			int r = (int)(Math.random()*10+1);
			Label itemLabel;
			// 40%확률로 아이템 생성
			if(r>=3 && r<5) {
				itemLabel = itemArr.get(0);
				moveItem(itemLabel);
			}
			else if(r>=1 && r<3) {
				itemLabel = itemArr.get(1);
				moveItem(itemLabel);
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
	private int x,y,w,h;
	public ShieldBlock(int x, int y, int w, int h, ImageIcon icon) {
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

}
class Player extends JLabel {
	Image img;
	private int x,y,w,h;
	private int life;
	public Player(int x, int y, int w, int h, int life,ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.setIcon(icon);
		this.setBounds(this.getX(), y,w,h);
		this.life = life;
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public int getX() {return x;}
	public int getY() {return y;}
	public void setX(int newX) {x = newX;}
	public void setXY(int newX, int newY) {x = newX; y = newY;}

	public int getLife() {return life;}
	public void setLife(int newLife) {life = newLife;}

}
class Bullet extends JLabel {
	Image img;
	public int y;
	public Bullet(int x, int y, int w, int h, ImageIcon icon) {
		this.setIcon(icon);
		this.setBounds(this.getX(), y,w,h);
		this.y = y;
		img = icon.getImage();
	}
	
	public void setY(int newY) {y =newY;}
	

}