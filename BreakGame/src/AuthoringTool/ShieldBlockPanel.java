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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
import AuthoringTool.EnemyPanel.ComboListener;
import AuthoringTool.PlayerPanel.DrawMouseListener;
import AuthoringTool.PlayerPanel.settingKeyListener;

public class ShieldBlockPanel extends JPanel{
	DrawPanel drawPanel;
	JPanel selectedImgPanel = new JPanel();
	JPanel imgsPanel = new JPanel();
	private String[] imgs = {"image/shield-block.png", "image/shield-block2.png"};
	JButton enemyButton;
	// 속성 레이블
	private JLabel xLabel, yLabel, wLabel, hLabel, typeLabel;
	// 속성 텍스트필드
	static JTextField xTextField, yTextField, wTextField, hTextField;
	// 속성 콤보박스
	private String[] typeList = {"breakable", "unbreakable"};
	static JComboBox typeCombo;
	
	private String imagePath;
	boolean chooseImg = false;
	
	ShieldBlockObj shieldBlockObj = null;
	ShieldBlockObj clickedShieldBlock = null;
	PolygonObj rect;
	public ShieldBlockPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		setLayout(null);
		//이미지 나열 패널을 중간에 추가
		imgsPanel.setSize(350,400);
		imgsPanel.setLocation(0, 20);
		imgsPanel.setLayout(new GridLayout(1,0,10,10)); // 가로로 3개, 세로로 제한없이 추가시킬 수 있도록 설정
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
		// 이미지 추가버튼 생성
		JButton addImgButton = new JButton("이미지 추가");
		addImgButton.setSize(100,30);
		addImgButton.setLocation(250,340);
		this.add(addImgButton);
		addImgButton.addActionListener(new OpenFileEvent(imgsPanel));


		//속성 설정 패널을 아래에 추가
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
				
		
		add(xLabel);
		add(yLabel);
		add(wLabel);
		add(hLabel);
		add(typeLabel);

		add(xTextField);
		add(yTextField);
		add(wTextField);
		add(hTextField);
		add(typeCombo);
		
		//drawPanel.addMouseListener(new DrawMouseListener());
		//drawPanel.addMouseMotionListener(new DrawMouseListener());

		
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

