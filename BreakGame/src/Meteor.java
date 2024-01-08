import javax.swing.ImageIcon;

public class Meteor extends Label implements Runnable {
	private int life, speed;
	boolean flag = true;
	boolean crash = false;

	public Meteor(int x, int y, int w, int h, int life, int speed, ImageIcon icon) {
		super(x, y, w, h, icon);
		this.life = life;
		this.speed = speed;
		
//		Thread th = new Thread(this);
//		th.run();
		
	}
	public int getLife() {return life;}
	public int getSpeed() {return speed;}
	
	@Override
	public void run() {
		while(flag) {
			//move();
		}
	}
	public void move() {
	}
	public void isCrashCheck() {
		
	}
}
