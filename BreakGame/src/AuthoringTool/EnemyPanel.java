package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import AuthoringTool.BackgroundPanel.OpenFileEvent;
import AuthoringTool.BackgroundPanel.actionEvent;

public class EnemyPanel extends JPanel{
	DrawPanel drawPanel;
	JPanel selectedImgPanel = new JPanel();
	JPanel imgsPanel = new JPanel();
	private String[] imgs = {"image/enemy1.png", "image/enemy2.png","image/enemy3.png","image/enemy4.png","image/enemy5.png","image/enemy6.png","image/enemy7.png"};
	JButton enemyButton;
	// 속성 레이블
	private JLabel xLabel, yLabel, wLabel, hLabel, typeLabel, lifeLabel, speedLabel;
	// 속성 텍스트필드
	private JTextField xTextField,yTextField, wTextField, hTextField, lifeTextField;
	// 속성 콤보박스
	private String[] typeList = {"LeftRight", "RightLeft", "Free","Not Moving"};
	private String[] speedList = {"2","3","4","5","6","7","8","9","10"};
	private JComboBox typeCombo, speedCombo;
	
	// 오브젝트 생성할 때 필요한 것들
	static JButton clickedBtn;
	static EnemyObj enemyObj, selectedEnemy;
	static PlayerObj playerObj, selectedPlayer;
	static ShieldBlockObj shieldBlockObj, selectedShieldBlock;
	static ItemObj itemObj,selectedItem;
	
	static String imagePath;
	private boolean clickedDrawPanel = false;
	static boolean chooseImg = false;
	static String clickedObj = null;
	boolean hasBorder = false;
	// 생성된 오브젝트 저장하는 벡터
//	Vector<EnemyObj> enemys = new Vector<EnemyObj>();
	
	// 패널에서 클릭한 오브젝트
	EnemyObj clickedEnemy;
	static PolygonObj rect;
	
	private String copyObj = null;
	int cursorX, cursorY;
	
	public EnemyPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		setLayout(null);
		this.setSize(this.getWidth(), this.getHeight());
		setFocusable(true); // 키입력받기 위해 
		
		//이미지 나열 패널을 중간에 추가
		imgsPanel.setSize(300,300);
		imgsPanel.setLocation(0, 20);
		imgsPanel.setLayout(new GridLayout(3,0,10,10)); // 가로로 3개, 세로로 제한없이 추가시킬 수 있도록 설정
		for(int i =0; i<imgs.length; i++) {
			ImageIcon icon = new ImageIcon(imgs[i]);
			Image img = icon.getImage();
			Image updateImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 이미지 버튼크기에 맞게 조절
			ImageIcon updateIcon = new ImageIcon(updateImg);
			updateIcon.setDescription(imgs[i]);
			
			enemyButton = new JButton(updateIcon);
			enemyButton.setSize(50,50);
			imgsPanel.add(enemyButton);
			enemyButton.addActionListener(new actionEvent());
		}
		// 이미지 스크롤바 생성
		JScrollPane imgsScrollPane = new JScrollPane(imgsPanel);
		imgsScrollPane.setBounds(0, 20, 375, 300);
		imgsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(imgsScrollPane);
//		imgsPanel.setBorder(new TitledBorder("imgs"));
//		this.add(imgsPanel);
		
		// 이미지 추가버튼 생성
		JButton addImgButton = new JButton("이미지 추가");
		addImgButton.setSize(100,30);
		addImgButton.setLocation(250,340);
		this.add(addImgButton);
		addImgButton.addActionListener(new OpenFileEvent(imgsPanel));

		// 속성 레이블 배치
		xLabel = new JLabel("X");
		xLabel.setBounds(30,400,20,20);
		xTextField = new JTextField(10);
		xTextField.setBounds(60,400,100,20);
		xTextField.addKeyListener(new settingKeyListener("xTextField"));
		
		yLabel = new JLabel("Y");
		yLabel.setBounds(200,400,20,20);
		yTextField = new JTextField(10);
		yTextField.setBounds(230,400,100,20);
		yTextField.addKeyListener(new settingKeyListener("yTextField"));
		
