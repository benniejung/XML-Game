package AuthoringTool;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ToolTappedPane extends JTabbedPane{
	DrawPanel drawPanel;
	public ToolTappedPane(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		
		this.addTab("Bg", new BackgroundPanel(drawPanel));
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
	public BackgroundPanel(DrawPanel drawPanel) {
		this.setLayout(new BorderLayout());
		this.drawPanel = drawPanel;
		JPanel BgBoxPanel = new JPanel();
		this.add(BgBoxPanel,BorderLayout.CENTER);
		BgBoxPanel.setBorder(new TitledBorder(new EtchedBorder(),"Background"));
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
			
//			ImageIcon icon = new ImageIcon(imgs[i]);
//			bgButton = new MyButton(btnSize * ((i % 3)), btnSize * (i / 3), btnSize, btnSize, icon);
//			BgBoxPanel.add(bgButton);
//			bgButton.addActionListener(new actionEvent());
		}
		// 이미지 추가버튼
		ImageIcon icon = new ImageIcon("image/add-image.png");
		Image img = icon.getImage();
		Image updateImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 이미지 버튼크기에 맞게 조절
		JButton addImgButton = new JButton(new ImageIcon(updateImg));
		addImgButton.setPreferredSize(new Dimension(100,100));
		
		// 위아래 간격 주기위해 빈박스로 채우기
		Box top = Box.createVerticalBox();
		top.add(Box.createVerticalStrut(100));
		Box bottom = Box.createVerticalBox();
		bottom.add(addImgButton);
		addImgButton.addActionListener(new OpenFileEvent(BgBoxPanel));
		JLabel addTextLabel = new JLabel("이미지 추가");
		bottom.add(addTextLabel);
		this.add(top, BorderLayout.NORTH);
		this.add(bottom,BorderLayout.SOUTH);
		

		
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

