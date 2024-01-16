import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameInfoPanel extends JPanel {
	private JLabel scoreLabel, lifeLabel;
	int life = GameManagement.life;
	public GameInfoPanel() {
		this.setLayout(null);

 	    JLabel stageTextLabel = new JLabel("STAGE");
		stageTextLabel.setBounds(15,15,195,25);
		stageTextLabel.setFont(new Font("Orbitron", Font.PLAIN, 25));
		
		JLabel scoreIconLabel = new JLabel(new ImageIcon("image/score.png"));
		scoreIconLabel.setBounds(300,0,50,50);
		scoreLabel = new JLabel("0");
		scoreLabel.setBounds(360,0,50,50);
		scoreLabel.setFont(new Font("Orbitron", Font.PLAIN, 25));
		
		JLabel lifeIconLabel = new JLabel(new ImageIcon("image/life.png"));
		lifeIconLabel.setBounds(500,0,50,50);
		lifeLabel = new JLabel(Integer.toString(life));
		lifeLabel.setBounds(560,0,50,50);
		lifeLabel.setFont(new Font("Orbitron", Font.PLAIN, 25));
 

		
		this.add(stageTextLabel);
		this.add(scoreIconLabel);
		this.add(lifeIconLabel);
		this.add(lifeLabel);
		this.add(scoreLabel);
		
	}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(226, 221, 208));  // Set the background color here
    }
	
    public JLabel getLifeLabel() {return lifeLabel;}
    public void setLifeLabel(int newLife) { 
    	String life = Integer.toString(newLife);
    	lifeLabel.setText(life);
    }

}
