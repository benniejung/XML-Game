package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	public EnemyPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		setLayout(null);
		this.setSize(this.getWidth(), this.getHeight());
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


		//속성 설정 패널을 아래에 추가
		
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

	//enemy사진 눌렀을때 이벤트
	class actionEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton clickedBtn = (JButton)e.getSource();
			clickedBtn.setBorder(BorderFactory.createLineBorder(Color.red,5));
			// 기존에 선택한 버튼들의 Border를 제거
	        for (Component component : imgsPanel.getComponents()) {
	            if (component instanceof JButton && component != clickedBtn) {
	                ((JButton) component).setBorder(BorderFactory.createEmptyBorder());
	            }
	        }
		}
	}
}