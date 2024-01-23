package AuthoringTool;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SoundPanel extends JPanel{
	DrawPanel drawPanel;
	public SoundPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		
		this.setLayout(new FlowLayout());
		
		//라디오버튼 생성
		JRadioButton radioBtn1 = new JRadioButton("music/background1.wav");
		JRadioButton radioBtn2 = new JRadioButton("music/background2.wav");
		
		//라디오버튼 그룹 생성
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(radioBtn1);
		buttonGroup.add(radioBtn2);
		
		add(radioBtn1);
		add(radioBtn2);
	}
}