	//shieldBlock사진 눌렀을때 이벤트
	class actionEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
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
				if(EnemyPanel.clickedObj != null) {
					if(EnemyPanel.clickedObj.equals("shieldBlock")) {
						index = source.getSelectedIndex();
						String type = typeList[index];
						EnemyPanel.selectedShieldBlock.setType(type);
					}
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
				if(changedSetting.equals("xTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int x = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("shieldBlock")) {
						EnemyPanel.selectedShieldBlock.setLocation(x,EnemyPanel.selectedShieldBlock.getY());
						EnemyPanel.selectedShieldBlock.setX(x);
					
					}
				} else if(changedSetting.equals("yTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int y = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("shieldBlock")) {
						EnemyPanel.selectedShieldBlock.setLocation(EnemyPanel.selectedShieldBlock.getX(),y);
						EnemyPanel.selectedShieldBlock.setY(y);
					
					}
				} else if(changedSetting.equals("wTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int w = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("shieldBlock")) {
						EnemyPanel.selectedShieldBlock.setSize(w,EnemyPanel.selectedShieldBlock.getH());
						EnemyPanel.selectedShieldBlock.setW(w);
					
					}
				} else if(changedSetting.equals("hTextField")) {
					JTextField textField = (JTextField)e.getSource();
					int h = Integer.parseInt(textField.getText());
					if(EnemyPanel.clickedObj.equals("shieldBlock")) {
						EnemyPanel.selectedShieldBlock.setSize(EnemyPanel.selectedShieldBlock.getW(),h);
						EnemyPanel.selectedShieldBlock.setH(h);
					
					}
				} 
				drawPanel.remove(EnemyPanel.rect);
				drawPanel.repaint();

                break;
			case KeyEvent.VK_BACK_SPACE:
				System.out.println("remove");
				drawPanel.remove(rect);
				drawPanel.remove(clickedShieldBlock);
				ShieldBlockObj.shieldBlocks.remove(clickedShieldBlock);
				drawPanel.repaint();
				
				break;
			}
			
		}
	}
	class DrawMouseListener implements MouseMotionListener,MouseListener {
		@Override
		public void mousePressed(MouseEvent e) {
			//clickedDrawPanel = true;
//			boolean clickedEnemyObj = false;
//			if(chooseImg == true) { // 이미지를 선택했을 경우, 패널에 그리거나 이미 있던 오브젝트 선택
//				for(int i =0; i<ShieldBlockObj.shieldBlocks.size(); i++) {
//			           Point p = e.getPoint();
//			           Rectangle shieldBlockBounds = ShieldBlockObj.shieldBlocks.get(i).getBounds();
//			            if (shieldBlockBounds.contains(p)) {
//			            	clickedEnemyObj = true;
//			                System.out.println("find");
//			                clickedShieldBlock = ShieldBlockObj.shieldBlocks.get(i);
//			                
//			                int x1 = clickedShieldBlock.getX();
//			                int y1 = clickedShieldBlock.getY();
//			                int x2 = x1 + clickedShieldBlock.getWidth();
//			                int y2 = y1 + clickedShieldBlock.getHeight();
//			                // 선택한 것만 박스 그려지도록
//			                for (Component component : drawPanel.getComponents()) {
//			                    if (component instanceof PolygonObj) {
//			                        drawPanel.remove((PolygonObj) component);
//			                    }
//			                }
//			                rect = new PolygonObj(x1, y1, x2, y2);
//			                drawPanel.add(rect);
//			                drawPanel.repaint();
//			                
////			    			Cursor ne_cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
////			    			drawPanel.setCursor(ne_cursor);
//			                
//							xTextField.setText(Integer.toString(clickedShieldBlock.getX()));
//							yTextField.setText(Integer.toString(clickedShieldBlock.getY()));
//							wTextField.setText(Integer.toString(clickedShieldBlock.getWidth()));
//							hTextField.setText(Integer.toString(clickedShieldBlock.getHeight()));
//							typeCombo.setSelectedItem(clickedShieldBlock.getType());
//
//			                return; // 해당 객체와 일치하는 경우 커서를 설정하고 메서드를 종료
//			            }	
//			     }
//
//				if(clickedEnemyObj) { // 이미 있는 오브젝트를 클릭했다면
//					
//
//				} else { // 새 오브젝트를 놓을 떄
//					int x = e.getX();
//					int y = e.getY();
//					int w = 50;
//					int h = 60;
//					String type = "breakable";
//					ImageIcon icon = new ImageIcon(imagePath);
//					
//					System.out.println("panel clicked!");
//					shieldBlockObj = new ShieldBlockObj(x,y,w,h,type, icon);
//					ShieldBlockObj.shieldBlocks.add(shieldBlockObj); // 벡터에 오브젝트 저장
//					drawPanel.add(shieldBlockObj);
//					drawPanel.repaint(); // drawPanel에 오브젝트 그린다
//					
//					xTextField.setText(Integer.toString(x));
//					yTextField.setText(Integer.toString(y));
//					wTextField.setText(Integer.toString(w));
//					hTextField.setText(Integer.toString(h));
////					String getSelectedItem = (String)typeCombo.getSelectedItem();
////					getSelectedItem = type;
////					typeCombo.setSelectedItem(getSelectedItem);
//					typeCombo.setSelectedItem(type);
//				}
//
//			}
//			else { // 어떤 이미지도 선택하지 않을 경우, 패널의 위치와 크기를 이동한다.
////				int x = e.getX();
////				int y = e.getY();
////				mainPanel.remove(drawPanel);
////				drawPanel.setLocation(x,y);
////				mainPanel.repaint();
//
//			}
//
		}
		@Override
		public void mouseMoved(MouseEvent e) {
//			if(chooseImg == true) {
//				Cursor hand_cursor = new Cursor(Cursor.HAND_CURSOR);
//				Cursor ne_cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
//				Cursor sw_cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
//				Cursor default_cursor = new Cursor(Cursor.DEFAULT_CURSOR);
//
//				for(int i =0; i<ShieldBlockObj.shieldBlocks.size(); i++) {
//			           Point p = e.getPoint();
//			           Rectangle shieldBlockBounds = ShieldBlockObj.shieldBlocks.get(i).getBounds();
//			            if (shieldBlockBounds.contains(p)) {
//			                drawPanel.setCursor(hand_cursor);
//			                return; // 해당 객체와 일치하는 경우 커서를 설정하고 메서드를 종료
//			            }	
//			     }
//				 drawPanel.setCursor(default_cursor);
//
//			}
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
//			if(chooseImg==true) {
//				drawPanel.remove(rect);
//				int x = e.getX();
//				int y = e.getY();
//				clickedShieldBlock.setLocation(x,y);
//				clickedShieldBlock.setX(x);
//				clickedShieldBlock.setY(y);
//				drawPanel.repaint();
//				
//				// 속성필드 값변경
//				xTextField.setText(Integer.toString(x));
//				yTextField.setText(Integer.toString(y));
//				
//			}
			
		}
	}
		
	}