		wLabel = new JLabel("W");
		wLabel.setBounds(30,450,20,20);
		wTextField = new JTextField(10);
		wTextField.setBounds(60,450,100,20);
		wTextField.addKeyListener(new settingKeyListener("wTextField"));
		
		hLabel = new JLabel("H");
		hLabel.setBounds(200,450,20,20);
		hTextField = new JTextField(10);
		hTextField.setBounds(230,450,100,20);
		hTextField.addKeyListener(new settingKeyListener("hTextField"));
		
		typeLabel = new JLabel("Type");
		typeLabel.setBounds(30,500,100,20);
		typeCombo = new JComboBox(typeList);
		typeCombo.setBounds(70,500,100,20);
		typeCombo.addActionListener(new ComboListener("typeCombo"));
		
		lifeLabel = new JLabel("Life");
		lifeLabel.setBounds(200,500,100,20);
		lifeTextField = new JTextField(10);
		lifeTextField.setBounds(230,500,100,20);
		lifeTextField.addKeyListener(new settingKeyListener("lifeTextField"));
		
		speedLabel = new JLabel("Speed");
		speedLabel.setBounds(30,550,100,20);
		speedCombo = new JComboBox(speedList);
		speedCombo.setBounds(70,550,100,20);
		speedCombo.addActionListener(new ComboListener("speedCombo"));
		
		add(xLabel);
		add(yLabel);
		add(wLabel);
		add(hLabel);
		add(typeLabel);
		add(lifeLabel);
		add(speedLabel);
		
		add(xTextField);
		add(yTextField);
		add(wTextField);
		add(hTextField);
		add(typeCombo);
		add(lifeTextField);
		add(speedCombo);
		
		drawPanel.requestFocusInWindow();
		drawPanel.addMouseListener(new DrawMouseListener());
		drawPanel.addMouseMotionListener(new DrawMouseListener());
		drawPanel.addKeyListener(new selectedObjListener());

//		if(clickedEnemy !=null) {
//			clickedEnemy.setFocusable(true);
//			clickedEnemy.requestFocusInWindow();
//			clickedEnemy.addKeyListener(new deleteKeyListener());
//		}
		
