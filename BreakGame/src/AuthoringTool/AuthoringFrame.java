package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AuthoringFrame extends JFrame {
	Container c;
	MainPanel mainPanel;
	RightPanel rightPanel;
	DrawPanel drawPanel;
	ToolTappedPane toolTappedPane;
	SizePanel sizePanel = new SizePanel();
	public AuthoringFrame() {
		setTitle("Authoring Tool");
		setSize(1200,900);
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
		openFileItem.addActionListener(new openFileActionListener());
		saveFileItem.addActionListener(new saveFileActionListener());
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
			drawPanel = new DrawPanel(sizePanel.getW(),sizePanel.getH(), mainPanel);
			drawPanel.setLocation(0,0);
			mainPanel.add(drawPanel);
			drawPanel.setBackground(Color.white);
            toolTappedPane = new ToolTappedPane(mainPanel,drawPanel);
            rightPanel.add(toolTappedPane, BorderLayout.CENTER);
			c.revalidate();
			
		}
	}
	class openFileActionListener implements ActionListener {
		private JFileChooser chooser;
		public openFileActionListener() {
			chooser = new JFileChooser("C:\\Users\\User\\git\\XML-Game\\BreakGame"); // 해당 경로에서 파일 찾기
		}
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
			chooser.setFileFilter(filter); // xml파일만 선택할 수 있음
			int ret = chooser.showOpenDialog(drawPanel);
			if (ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 선택해주세요", "파일 선택", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(chooser.getSelectedFile()!=null) {
				String filePath = chooser.getSelectedFile().getName();
				System.out.println("chose file: " + filePath);
				XMLReader xml = new XMLReader(filePath);
				Node mainGameNode = xml.getMainGameElement();
				Node sizeNode = XMLReader.getNode(mainGameNode,XMLReader.E_SIZE);
				
				String sizeW = XMLReader.getAttr(sizeNode, "w");
				String seizeH = XMLReader.getAttr(sizeNode, "h");
				drawPanel = new DrawPanel(Integer.parseInt(sizeW),Integer.parseInt(seizeH), mainPanel);
				drawPanel.setLocation(0,0);
				mainPanel.add(drawPanel);
				drawPanel.setBackground(Color.white);
	            toolTappedPane = new ToolTappedPane(mainPanel,drawPanel);
	            rightPanel.add(toolTappedPane, BorderLayout.CENTER);
				c.revalidate();
				
				Node gamePanelNode = xml.getGamePanelElement();
				Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BG);
				ImageIcon bgImg = new ImageIcon(bgNode.getTextContent());
				drawPanel.setBgIcon(bgImg); // 배경사진 설정
				
				Node soundNode = XMLReader.getNode(gamePanelNode, XMLReader.E_SOUND); // 음악설정
				
				
				Node activeScreenNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ACTIVESCREEN);
				// Player노드
				Node playerNode = XMLReader.getNode(activeScreenNode, XMLReader.E_PLAYER);
				NodeList playerNodeList = playerNode.getChildNodes();
				for(int i=0; i<playerNodeList.getLength(); i++) {
					Node node = playerNodeList.item(i);
					if(node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					 // found!!, <Obj> tag
					if(node.getNodeName().equals(XMLReader.E_OBJ)) {
						int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
						int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
						int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
						int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
						int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
						ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
						PlayerObj player = new PlayerObj(x,y,w,h, life, icon);
						drawPanel.add(player);
						PlayerObj.player.add(player);
					}
				}
				// Enemy노드
				Node enemiesNode = XMLReader.getNode(activeScreenNode, XMLReader.E_ENEMIES);
				NodeList enemiesNodeList = enemiesNode.getChildNodes();
				for(int i=0; i<enemiesNodeList.getLength(); i++) {
					Node node = enemiesNodeList.item(i);
					if(node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					// found!!, <Obj> tag
					if(node.getNodeName().equals(XMLReader.E_ENEMY)) {
						int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
						int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
						int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
						int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
						int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
						int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
						String type = XMLReader.getAttr(node, "type");
						ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
						
						EnemyObj enemy = new EnemyObj(x,y,w,h,type,life,speed, icon);
						EnemyObj.enemys.add(enemy);
						drawPanel.add(enemy);
						
					}
				}
				// shieldBlock 노드
				Node shieldBlocksNode = XMLReader.getNode(activeScreenNode, XMLReader.E_SHIELDBLOCKS);
				NodeList shieldBlocksNodeList = shieldBlocksNode.getChildNodes();
				for(int i=0; i<shieldBlocksNodeList.getLength(); i++) {
					Node node = shieldBlocksNodeList.item(i);
					if(node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					// found!!, <Obj> tag
					if(node.getNodeName().equals(XMLReader.E_SHIELDBLOCK)) {
						int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
						int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
						int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
						int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
						String type = XMLReader.getAttr(node, "type");
						
						ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
						ShieldBlockObj shieldBlock = new ShieldBlockObj(x,y,w,h,type,icon);
						ShieldBlockObj.shieldBlocks.add(shieldBlock);
						drawPanel.add(shieldBlock);
						
					}
				}



			}

			
		}
	}
	class saveFileActionListener implements ActionListener {
		private JFileChooser chooser;
		public saveFileActionListener() {
			chooser = new JFileChooser("C:\\Users\\User\\git\\XML-Game\\BreakGame"); // 해당 경로에서 파일 찾기

		}
		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
			chooser.setFileFilter(filter); // xml파일만 선택할 수 있음
			int ret = chooser.showSaveDialog(drawPanel);
			if (ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 저장해주세요", "파일 저장", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String imageDescription = ((ImageIcon) drawPanel.getBgIcon()).getDescription();
			String soundFile = SoundPanel.fileName;
			if (chooser.getSelectedFile() != null) {
				String filePath = chooser.getSelectedFile().getPath();
				XMLFile file = new XMLFile(filePath);
				
				String text = "<MainGame>\n<Screen>\n<Size w= \""+ sizePanel.getW()+"\""+" h=\""+sizePanel.getH()+"\""+"></Size>\n";
				text+="</Screen>\n";
				text+="<GamePanel>\n<Bg>"+imageDescription+"</Bg>\n";
				text+="<Sound>"+soundFile+"</Sound>\n";
				text+="<ActiveScreen>\n";
				file.writeFile(text, XMLFile.file);
				
				EnemyObj enemyObj = new EnemyObj();
				file.writeFile(enemyObj.toString(), XMLFile.file);
				
				PlayerObj playerObj = new PlayerObj();
				file.writeFile(playerObj.toString(), XMLFile.file);
				
				ShieldBlockObj shieldBlockObj = new ShieldBlockObj();
				file.writeFile(shieldBlockObj.toString(), XMLFile.file);
				
				text ="</ActiveScreen>\n</GamePanel>\n</MainGame>";
				file.writeFile(text, XMLFile.file);

				
				

			}


		}
	}
}


class SizePanel extends JPanel {
	private int w, h;
	private JTextField widthTextField = new JTextField(10);
	private JTextField heightTextField = new JTextField(10);
	
	public SizePanel() {
		setLayout(new GridLayout(3,2));
		add(new JLabel("Max Width:800, Min Width:900"));
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