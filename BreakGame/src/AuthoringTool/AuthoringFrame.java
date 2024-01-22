package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

public class AuthoringFrame extends JFrame {
	Container c;
	MainPanel mainPanel;
	ToolTappedPane toolTappedPane;
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
		JMenuItem openFileItem = new JMenuItem("Open File...");
		JMenuItem saveFileItem = new JMenuItem("Save");
		
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
		toolTappedPane = new ToolTappedPane();
		hPane.setLeftComponent(mainPanel);
		hPane.setRightComponent(toolTappedPane);
		c.add(hPane, BorderLayout.CENTER);
		
	}
}