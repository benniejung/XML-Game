import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bullet extends JLabel implements Runnable{
	Image img;
	public int y;
	private GamePanel gamePanel;
	private GameInfoPanel gameInfoPanel;
	private Player player;
	private ArrayList<ShieldBlock> shieldBlockArr;
	JLabel lifeLabel;
	private Thread bulletThread;
	private boolean stopFlag = false;
	private boolean getStopFlag() {return stopFlag;}
	public void setStopFlag() {stopFlag = true;}

	public Bullet(int x, int y, int w, int h, ImageIcon icon, GamePanel gamePanel, Player player, ArrayList shieldBlockArr, GameInfoPanel gameInfoPanel) {

		this.setBounds(x, y,w,h);
		this.y = y;
		img = icon.getImage();
		this.gamePanel = gamePanel;
		this.player = player;
		this.shieldBlockArr = shieldBlockArr;
		this.gameInfoPanel = gameInfoPanel;
		
		gamePanel.add(this);
		Thread th = new Thread(this);
		th.start();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public void setY(int newY) {y =newY;}
	
    // 스레드 대기
	synchronized private void waitFlag() {
		
		try { this.wait(); } 
		catch (InterruptedException e) { } 
		
	}
    // 스레드 깨우기
	synchronized public void resumeFlag() { 
		stopFlag = false;
		GameManagement.stopFlag = false;
		this.notify(); 
		
	}

	@Override
	public void run() {
		while(this.getY()<850) {
			if(stopFlag == true) waitFlag();
			this.setLocation(this.getX(), this.getY() + 10);
			
            // 총알이 player에 닿았을 때
            if (this.getBounds().intersects(player.getBounds())) {
            	System.out.println(GameManagement.life);
            	int newLife = GameManagement.life -1;
            	gameInfoPanel.setLifeLabel(newLife);
            	GameManagement.life = newLife;
                gamePanel.remove(this);
//                //  생명이 0일 때 게임 종료 추가해야함!!
//                if(player.getLife() == 0) {
//                	bulletThread.interrupt();
//                }
                return;
            }
            Iterator<ShieldBlock> iterator = shieldBlockArr.iterator();
            while (iterator.hasNext()) {
                ShieldBlock shieldBlock = iterator.next();
                if(shieldBlock.getType().equals("breakable")) {
                    if (this.getBounds().intersects(shieldBlock.getBounds())) {
                        iterator.remove(); // Use iterator's remove method
                        gamePanel.remove(shieldBlock);
                        gamePanel.remove(this);
                        return;
                    }

                } else {
                    if (this.getBounds().intersects(shieldBlock.getBounds())) {
                        gamePanel.remove(this);
                        return;
                    }

                }
            }
			try { 
				Thread.sleep(30);
							
			} catch (InterruptedException e) {
				System.out.println("stop");
				return;
			}
			gamePanel.repaint(); // 설정된 총알의 위치대로 총알을 그린다
		}
		gamePanel.remove(this);
	}
}
