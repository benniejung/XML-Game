package AuthoringTool;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import AuthoringTool.EnemyPanel.OpenFileEvent;
import AuthoringTool.EnemyPanel.settingKeyListener;

public class ToolTappedPane extends JTabbedPane{
	DrawPanel drawPanel;
	MainPanel mainPanel;
	public ToolTappedPane(MainPanel mainPanel,DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		this.mainPanel = mainPanel;
		
		this.addTab("Bg", new BackgroundPanel(mainPanel,drawPanel));
		this.addTab("Sound", new SoundPanel(drawPanel));
		this.addTab("Obj", new ObjPanel(drawPanel));
//		this.addTab("Enemy", new EnemyPanel(drawPanel));
//		this.addTab("Player", new PlayerPanel(drawPanel));
//		this.addTab("ShieldBlock", new ShieldBlockPanel(drawPanel));
//		this.addTab("Item", new ItemPanel());
	}
}

class BackgroundPanel extends JPanel {
	private JButton bgButton;
	private JButton selectedButton;
	private String[] imgs = {"image/background/bg1.jpg", "image/background/bg2.jpg","image/background/bg3.jpg","image/background/bg4.jpg","image/background/bg5.jpg"};
	int btnSize = 100;
	DrawPanel drawPanel;
	MainPanel mainPanel;
	boolean hasBorder = false;
	static boolean isBackgroundPanel = true;
	
	private JTextField xTextField, yTextField, wTextField, hTextField;
	public BackgroundPanel(MainPanel mainPanel,DrawPanel drawPanel) {
		this.setLayout(null);
		this.setSize(this.getWidth(), this.getHeight());
		this.drawPanel = drawPanel;
		this.mainPanel = mainPanel;
		setFocusable(true); // 키입력받기 위해 
		
		// 전체화면 크기조절 부분
		JLabel controlPanelSizeText = new JLabel("전체화면 위치 및 크기조절");
		JLabel xLabel = new JLabel("x");
		JLabel yLabel = new JLabel("y");
		JLabel wLabel = new JLabel("w");
		JLabel hLabel = new JLabel("h");
		
		controlPanelSizeText.setBounds(10,20,150,20);
		xLabel.setBounds(10,60,10,20);
		yLabel.setBounds(150,60,10,20);
		wLabel.setBounds(10,90,10,20);
		hLabel.setBounds(150,90,10,20);
		
		this.add(controlPanelSizeText);
		this.add(xLabel);
		this.add(yLabel);
		this.add(wLabel);
		this.add(hLabel);
		
		xTextField = new JTextField(10);
		yTextField = new JTextField(10);
		wTextField = new JTextField(10);
		hTextField = new JTextField(10);
		
		xTextField.setBounds(30,60,100,20);
		yTextField.setBounds(180,60,100,20);
		wTextField.setBounds(30,90,100,20);
		hTextField.setBounds(180,90,100,20);
		
		xTextField.addKeyListener(new settingKeyListener("xTextField", drawPanel));
		yTextField.addKeyListener(new settingKeyListener("yTextField",drawPanel));
		wTextField.addKeyListener(new settingKeyListener("wTextField", drawPanel));
		hTextField.addKeyListener(new settingKeyListener("hTextField",drawPanel));
		
		this.add(xTextField);
		this.add(yTextField);
		this.add(wTextField);
		this.add(hTextField);
		
		JLabel chooseBgText = new JLabel("배경이미지 선택");
		chooseBgText.setBounds(10,150,150,20);
		this.add(chooseBgText);
		
		JPanel BgBoxPanel = new JPanel();
		BgBoxPanel.setSize(300,300);
		BgBoxPanel.setLocation(0,20);
		BgBoxPanel.setLayout(new GridLayout(3,0,10,10));
		//this.add(BgBoxPanel,BorderLayout.CENTER);
		//BgBoxPanel.setBorder(new TitledBorder(new EtchedBorder(),"Background"));
		for(int i =0; i<imgs.length; i++) {
			ImageIcon icon = new ImageIcon(imgs[i]);
			Image img = icon.getImage();
			Image updateImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 이미지 버튼크기에 맞게 조절
			ImageIcon updateIcon = new ImageIcon(updateImg);
			updateIcon.setDescription(imgs[i]);
			
			bgButton = new JButton(updateIcon);
			bgButton.setPreferredSize(new Dimension(100,100));
			BgBoxPanel.add(bgButton);
			bgButton.addActionListener(new actionEvent());
			
			drawPanel.addMouseListener(new drawPanelMouseListener(drawPanel));
			drawPanel.addMouseMotionListener(new drawPanelMouseListener(drawPanel));

//			ImageIcon icon = new ImageIcon(imgs[i]);
//			bgButton = new MyButton(btnSize * ((i % 3)), btnSize * (i / 3), btnSize, btnSize, icon);
//			BgBoxPanel.add(bgButton);
//			bgButton.addActionListener(new actionEvent());
		}
		
		// 이미지 스크롤바 생성
		JScrollPane imgsScrollPane = new JScrollPane(BgBoxPanel);
		imgsScrollPane.setBounds(0, 180, 375, 300);
		imgsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(imgsScrollPane);

		// 이미지 추가버튼 생성
		JButton addImgButton = new JButton("이미지 추가");
		addImgButton.setSize(100,30);
		addImgButton.setLocation(250,500);
		this.add(addImgButton);
		addImgButton.addActionListener(new OpenFileEvent(BgBoxPanel));
		
		// 배경 초기화버튼 생성
		JButton initialButton = new JButton("배경 초기화");
		initialButton.setBounds(100,500,100,30);
		this.add(initialButton);
		initialButton.addActionListener(new initialEvent(drawPanel));
	}
	
