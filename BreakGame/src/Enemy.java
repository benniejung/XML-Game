import javax.swing.ImageIcon;

public class Enemy extends Label{
	private int life;

	public Enemy(int x, int y, int w, int h, int life, ImageIcon icon) {
		super(x, y, w, h, icon);
		this.life = life;
		
	}

}
