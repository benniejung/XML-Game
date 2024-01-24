package AuthoringTool;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ObjPanel extends JPanel {
	DrawPanel drawPanel;
	private String[] objects = {"Enemy", "Player", "ShieldBlock","Item"};
	private EnemyPanel enemyPanel;
	private PlayerPanel playerPanel;
	private ShieldBlockPanel shieldBlockPanel;
	private JPanel selectedObjPanel;
	public ObjPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
        setLayout(new BorderLayout());

        JComboBox<String> objCombo = new JComboBox<String>();
        for (int i = 0; i < objects.length; i++) {
            objCombo.addItem(objects[i]);
        }

        objCombo.setBounds(100, 10, 100, 30);
        add(objCombo, BorderLayout.NORTH);
        objCombo.addActionListener(new ComboListener());
        
        selectedObjPanel = new JPanel();
        add(selectedObjPanel,BorderLayout.CENTER);
        selectedObjPanel.setLayout(new CardLayout());

        enemyPanel = new EnemyPanel(drawPanel);
        playerPanel = new PlayerPanel(drawPanel);
        shieldBlockPanel = new ShieldBlockPanel(drawPanel);
        enemyPanel.setVisible(true);
        playerPanel.setVisible(true);
        shieldBlockPanel.setVisible(true);
        selectedObjPanel.add(enemyPanel);
    }
	
	class ComboListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> comboBox = (JComboBox<String>) e.getSource();

            // 선택된 아이템 가져오기
            String selectedObj = (String) comboBox.getSelectedItem();
            
            CardLayout cardLayout = (CardLayout) selectedObjPanel.getLayout();
            switch (selectedObj) {
            case "Enemy":
                selectedObjPanel.add("EnemyPanel", enemyPanel);
                cardLayout.show(selectedObjPanel, "EnemyPanel");
                break;
            case "Player":
                selectedObjPanel.add("PlayerPanel", playerPanel);
                cardLayout.show(selectedObjPanel, "PlayerPanel");

                break;
            case "ShieldBlock":
                selectedObjPanel.add("ShieldBlockPanel", shieldBlockPanel);
                cardLayout.show(selectedObjPanel, "ShieldBlockPanel");
                break;
            }        
        }
	}
}
