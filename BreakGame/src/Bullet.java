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
	private Player player;
	private ArrayList<ShieldBlock> shieldBlockArr;
	public Bullet(int x, int y, int w, int h, ImageIcon icon, GamePanel gamePanel, Player player, ArrayList shieldBlockArr) {

		this.setBounds(x, y,w,h);
		this.y = y;
		img = icon.getImage();
		this.gamePanel = gamePanel;
		this.player = player;
		this.shieldBlockArr = shieldBlockArr;
		gamePanel.add(this);
		Thread th = new Thread(this);
		th.start();

	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public void setY(int newY) {y =newY;}
	
	@Override
	public void run() {
		while(this.getY()<850) {
			
			this.setLocation(this.getX(), this.getY() + 10);
			
            // 총알이 player에 닿았을 때
            if (this.getBounds().intersects(player.getBounds())) {
            	int newLife = player.getLife()-1;
            	gamePanel.lifeLabel.setText(Integer.toString(newLife));
                player.setLife(newLife);
                
                gamePanel.remove(this);
               //  생명이 0일 때 게임 종료 추가해야함!!
                return; 
            }
            for (Iterator<ShieldBlock> iterator = shieldBlockArr.iterator(); iterator.hasNext();) {
            	ShieldBlock shieldBlock = iterator.next();
                if (this.getBounds().intersects(shieldBlock.getBounds())) {
                    
                    //handleCollision(shieldBlock, iterator);
                    gamePanel.remove(shieldBlock);
                    gamePanel.remove(this);
                    shieldBlockArr.remove(shieldBlock);
                
                    return; 
                }
            }
  
			try {
				Thread.sleep(20);
							
			} catch (InterruptedException e) {
				System.out.println("stop");
				return;
			}
			gamePanel.repaint(); // 설정된 총알의 위치대로 총알을 그린다
		}
		gamePanel.remove(this);
	}
}
