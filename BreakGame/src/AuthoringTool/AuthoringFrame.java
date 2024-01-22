package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class AuthoringFrame extends JFrame {
	Container c;
	MainPanel mainPanel;
	RightPanel rightPanel;
	DrawPanel drawPanel;
	ToolTappedPane toolTappedPane;
	SizePanel sizePanel = new SizePanel();
	public AuthoringFrame() {
		setTitle("Authoring Tool");
		setSize(1200,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		makeMenu();
		makeSplitPane();
		
		setResizable(false);  // 크기 고정
		setVisible(true);
	}
	
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu File = new JMenu("File");
		JMenuItem newFileItem = new JMenuItem("New");
		JMenuItem openFileItem = new JMenuItem("Open File...");
		JMenuItem saveFileItem = new JMenuItem("Save");
		
		newFileItem.addActionListener(new newFileActionListener());
		File.add(newFileItem);
		File.add(openFileItem);
		File.add(saveFileItem);
		
		menuBar.add(File);
		
		setJMenuBar(menuBar); // 메뉴바를 프레임에 넣기
	}
	private void makeSplitPane() {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(800);
		
		mainPanel = new MainPanel();
		rightPanel = new RightPanel();
		//toolTappedPane = new ToolTappedPane();
		hPane.setLeftComponent(mainPanel);
		hPane.setRightComponent(rightPanel);
		c.add(hPane, BorderLayout.CENTER);
		
	}
	
	class newFileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, sizePanel, "게임메인화면 크기 지정", JOptionPane.PLAIN_MESSAGE);
			sizePanel.setSize();
			drawPanel = new DrawPanel(sizePanel.getW(),sizePanel.getH());
			drawPanel.setLocation(0,0);
			mainPanel.add(drawPanel);
			toolTappedPane = new ToolTappedPane(drawPanel);
			rightPanel.add(toolTappedPane, BorderLayout.CENTER);
			c.revalidate();
		}

	}
}


class SizePanel extends JPanel {
	private int w, h;
	private JTextField widthTextField = new JTextField(10);
	private JTextField heightTextField = new JTextField(10);
	
	public SizePanel() {
		setLayout(new GridLayout(3,2));
		add(new JLabel("Max Width:800, Min Width:800"));
		add(new JLabel(""));
		add(new JLabel("Width:"));
		add(widthTextField);
		
		add(new JLabel("Height:"));
		add(heightTextField);
		heightTextField.addActionListener(new textFieldListener());
	}
	public int getW() {return w;}
	public int getH() {return h;}
	public void setSize() {
		w=Integer.parseInt(widthTextField.getText());
		widthTextField.setText("");
		h=Integer.parseInt(heightTextField.getText());
		heightTextField.setText("");
	}
	class textFieldListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField jt=(JTextField)e.getSource();
			if(jt==widthTextField) {
				w=Integer.parseInt(jt.getText());
			}
			else
				h=Integer.parseInt(jt.getText());
		}
	}
}