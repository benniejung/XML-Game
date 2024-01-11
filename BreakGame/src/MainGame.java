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
	private JLabel lifeTextLabel = new JLabel(new ImageIcon("image/life.png"));
	
	
	ArrayList<Enemy> enemyArr = new ArrayList<Enemy>();
	boolean flag = true;
	boolean crash = false;
	String crashType;
	ImageIcon bgImg;
	
	private Bullet bullet;
	MoveEnemyThread moveEnemyTh;
	EnemyShootThread enemyShootTh;
	
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
		JLabel lifeLabel = new JLabel(Integer.toString(player.getLife()));
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

		

		
		// 스레드 생성
		moveEnemyTh = new MoveEnemyThread(this,enemyArr);
		moveEnemyTh.start();
		
		enemyShootTh = new EnemyShootThread(this,enemyArr, player,lifeLabel);
		enemyShootTh.start();
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
	public void paintComponent(Graphics g) {
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
}

// 적이 움직이는 스레드
class MoveEnemyThread extends Thread {
	private GamePanel gamePanel;
	private Enemy enemy;
	boolean crash = false;
	String crashType;
	ArrayList<Enemy> enemyArr;
	public MoveEnemyThread(GamePanel gamePanel, ArrayList<Enemy> arr) {
		this.gamePanel = gamePanel;
		this.enemyArr = arr;
	}
	// 왼쪽 또는 오른쪽 충돌 확인 메소드
	public void crashCheck(int x, int y) {
		if (x >= 0 && x + gamePanel.getWidth() >= 800 &&
                y >= 0 && y + gamePanel.getHeight() >= 550) {
			crash = true;
		}
		else crash = false;

	}
	
	@Override
	public void run() {
		while(true) {
			for(Enemy enemy : enemyArr) {
				if(enemy.getType() == 2) {
				     int direction = (int) (Math.random() * 4+1);
		             
		             
		             int x = enemy.getX();
		             int y = enemy.getY();

					// 위
					if (direction == 1) {
						int newY = y - enemy.getSpeed();
						if (newY >= 0) {
							enemy.setLocation(x, newY);
							enemy.setXY(x, newY);
						}

					}
					// 아래
					else if (direction == 2) {
						int newY = y + enemy.getSpeed();
						if (newY <= 600) {
							enemy.setLocation(x, newY);
							enemy.setXY(x, newY);
						}
					}
					// 왼쪽
					else if (direction == 3) {
						int newX = x - enemy.getSpeed();
						if (newX >= 0) {
							enemy.setLocation(newX, y);
							enemy.setXY(newX, y);
						}
					}
					// 오른쪽
					else if (direction == 4) {
						int newX = x + enemy.getSpeed();
						if (newX <= gamePanel.getWidth() - enemy.getWidth()) {
							enemy.setLocation(newX, y);
							enemy.setXY(newX, y);
						}
					}
				}
					
			}

	        try {
            Thread.sleep(450);
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
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gamePanel.remove(bullet); // 원래있던 총알을 지우고
        gamePanel.repaint(); // 설정된 총알의 위치대로 총알을 그린다
    }
}

// 클래스들
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
