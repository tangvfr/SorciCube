package fr.tangv.sorcicubeapp.card;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.card.Card;

public class UUIDPanelEditor extends JPanel {

	private static final long serialVersionUID = 6292444212076707013L;
	private CardsPanel cardsPanel;
	private JTextField fieldUUID;
	private JButton btnShow;
	private JTextField field;
	private Window parent;
	private String textLabel;
	private Card card;
	
	public UUIDPanelEditor(CardsPanel cardsPanel, UUID uuid) {
		this.cardsPanel = cardsPanel;
		this.fieldUUID = new JTextField(uuid.toString());
		this.fieldUUID.setEditable(false);
		this.card = cardsPanel.getCards().getCard(uuid);
		this.textLabel = (card != null) ? PanelNav.renderHTMLCard(card, "Card: ") : "Card is not existing";
		this.btnShow = new JButton("Show Card");
		btnShow.setFocusable(false);
		if (card != null)
			btnShow.addMouseListener(new ClickUUIDPanelEditor());
		else
			btnShow.setEnabled(false);
		this.field = new JTextField(uuid.toString());
		//display
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(textLabel));
		JPanel panel1 = new JPanel();
		panel1.add(btnShow, BorderLayout.WEST);
		panel1.add(fieldUUID, BorderLayout.CENTER);
		JPanel panel2 = new JPanel();
		panel2.add(new JLabel("New UUID: "), BorderLayout.WEST);
		panel2.add(field, BorderLayout.CENTER);
		this.add(panel1, BorderLayout.NORTH);
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	public void setParent(Window parent) {
		this.parent = parent;
	}
	
	public UUID getCardUUID() throws Exception {
		UUID uuid;
		try {
			uuid = UUID.fromString(field.getText());
		} catch (Throwable e) {
			throw new Exception("\""+field.getText()+"\" is not UUID !");
		}
		Card card = cardsPanel.getCards().getCard(uuid);
		if (card == null)
			throw new Exception("Don't existing card for the UUID \""+uuid.toString()+"\" !");
		return uuid;
	}
	
	private class ClickUUIDPanelEditor extends ClickListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			//init frame
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setTitle("Card: "+card.getUUID().toString());
			//init in
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(textLabel));
			panel.setLayout(new BorderLayout());
			JTable table = new JTable();
			table.addMouseListener(new MouseTable(table, cardsPanel, frame));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setModel(new ModelEditCard(card));
			panel.add(table);
			frame.setContentPane(panel);
			//show
			frame.pack();
			frame.setSize(500, frame.getHeight());
			parent.setEnabled(false);
			frame.addWindowListener(new WindowListener() {
				@Override public void windowOpened(WindowEvent e) {}
				@Override public void windowIconified(WindowEvent e) {}
				@Override public void windowDeiconified(WindowEvent e) {}
				@Override public void windowDeactivated(WindowEvent e) {}
				@Override
				public void windowClosing(WindowEvent e) {
					parent.setEnabled(true);
				}
				@Override public void windowClosed(WindowEvent e) {}
				@Override public void windowActivated(WindowEvent e) {}
			});
			frame.setLocationRelativeTo(UUIDPanelEditor.this);
			frame.setVisible(true);
		}
	}
	
}