		clickedDrawPanel = false;
		System.out.println(clickedDrawPanel);
		// 기존에 선택한 버튼들의 Border를 제거
        for (Component component : imgsPanel.getComponents()) {
            if (component instanceof JButton && component == clickedBtn) {
                ((JButton) component).setBorder(BorderFactory.createEmptyBorder());
            }
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
				addImgButton.addActionListener(new actionEvent());
				imgsPanel.add(addImgButton);
				
			}
		}
	}
	// 삭제, 복사 리스너
	class selectedObjListener extends KeyAdapter{
		String obj;
		public selectedObjListener() {
		}
		public selectedObjListener(String obj) {
			this.obj = obj;
		}
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_BACK_SPACE:
				drawPanel.remove(rect);
				System.out.println("remove");
				if(clickedObj.equals("enemy")) {
					drawPanel.remove(selectedEnemy);
					EnemyObj.enemys.remove(selectedEnemy);
				} else if(clickedObj.equals("player")) {
					drawPanel.remove(selectedPlayer);
					PlayerObj.player.remove(selectedPlayer);
					System.out.println(PlayerObj.player.size());
				} else if(clickedObj.equals("shieldBlock")) {
					drawPanel.remove(selectedShieldBlock);
					ShieldBlockObj.shieldBlocks.remove(selectedShieldBlock);
				}

				drawPanel.repaint();
				break;
			}
			// ctrl+C
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
				copyObj = obj;
				System.out.println("복사한 오브젝트: " + copyObj);
			}
			// ctrl+V
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
				System.out.println("복사");
				switch(copyObj) {
				case "enemy":
					int x = cursorX;
					int y = cursorY;
					System.out.println(cursorX);
					int w1 = selectedEnemy.getW();
					int h1 = selectedEnemy.getH();
					String type1 = selectedEnemy.getType();
					int speed = selectedEnemy.getSpeed();
					int life = selectedEnemy.getLife();
					ImageIcon icon = new ImageIcon(selectedEnemy.img);

					EnemyObj newEnemy = new EnemyObj(cursorX, cursorY, w1,h1, type1, speed, life, icon);
					System.out.println("newObj의 x:"+newEnemy.getX());
					EnemyObj.enemys.add(newEnemy);
					drawPanel.add(newEnemy);
					drawPanel.repaint();
					break;
				case "shieldBlock":
					int x1 = cursorX;
					int y1 = cursorY;
					System.out.println(cursorX);
					int w2 = selectedShieldBlock.getW();
					int h2 = selectedShieldBlock.getH();
					String type2 = selectedShieldBlock.getType();
					ImageIcon icon1 = new ImageIcon(selectedShieldBlock.img);

					ShieldBlockObj newShieldBlock = new ShieldBlockObj(cursorX, cursorY, w2,h2, type2, icon1);
					ShieldBlockObj.shieldBlocks.add(newShieldBlock);
					drawPanel.add(newShieldBlock);
					drawPanel.repaint();

					break;
				}
			}

		}
	}
	
	// 콤보박스 이벤트
	class ComboListener implements ActionListener{
		private String comboBox;
		public ComboListener(String comboBox) {
			this.comboBox = comboBox;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox source = (JComboBox)e.getSource();
			int index;
			switch(comboBox) {
			case "typeCombo":
				if(clickedObj != null) {
					if(clickedObj.equals("enemy")) {
						index = source.getSelectedIndex();
						String type = typeList[index];
						selectedEnemy.setType(type);
					} 
				}

				break;
			case "speedCombo":
				if(selectedEnemy !=null) {
					index = source.getSelectedIndex();
					int speed = Integer.parseInt(speedList[index]);
					selectedEnemy.setSpeed(speed);		
				}
				break;
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
					if(clickedObj.equals("enemy")) {
						selectedEnemy.setLocation(x,selectedEnemy.getY());
						selectedEnemy.setX(x);
					} 
				} else if(changedSetting.equals("yTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int y = Integer.parseInt(textField.getText());
					if(clickedObj.equals("enemy")) {
						selectedEnemy.setLocation(selectedEnemy.getX(),y);
						selectedEnemy.setY(y);
					}  

				} else if(changedSetting.equals("wTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int w = Integer.parseInt(textField.getText());
					if(clickedObj.equals("enemy")) {
						selectedEnemy.setSize(w,selectedEnemy.getH());
						selectedEnemy.setW(w);
					} 
				} else if(changedSetting.equals("hTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int h = Integer.parseInt(textField.getText());
					if(clickedObj.equals("enemy")) {
						selectedEnemy.setSize(selectedEnemy.getW(),h);
						selectedEnemy.setH(h);
					} 
	
				} else if(changedSetting.equals("lifeTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int life = Integer.parseInt(textField.getText());
					if(clickedObj.equals("enemy")) {
						selectedEnemy.setLife(life);
					} 
				}
				drawPanel.remove(rect);
				drawPanel.repaint();

                break;
			
			}
		}

	}
	//enemy사진 눌렀을때 이벤트
	class actionEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			clickedBtn = (JButton)e.getSource();
			clickedBtn.setBorder(BorderFactory.createLineBorder(Color.red,5)); // 어떤 사진을 클릭했는지 알 수 있도록 border주기
			// 하나의 사진만 Border를 가질 수 있게
	        for (Component component : imgsPanel.getComponents()) {
	            if (component instanceof JButton && component != clickedBtn) {
	                ((JButton) component).setBorder(BorderFactory.createEmptyBorder());
	            }
	        }
	        // 선택한 이미지 경로 가져오기
	        if (clickedBtn.getIcon() instanceof ImageIcon) {
	            ImageIcon icon = (ImageIcon) clickedBtn.getIcon();
	            imagePath = icon.getDescription();
	            System.out.println("Selected Image Path: " + imagePath);
	            
	        }
	        chooseImg = true;
	        
	        
		}
	}
	class DrawMouseListener implements MouseMotionListener,MouseListener {
		@Override
		public void mousePressed(MouseEvent e) {
			clickedDrawPanel = true;
			boolean clickedExistedObj = false; // 새로 생성 여부를 정하기 위한 변수
			Object obj = null;
			int x1,y1,x2,y2;
			int w,h,life,speed;
			String type;
			Point p = e.getPoint(); // 마우스 커서 위치 저장
			for(int i =0; i<EnemyObj.enemys.size(); i++) { // 선택한 오브젝트 확인
				Rectangle enemyBounds = EnemyObj.enemys.get(i).getBounds();
				if (enemyBounds.contains(p)) {
					obj = EnemyObj.enemys.get(i);
					clickedObj = "enemy";
					clickedExistedObj = true;
				}
				else continue;
			}
			for(int i =0; i<PlayerObj.player.size(); i++) {
				Rectangle playerBounds = PlayerObj.player.get(i).getBounds();
				if (playerBounds.contains(p)) {
					obj = PlayerObj.player.get(i);
					clickedObj = "player";
					clickedExistedObj = true;
				}
				else continue;

			}
			for(int i =0; i<ShieldBlockObj.shieldBlocks.size(); i++) {
				Rectangle shieldBlockBounds = ShieldBlockObj.shieldBlocks.get(i).getBounds();
				if (shieldBlockBounds.contains(p)) {
					obj = ShieldBlockObj.shieldBlocks.get(i);
					clickedObj = "shieldBlock";
					clickedExistedObj = true;
				}
				else continue;

			}
			for(int i =0; i<ItemObj.heartItemObjArr.size(); i++) {
				Rectangle heartItemBounds = ItemObj.heartItemObjArr.get(i).getBounds();
				if (heartItemBounds.contains(p)) {
					obj = ItemObj.heartItemObjArr.get(i);
					clickedObj = "item";
					clickedExistedObj = true;
				}
				else continue;

			}
			for(int i =0; i<ItemObj.shieldItemObjArr.size(); i++) {
				Rectangle shieldItemBounds = ItemObj.shieldItemObjArr.get(i).getBounds();
				if (shieldItemBounds.contains(p)) {
					obj = ItemObj.shieldItemObjArr.get(i);
					clickedObj = "item";
					clickedExistedObj = true;
				}
				else continue;

			}

			
			System.out.println("clickedExistedObj: "+ clickedExistedObj);
			// 패널에 이미 있던 오브젝트를 선택한 경우에만 해당 코드 실행
			if(clickedExistedObj) {
				switch(clickedObj) {
				case "enemy":
					ObjPanel.objCombo.setSelectedItem("Enemy");
					selectedEnemy = (EnemyObj)obj; // 다운캐스팅
	                x1 = selectedEnemy.getX();
	                y1 = selectedEnemy.getY();
	                x2 = x1 + selectedEnemy.getWidth();
	                y2 = y1 + selectedEnemy.getHeight();
	                // 선택한 것만 박스 그려지도록
	                for (Component component : drawPanel.getComponents()) {
	                    if (component instanceof PolygonObj) {
	                        drawPanel.remove((PolygonObj) component);
	                    }
	                }
	                rect = new PolygonObj(x1, y1, x2, y2);
	                drawPanel.add(rect);
	                drawPanel.repaint();
	                
	                w = selectedEnemy.getW();
	                h = selectedEnemy.getH();
	                life = selectedEnemy.getLife();
	                speed = selectedEnemy.getSpeed();
	                type = selectedEnemy.getType();
	                
	                // 해당 오브젝트의 설정값 나타나도록
					xTextField.setText(Integer.toString(x1));
					yTextField.setText(Integer.toString(y1));
					wTextField.setText(Integer.toString(w));
					hTextField.setText(Integer.toString(h));
					lifeTextField.setText(Integer.toString(life));
//					String getSelectedItem = (String)typeCombo.getSelectedItem();
//					getSelectedItem = type;
//					typeCombo.setSelectedItem(getSelectedItem);
					typeCombo.setSelectedItem(type);
					speedCombo.setSelectedItem(Integer.toString(speed));
					
					//
					selectedEnemy.setFocusable(true);
					selectedEnemy.requestFocus(); // 이거 추가했더니 해결됨..(나중에 공부 다시하자^^)
					selectedEnemy.addKeyListener(new selectedObjListener(clickedObj));
					break;
				case "player":
					ObjPanel.objCombo.setSelectedItem("Player");
					selectedPlayer = (PlayerObj)obj; // 다운캐스팅
	                x1 = selectedPlayer.getX();
	                y1 = selectedPlayer.getY();
	                x2 = x1 + selectedPlayer.getWidth();
	                y2 = y1 + selectedPlayer.getHeight();
	                // 선택한 것만 박스 그려지도록
	                for (Component component : drawPanel.getComponents()) {
	                    if (component instanceof PolygonObj) {
	                        drawPanel.remove((PolygonObj) component);
	                    }
	                }
	                rect = new PolygonObj(x1, y1, x2, y2);
	                drawPanel.add(rect);
	                drawPanel.repaint();
	                
	                w = selectedPlayer.getW();
	                h = selectedPlayer.getH();
	                life = selectedPlayer.getLife();

	                // 해당 오브젝트의 설정값 나타나도록
					PlayerPanel.xTextField.setText(Integer.toString(x1));
					PlayerPanel.yTextField.setText(Integer.toString(y1));
					PlayerPanel.wTextField.setText(Integer.toString(w));
					PlayerPanel.hTextField.setText(Integer.toString(h));
					PlayerPanel.lifeTextField.setText(Integer.toString(life));
					
					selectedPlayer.setFocusable(true);
					selectedPlayer.requestFocus();
					selectedPlayer.addKeyListener(new selectedObjListener(clickedObj));


					break;
				case "shieldBlock":
					ObjPanel.objCombo.setSelectedItem("ShieldBlock");
					selectedShieldBlock = (ShieldBlockObj)obj; // 다운캐스팅
	                x1 = selectedShieldBlock.getX();
	                y1 = selectedShieldBlock.getY();
	                x2 = x1 + selectedShieldBlock.getWidth();
	                y2 = y1 + selectedShieldBlock.getHeight();
	                // 선택한 것만 박스 그려지도록
	                for (Component component : drawPanel.getComponents()) {
	                    if (component instanceof PolygonObj) {
	                        drawPanel.remove((PolygonObj) component);
	                    }
	                }
	                rect = new PolygonObj(x1, y1, x2, y2);
	                drawPanel.add(rect);
	                drawPanel.repaint();
	                
	                w = selectedShieldBlock.getW();
	                h = selectedShieldBlock.getH();
	                type = selectedShieldBlock.getType();
	                
	                // 해당 오브젝트의 설정값 나타나도록
					ShieldBlockPanel.xTextField.setText(Integer.toString(x1));
					ShieldBlockPanel.yTextField.setText(Integer.toString(y1));
					ShieldBlockPanel.wTextField.setText(Integer.toString(w));
					ShieldBlockPanel.hTextField.setText(Integer.toString(h));
					ShieldBlockPanel.typeCombo.setSelectedItem(type);

					selectedShieldBlock.setFocusable(true);
					selectedShieldBlock.requestFocus();
					selectedShieldBlock.addKeyListener(new selectedObjListener(clickedObj));

					break;
				case "item":
					ObjPanel.objCombo.setSelectedItem("Item");
					selectedItem = (ItemObj)obj; // 다운캐스팅
	                x1 = selectedItem.getX();
	                y1 = selectedItem.getY();
	                x2 = x1 + selectedItem.getWidth();
	                y2 = y1 + selectedItem.getHeight();
	                // 선택한 것만 박스 그려지도록
	                for (Component component : drawPanel.getComponents()) {
	                    if (component instanceof PolygonObj) {
	                        drawPanel.remove((PolygonObj) component);
	                    }
	                }
	                rect = new PolygonObj(x1, y1, x2, y2);
	                drawPanel.add(rect);
	                drawPanel.repaint();
	                
	                w = selectedItem.getW();
	                h = selectedItem.getH();
	                
	                // 해당 오브젝트의 설정값 나타나도록
					ItemPanel.xTextField.setText(Integer.toString(x1));
					ItemPanel.yTextField.setText(Integer.toString(y1));
					ItemPanel.wTextField.setText(Integer.toString(w));
					ItemPanel.hTextField.setText(Integer.toString(h));
					break;
				}
			} 
			else { // 오브젝트 생성
				String selectedItem = ObjPanel.selectedItem;

				ImageIcon icon;
				System.out.println(selectedItem);
				switch(selectedItem) {
				case "Enemy":
					int x = e.getX();
					int y = e.getY();
					int w1 = 50;
					int h1 = 60;
					String type1 = "LeftRight";
					speed = 2;
					life = 2;
					icon = new ImageIcon(imagePath);
					
					System.out.println("panel clicked!");
					enemyObj = new EnemyObj(x,y,w1,h1,type1,speed,life,icon);
					EnemyObj.enemys.add(enemyObj); // 벡터에 오브젝트 저장
					drawPanel.add(enemyObj);
					drawPanel.repaint(); // drawPanel에 오브젝트 그린다
					
					xTextField.setText(Integer.toString(x));
					yTextField.setText(Integer.toString(y));
					wTextField.setText(Integer.toString(w1));
					hTextField.setText(Integer.toString(h1));
					lifeTextField.setText(Integer.toString(life));
//					String getSelectedItem = (String)typeCombo.getSelectedItem();
//					getSelectedItem = type;
//					typeCombo.setSelectedItem(getSelectedItem);
					typeCombo.setSelectedItem(type1);
					speedCombo.setSelectedItem(Integer.toString(speed));
					
					// 이미지목록패널의 테두리선 제거 및 선택초기화
					clickedBtn.setBorder(null);
					imagePath = null;
					chooseImg = false;
					break;
				case "Player":
					System.out.println("player아이템 선택");
					if(PlayerObj.player.size() < 1) {
						x = e.getX();
						y = e.getY();
						w1 = 40;
						h1 = 60;
						life = 5;
						icon = new ImageIcon(imagePath);
						System.out.println(imagePath);
						
						System.out.println("panel clicked!");
						PlayerObj playerObj = new PlayerObj(x,y,w1,h1,life,icon);
						PlayerObj.player.add(playerObj); // 벡터에 오브젝트 저장
						drawPanel.add(playerObj);
						drawPanel.repaint(); // drawPanel에 오브젝트 그린다
						
						PlayerPanel.xTextField.setText(Integer.toString(x));
						PlayerPanel.yTextField.setText(Integer.toString(y));
						PlayerPanel.wTextField.setText(Integer.toString(w1));
						PlayerPanel.hTextField.setText(Integer.toString(h1));
						PlayerPanel.lifeTextField.setText(Integer.toString(life));
						
						// 이미지목록패널의 테두리선 제거 및 선택초기화
						clickedBtn.setBorder(null);
						chooseImg = false;
						imagePath = null;

					}

					break;
				case "ShieldBlock":
					x = e.getX();
					y = e.getY();
					w1 = 50;
					h1 = 60;
					type1 = "breakable";
					icon = new ImageIcon(imagePath);
					
					System.out.println("panel clicked!");
					shieldBlockObj = new ShieldBlockObj(x,y,w1,h1,type1, icon);
					ShieldBlockObj.shieldBlocks.add(shieldBlockObj); // 벡터에 오브젝트 저장
					drawPanel.add(shieldBlockObj);
					drawPanel.repaint(); // drawPanel에 오브젝트 그린다
					
					ShieldBlockPanel.xTextField.setText(Integer.toString(x));
					ShieldBlockPanel.yTextField.setText(Integer.toString(y));
					ShieldBlockPanel.wTextField.setText(Integer.toString(w1));
					ShieldBlockPanel.hTextField.setText(Integer.toString(h1));
					ShieldBlockPanel.typeCombo.setSelectedItem(type1);
					
					// 이미지목록패널의 테두리선 제거 및 선택초기화
					clickedBtn.setBorder(null);
					chooseImg = false;
					imagePath = null;

					break;
				case "Item":
					x = e.getX();
					y = e.getY();
					w1 = 50;
					h1 = 60;
					icon = new ImageIcon(imagePath);
					
					System.out.println("panel clicked!");
					itemObj = new ItemObj(x,y,w1,h1,icon);
					if(ItemPanel.imgPanelName.equals("lifeImgsPanel")) {
						if(ItemObj.heartItemObjArr.size()!=1) { // 벡터에 하나만 저장하고 그릴 수 있도록
							ItemObj.heartItemObjArr.add(itemObj); // 벡터에 오브젝트 저장
							drawPanel.add(itemObj);
							drawPanel.repaint(); // drawPanel에 오브젝트 그린다

						}
						else {
							// 벡터가 꽉차면 tooltip 달기?
						}
					}
					if(ItemPanel.imgPanelName.equals("shieldImgsPanel")) {
						if(ItemObj.shieldItemObjArr.size()!=1) { // 벡터에 하나만 저장하고 그릴 수 있도록
							ItemObj.shieldItemObjArr.add(itemObj); // 벡터에 오브젝트 저장
							drawPanel.add(itemObj);
							drawPanel.repaint(); // drawPanel에 오브젝트 그린다
						}
						else {
							// 벡터가 꽉차면 tooltip 달기?
						}
					}
					
					ItemPanel.xTextField.setText(Integer.toString(x));
					ItemPanel.yTextField.setText(Integer.toString(y));
					ItemPanel.wTextField.setText(Integer.toString(w1));
					ItemPanel.hTextField.setText(Integer.toString(h1));
					
					
					// 이미지목록패널의 테두리선 제거 및 선택초기화
					clickedBtn.setBorder(null);
					chooseImg = false;
					imagePath = null;

					break;
				default:
					hasBorder = !hasBorder;
					if(hasBorder) {
						drawPanel.setBorder(BorderFactory.createLineBorder(Color.blue,5));
					} else {
						drawPanel.setBorder(null);
					}

					break;
				}
				cursorX = e.getX();
				cursorY = e.getY();
				System.out.println("클릭 커서위치x: " + cursorX);

				
				
			}

		}
		@Override
		public void mouseMoved(MouseEvent e) {
			Cursor hand_cursor = new Cursor(Cursor.HAND_CURSOR);
			Cursor ne_cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
			Cursor sw_cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
			Cursor default_cursor = new Cursor(Cursor.DEFAULT_CURSOR);

			for(int i =0; i<EnemyObj.enemys.size(); i++) {
		           Point p = e.getPoint();
		           Rectangle enemyBounds = EnemyObj.enemys.get(i).getBounds();
		            if (enemyBounds.contains(p)) {
		                drawPanel.setCursor(hand_cursor);
		                return; // 해당 객체와 일치하는 경우 커서를 설정하고 메서드를 종료
		            }	
		     }
			 drawPanel.setCursor(default_cursor);
			 
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseDragged(MouseEvent e) {
				int x,y;
				switch(clickedObj) {
				case "enemy":
					drawPanel.remove(rect);
					x = e.getX();
					y = e.getY();
					selectedEnemy.setLocation(x,y);
					selectedEnemy.setX(x);
					selectedEnemy.setY(y);
					drawPanel.repaint();
					
					// 속성필드 값변경
					xTextField.setText(Integer.toString(x));
					yTextField.setText(Integer.toString(y));
					
					break;
				case "player":
					drawPanel.remove(rect);
					x = e.getX();
					y = e.getY();
					selectedPlayer.setLocation(x,y);
					selectedPlayer.setX(x);
					selectedPlayer.setY(y);
					drawPanel.repaint();
					
					// 속성필드 값변경
					PlayerPanel.xTextField.setText(Integer.toString(x));
					PlayerPanel.yTextField.setText(Integer.toString(y));
										
					break;
				case "shieldBlock":
					drawPanel.remove(rect);
					x = e.getX();
					y = e.getY();
					selectedShieldBlock.setLocation(x,y);
					selectedShieldBlock.setX(x);
					selectedShieldBlock.setY(y);
					drawPanel.repaint();
					
					// 속성필드 값변경
					ShieldBlockPanel.xTextField.setText(Integer.toString(x));
					ShieldBlockPanel.yTextField.setText(Integer.toString(y));

					break;
				case "item":
					drawPanel.remove(rect);
					x = e.getX();
					y = e.getY();
					selectedItem.setLocation(x,y);
					selectedItem.setX(x);
					selectedItem.setY(y);
					drawPanel.repaint();
					
					// 속성필드 값변경
					ItemPanel.xTextField.setText(Integer.toString(x));
					ItemPanel.yTextField.setText(Integer.toString(y));

					break;
				}
				
				
			
			
		}
	}
}