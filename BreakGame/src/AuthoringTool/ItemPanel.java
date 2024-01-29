package AuthoringTool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import AuthoringTool.PlayerPanel.OpenFileEvent;
import AuthoringTool.PlayerPanel.actionEvent;
import AuthoringTool.PlayerPanel.settingKeyListener;

public class ItemPanel extends JPanel{
	DrawPanel drawPanel;
	JPanel lifeImgsPanel = new JPanel();
	JPanel shieldImgsPanel = new JPanel();
	private String[] lifeImgs = {"image/heart.png", "image/player2.png"};
	private String[] shieldImgs = {"image/shield.png"};
	JButton lifeImgButton, shieldImgButton;
	
	// 속성 레이블
	private JLabel xLabel, yLabel, wLabel, hLabel, lifeLabel;
	// 속성 텍스트필드
	static JTextField xTextField, yTextField, wTextField,hTextField,lifeTextField;
	// 선택한 이미지 경로
	String imagePath;
	PolygonObj rect;
	// 선택한 패널 저장변수
	static String imgPanelName = null;
	boolean chooseImg = false;
	public ItemPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		this.setLayout(null);
		this.setSize(this.getWidth(), this.getHeight());
		
		JLabel lifeItemTextLabel = new JLabel("생명 아이템");
		JLabel shieldItemTextLabel = new JLabel("보호 아이템");
		
		lifeItemTextLabel.setBounds(10,10,200,30);
		shieldItemTextLabel.setBounds(10,240,200,30);
		
		this.add(lifeItemTextLabel);
		this.add(shieldItemTextLabel);
		
		//이미지 나열 패널을 중간에 추가
		lifeImgsPanel.setSize(300,300);
		lifeImgsPanel.setLocation(0, 20);
		lifeImgsPanel.setLayout(new GridLayout(1,0,10,10));
		for(int i =0; i<lifeImgs.length; i++) {
			ImageIcon icon = new ImageIcon(lifeImgs[i]);
			Image img = icon.getImage();
			Image updateImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 이미지 버튼크기에 맞게 조절
			ImageIcon updateIcon = new ImageIcon(updateImg);
			updateIcon.setDescription(lifeImgs[i]);
			
			lifeImgButton = new JButton(updateIcon);
			lifeImgButton.setPreferredSize(new Dimension(50, 50));
			lifeImgsPanel.add(lifeImgButton);
			lifeImgButton.addActionListener(new actionEvent(lifeImgsPanel, "lifeImgsPanel"));
		}
		// 이미지 스크롤바 생성
		JScrollPane lifeImgsScrollPane = new JScrollPane(lifeImgsPanel);
		lifeImgsScrollPane.setBounds(0, 50, 375, 150);
		lifeImgsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(lifeImgsScrollPane);
		// 이미지 추가버튼 생성
		JButton addImgButton1 = new JButton("이미지 추가");
		addImgButton1.setSize(100,30);
		addImgButton1.setLocation(250,220);
		this.add(addImgButton1);
		addImgButton1.addActionListener(new OpenFileEvent(lifeImgsPanel));

		
		// 보호아이템 이미지 나열 패널을 중간에 추가
		shieldImgsPanel.setSize(300,300);
		shieldImgsPanel.setLocation(0, 20);
		shieldImgsPanel.setLayout(new GridLayout(1,0,10,10));
		for(int i =0; i<shieldImgs.length; i++) {
			ImageIcon icon = new ImageIcon(shieldImgs[i]);
			Image img = icon.getImage();
			Image updateImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 이미지 버튼크기에 맞게 조절
			ImageIcon updateIcon = new ImageIcon(updateImg);
			updateIcon.setDescription(shieldImgs[i]);
			
			shieldImgButton = new JButton(updateIcon);
			shieldImgButton.setPreferredSize(new Dimension(50, 50));
			shieldImgsPanel.add(shieldImgButton);
			shieldImgButton.addActionListener(new actionEvent(shieldImgsPanel, "shieldImgsPanel"));
		}
		// 보호아이템 이미지 스크롤바 생성
		JScrollPane shieldImgsScrollPane = new JScrollPane(shieldImgsPanel);
		shieldImgsScrollPane.setBounds(0, 270, 375, 150);
		shieldImgsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(shieldImgsScrollPane);
		// 보호아이템 이미지 추가버튼 생성
		JButton addImgButton2 = new JButton("이미지 추가");
		addImgButton2.setSize(100,30);
		addImgButton2.setLocation(250,430);
		this.add(addImgButton2);
		addImgButton2.addActionListener(new OpenFileEvent(shieldImgsPanel));
		
		// 속성 레이블 배치
		xLabel = new JLabel("X");
		xLabel.setBounds(30,500,20,20);
		xTextField = new JTextField(10);
		xTextField.setBounds(60,500,100,20);
		xTextField.addKeyListener(new settingKeyListener("xTextField"));
		
