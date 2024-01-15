import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Enemy extends JLabel implements Runnable{
	Image img;
	private int x, y, w, h, life,speed;
	String type;
	boolean crash = false;
	String crashType = "";
	public Enemy(int x, int y, int w, int h, String type, int life, int speed,ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.setBounds(x,y,w,h);
		this.life = life;
		this.speed = speed;
		this.type = type;
		img = icon.getImage();
		
		Thread th = new Thread(this);
		th.start();
		


	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	public int getX() {return x;}

	public int getY() {return y;}

	public void setX(int newX) {x = newX;}

	public void setY(int newY) {y = newY;}

	public void setXY(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public int getLife() {return life;}
	public int getSpeed() {return speed;}
	public String getType() {return type;}
	//public int getType() {return type;}
	
	public void setLife(int newLife) {life = newLife;} 
	
	// 왼쪽 또는 오른쪽 충돌 확인 메소드
	public void crashCheck() {
		if (x <= 0) {
			crash = true;
			crashType = "left-crash";
		}

		else if(x + w >= 800) {
			crash = true;
			crashType = "right-crash";

		}
		else {
			crash = false;
		}


	}

	public void process() {
		switch(type) {
		case "Not Moving":
			break;
		case "LeftRight":
			crashCheck();
			if(crash) {
				crash = false; 
				int newX;
        		switch(crashType) {
        		case "left-crash":
        			newX = x + speed;
        			this.setLocation(newX, y);
        			this.setXY(newX, y);

        			break;
        		case "right-crash":
        			newX = x - speed;
        			this.setLocation(newX, y);
        			this.setXY(newX, y);

        			break;
        		}
        		
			}
			 else {
				 if(crashType.equals("left-crash")) {
	        			int newX = x + speed;
	        			this.setLocation(newX, y);
	        			this.setXY(newX, y);


				 }
				 else if(crashType.equals("right-crash")) {
	        			int newX = x - speed;
	        			this.setLocation(newX, y);
	        			this.setXY(newX, y);


				 }
				 else {
	        			int newX = x - speed;
	        			this.setLocation(newX, y);
	        			this.setXY(newX, y);
	        			

				 }
				 
	         }
			break;
		case "Free":
			int directionX = (int)(Math.random()*2 +1);
			int directionY = (int)(Math.random()*2 +1);
			int ranX = (int)(Math.random()*40);
			int ranY = (int)(Math.random()*40);
			
			// +x, +y일 경우
			if(directionX == 1 && directionY == 1) {
				this.setLocation(this.getX() + ranX, this.getY() + ranY);
				int newX = this.getX() + ranX;
				int newY = this.getY() + ranY;

				this.setXY(newX, newY);
			}
			// +x, -y
			else if(directionX == 1 && directionY == 2) {
				this.setLocation(this.getX() + ranX, this.getY() - ranY);
				int newX = this.getX() + ranX;
				int newY = this.getY() - ranY;

				this.setXY(newX, newY);

			}
			// -x, -y
			else if(directionX == 2 && directionY == 2) {
				this.setLocation(this.getX() - ranX, this.getY() - ranY);
				int newX = this.getX() - ranX;
				int newY = this.getY() - ranY;

				this.setXY(newX, newY);

			}
			// -x, +y
			else if(directionX == 2 && directionY == 1) {
				this.setLocation(this.getX() - ranX, this.getY() + ranY);
				int newX = this.getX() - ranX;
				int newY = this.getY() + ranY;

				this.setXY(newX, newY);

			}			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("stop");
				e.printStackTrace();  // 예외 로그 출력
				return;
			}
			break;

		
			
		}
	}
	@Override
	public void run() {
		while(true) {
			process();
			repaint();
			try {
				Thread.sleep(20);
							
			} catch (InterruptedException e) {
				System.out.println("stop");
				return;
			}

		}
		
	}

}
