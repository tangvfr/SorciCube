package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.manager.ManagerCards;

public class UUIDPanelEditor extends JPanel {

	private static final long serialVersionUID = 6292444212076707013L;
	private ManagerCards cards;
	private JTextField fieldUUID;
	private JButton btnShow;
	private JTextField field;
	
	public UUIDPanelEditor(ManagerCards cards, UUID uuid) {
		this.cards = cards;
		this.fieldUUID = new JTextField(uuid.toString());
		this.fieldUUID.setEditable(false);
		String textLabel = "Card: ";
		Card card = cards.getCard(uuid);
		if (card != null)
			textLabel = PanelNav.renderHTMLCard(card, textLabel);
		else
			textLabel += "card is not existing";
		this.btnShow = new JButton("Show Card");
		btnShow.setFocusable(false);
		/*
		 this.table = new JTable();
table.addMouseListener(new MouseTable(table, this));
table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
table.setModel(new ModelEditCard(this.carts.getCard(((ModelEditCard) table.getModel()).getCard().getUUID())));
		 */
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
	
	public UUID getCardUUID() throws Throwable {
		UUID uuid;
		try {
			uuid = UUID.fromString(field.getText());
		} catch (Throwable e) {
			throw new Throwable("\""+field.getText()+"\" is not UUID !");
		}
		Card card = cards.getCard(uuid);
		if (card == null)
			throw new Throwable("Card is not existing for the UUID \""+uuid.toString()+"\" !");
		return uuid;
	}
	
}
