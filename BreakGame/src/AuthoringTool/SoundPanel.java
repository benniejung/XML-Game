package AuthoringTool;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import AuthoringTool.EnemyPanel.actionEvent;

public class SoundPanel extends JPanel{
	DrawPanel drawPanel;
	static String fileName = null;
	private JButton chooseFileBtn;
	private JLabel fileTextLabel, fileLabel;
	public SoundPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		this.setLayout(null);
		
		chooseFileBtn = new JButton("파일선택");
		chooseFileBtn.setBounds(120,50,130,30);
		this.add(chooseFileBtn);
		chooseFileBtn.addActionListener(new OpenFileEvent()); // 파일 열기
		
		fileTextLabel = new JLabel("선택파일: ");
		fileTextLabel.setBounds(70,120,150,20);
		this.add(fileTextLabel);
		
		fileLabel = new JLabel("");
		fileLabel.setBounds(150,115,500,30);
		this.add(fileLabel);
		
	}
	// 이미지 추가버튼 눌렀을 때 이벤트
	class OpenFileEvent implements ActionListener {
		private JFileChooser chooser = new JFileChooser();
		public OpenFileEvent() {
			chooser = new JFileChooser("C:\\Users\\User\\OneDrive\\바탕 화면\\music"); // 해당 경로에서 파일 찾기
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Wav", "wav"); // 사진만 선택되도록
			chooser.setFileFilter(filter);
			// 파일을 선택하지 않았을 경우
			if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 선택해주세요", "Choose File", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// 파일을 선택한 경우
			if (chooser.getSelectedFile() != null) {
				String filePath = chooser.getSelectedFile().getName();
				fileLabel.setText(filePath);
				
				
				fileName = filePath.substring(0, filePath.lastIndexOf("."));
			}
		}
	}
}