		yLabel = new JLabel("Y");
		yLabel.setBounds(200,500,20,20);
		yTextField = new JTextField(10);
		yTextField.setBounds(230,500,100,20);
		yTextField.addKeyListener(new settingKeyListener("yTextField"));
		
		wLabel = new JLabel("W");
		wLabel.setBounds(30,550,20,20);
		wTextField = new JTextField(10);
		wTextField.setBounds(60,550,100,20);
		wTextField.addKeyListener(new settingKeyListener("wTextField"));
		
		hLabel = new JLabel("H");
		hLabel.setBounds(200,550,20,20);
		hTextField = new JTextField(10);
		hTextField.setBounds(230,550,100,20);
		hTextField.addKeyListener(new settingKeyListener("hTextField"));

		add(xLabel);
		add(yLabel);
		add(wLabel);
		add(hLabel);

		add(xTextField);
		add(yTextField);
		add(wTextField);
		add(hTextField);


	}
	
	class actionEvent implements ActionListener {
		JPanel imgsPanel;
		String panelName;
		public actionEvent(JPanel imgsPanel, String panelName) {
			this.imgsPanel = imgsPanel;
			this.panelName = panelName;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			imgPanelName = panelName;
			System.out.println("선택한 이미지패널이름: " + imgPanelName);
			EnemyPanel.clickedBtn = (JButton)e.getSource();
			EnemyPanel.clickedBtn.setBorder(BorderFactory.createLineBorder(Color.red,5));
			// 기존에 선택한 버튼들의 Border를 제거
	        for (Component component : imgsPanel.getComponents()) {
	            if (component instanceof JButton && component != EnemyPanel.clickedBtn) {
	                ((JButton) component).setBorder(BorderFactory.createEmptyBorder());
	            }
	        }
	        // 선택한 이미지 경로 가져오기
	        if (EnemyPanel.clickedBtn.getIcon() instanceof ImageIcon) {
	            ImageIcon icon = (ImageIcon) EnemyPanel.clickedBtn.getIcon();
	            imagePath = icon.getDescription();
	            EnemyPanel.imagePath = imagePath;
	            System.out.println("Selected Image Path: " + imagePath);
	        }
	        chooseImg = true;
		}
		
	}

	// 이미지 추가버튼 눌렀을 때 이벤트
	class OpenFileEvent implements ActionListener {
		private JFileChooser chooser = new JFileChooser();
		JPanel imgsPanel;
		public OpenFileEvent(JPanel imgsPanel) {
			this.imgsPanel = imgsPanel;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png", "gif"); // 사진만 선택되도록
			chooser.setFileFilter(filter);
			// 파일을 선택하지 않았을 경우
			if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 선택해주세요", "Choose File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// 파일을 선택한 경우
			if (chooser.getSelectedFile() != null) {
				String filePath = chooser.getSelectedFile().getPath();
				ImageIcon icon = new ImageIcon(filePath);
				Image img = icon.getImage();
				Image updateImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 이미지 버튼크기에 맞게 조절
				JButton addImgButton = new JButton(new ImageIcon(updateImg));
				addImgButton.setPreferredSize(new Dimension(80,80));
				//addImgButton.addActionListener(new actionEvent());
				imgsPanel.add(addImgButton);
				
			}

		}
	}
	class settingKeyListener extends KeyAdapter{
		String changedSetting;
//		DrawPanel drawPanel;
		public settingKeyListener() {}
		public settingKeyListener(String changedSetting) {
			this.changedSetting = changedSetting;
		}
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_ENTER:
				//enemys.remove(clickedEnemy);
				if(changedSetting.equals("xTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int x = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("item")) {
						EnemyPanel.selectedItem.setLocation(x,EnemyPanel.selectedItem.getY());
						EnemyPanel.selectedItem.setX(x);
					
					}
				} else if(changedSetting.equals("yTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int y = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("item")) {
						EnemyPanel.selectedItem.setLocation(EnemyPanel.selectedItem.getX(),y);
						EnemyPanel.selectedItem.setY(y);
					
					}
				} else if(changedSetting.equals("wTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int w = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("item")) {
						EnemyPanel.selectedItem.setSize(w,EnemyPanel.selectedItem.getH());
						EnemyPanel.selectedItem.setW(w);
					
					}
				} else if(changedSetting.equals("hTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int h = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("item")) {
						EnemyPanel.selectedItem.setSize(EnemyPanel.selectedItem.getW(),h);
						EnemyPanel.selectedItem.setH(h);
					
					}
				} 
				drawPanel.remove(EnemyPanel.rect);
				drawPanel.repaint();

                break;
			case KeyEvent.VK_BACK_SPACE:
//				System.out.println("remove");
//				drawPanel.remove(rect);
//				drawPanel.remove(clickedPlayer);
//				PlayerObj.player.remove(clickedPlayer);
//				drawPanel.repaint();
				
				break;
			}
			
		}
	}

}