	// 전체화면 크기 조절 키입력 이벤트
	class settingKeyListener extends KeyAdapter{
		String changedSetting;
		DrawPanel drawPanel;
//		DrawPanel drawPanel;
		public settingKeyListener() {}
		public settingKeyListener(String changedSetting, DrawPanel drawPanel) {
			this.changedSetting = changedSetting;
			this.drawPanel = drawPanel;
		}
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_ENTER:
				System.out.println("enter");
				if(changedSetting.equals("xTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int x = Integer.parseInt(textField.getText());
					drawPanel.setSize(drawPanel.getWidth(), drawPanel.getHeight());
					if(drawPanel.getWidth()!=800 || drawPanel.getHeight()!=800) { // 화면크기가 max가 아닐때만 움직이도록
						drawPanel.setLocation(x,drawPanel.getY());
					}
					
				}
				else if(changedSetting.equals("yTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int y = Integer.parseInt(textField.getText());
					drawPanel.setSize(drawPanel.getWidth(), drawPanel.getHeight());
					if(drawPanel.getWidth()!=800 || drawPanel.getHeight()!=800) {
						drawPanel.setLocation(drawPanel.getX(),y);
					}
				}
				else if(changedSetting.equals("wTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int w = Integer.parseInt(textField.getText());
					drawPanel.setSize(w, drawPanel.getHeight());
					drawPanel.setLocation(drawPanel.getX(),drawPanel.getY());
				} else if(changedSetting.equals("hTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int h = Integer.parseInt(textField.getText());
					drawPanel.setSize(drawPanel.getHeight(), h);
					drawPanel.setLocation(drawPanel.getX(),drawPanel.getY());
				}
				drawPanel.setBorder(BorderFactory.createEmptyBorder());
				drawPanel.repaint();

                break;
			case KeyEvent.VK_BACK_SPACE:
				mainPanel.remove(drawPanel);
				mainPanel.revalidate();
				
				break;

			}
			
		}
	}
	// 이미지 초기화버튼 눌렀을 때 이벤트
	class initialEvent implements ActionListener {
		DrawPanel drawPanel;
		public initialEvent(DrawPanel drawPanel) {
			this.drawPanel = drawPanel;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			drawPanel.removeBgIcon();
			drawPanel.repaint();
		}
	}
	// 이미지 추가버튼 눌렀을 때 이벤트
	class OpenFileEvent implements ActionListener {
		private JFileChooser chooser = new JFileChooser();
		JPanel BgBoxPanel;
		public OpenFileEvent(JPanel BgBoxPanel) {
			this.BgBoxPanel = BgBoxPanel;
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
				System.out.println("choose filePath: " + filePath);
				ImageIcon icon = new ImageIcon(filePath);
				Image img = icon.getImage();
				Image updateImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 이미지 버튼크기에 맞게 조절
				ImageIcon updateIcon = new ImageIcon(updateImg);
				updateIcon.setDescription(filePath);
				JButton addImgButton = new JButton(updateIcon);
				addImgButton.setPreferredSize(new Dimension(100,100));
				addImgButton.addActionListener(new actionEvent());
				BgBoxPanel.add(addImgButton);
				
			}

		}
	}
	// 배경 눌렀을 때 이벤트
	class actionEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			selectedButton = b;
			
			System.out.println(((ImageIcon) b.getIcon()).getDescription());
			String imageDescription = ((ImageIcon) b.getIcon()).getDescription();
			
			drawPanel.setBgIcon(new ImageIcon(imageDescription));
			drawPanel.repaint();

		}
	}
	class drawPanelMouseListener extends MouseAdapter  {
		DrawPanel drawPanel;
		public drawPanelMouseListener(DrawPanel drawPanel) {
			this.drawPanel = drawPanel;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(EnemyPanel.chooseImg == false) {
				hasBorder = !hasBorder;
				if(hasBorder) {
					drawPanel.setBorder(BorderFactory.createLineBorder(Color.blue,5));
				} else {
					drawPanel.setBorder(null);
				}
				
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
//			if(EnemyPanel.chooseImg == false) {
//				drawPanel.setLocation(e.getX(),e.getY());
//				mainPanel.repaint();
//			}
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
class ItemPanel extends JPanel {
	public ItemPanel() {
		
	}
}

class MyButton extends JButton {
	private int x,y,w,h;
	private ImageIcon icon;
	
	public MyButton(int x, int y, int w, int h, ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.setIcon(icon);
		this.setBounds(x,y,w,h);
		this.setOpaque(true);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setLocation(x, y);
		Image img = icon.getImage();
		g.drawImage(img, 0, 0, w, h, 0, 0, img.getWidth(null), img.getHeight(null), null);

	}

}

