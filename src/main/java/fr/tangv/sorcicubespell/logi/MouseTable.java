package fr.tangv.sorcicubespell.logi;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardMaterial;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardSkin;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.logi.dialog.DialogBase;
import fr.tangv.sorcicubespell.logi.dialog.DialogCombo;
import fr.tangv.sorcicubespell.logi.dialog.DialogSkin;
import fr.tangv.sorcicubespell.util.TextList;

public class MouseTable extends ClickListener {

	private JTable table;
	private CardsPanel cardsPanel;
	private Window parent;
	
	public MouseTable(JTable table, CardsPanel cardsPanel, Window parent) {
		this.table = table;
		this.cardsPanel = cardsPanel;
		this.parent = parent;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = table.getSelectedColumn();
		int row = table.getSelectedRow();
		if (table.getModel() instanceof ModelEditCard && col == 1) {
			ModelEditCard edit = (ModelEditCard) table.getModel();
			Card card = edit.getCard();
			switch (row) {
				case 0:
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(card.getUUID().toString()), null);
					JOptionPane.showMessageDialog(table, "UUID of card was copied !", "Card UUID", JOptionPane.INFORMATION_MESSAGE);
					break;
			
				case 1:
					new DialogBase<JTextField>(parent, "Name", new JTextField(card.getName())) {
						private static final long serialVersionUID = -4613034932047272121L;

						@Override
						public void eventOk(JTextField comp) {
							card.setName(comp.getText());
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					break;
					
				case 2:
					JTextField textType = new JTextField(card.getType().name());
					textType.setEditable(false);
					new DialogBase<JTextField>(parent, "Type", textType) {
						private static final long serialVersionUID = -4613094932047272122L;
						@Override
						public void eventOk(JTextField comp) {}
					};
					break;
					
				case 3:
					if (card.getMaterial().hasSkin())
						new DialogSkin(parent, "Material", card.getMaterial().getSkin(), true) {
							private static final long serialVersionUID = -4613034932047272123L;

							@Override
							public void eventOk(JTextField comp) {
								eventOkMaterial(comp, card, this);
							}
						};
					else
						new DialogBase<JTextField>(parent, "Material", new JTextField(card.getMaterial().toString())) {
							private static final long serialVersionUID = -4613034932047272124L;
	
							@Override
							public void eventOk(JTextField comp) {
								eventOkMaterial(comp, card, this);
							}
						};
					break;
					
				case 4:
					new DialogCombo<CardRarity>(parent, "Rarity", card.getRarity()) {
						private static final long serialVersionUID = -4807395720412058129L;

						@Override
						public void eventOk(CardRarity enumCombo) {
							card.setRarity(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					break;
					
				case 5:
					new DialogCombo<CardFaction>(parent, "Faction", card.getFaction()) {
						private static final long serialVersionUID = -4807395720412058130L;

						@Override
						public void eventOk(CardFaction enumCombo) {
							card.setFaction(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					break;

				case 6:
					new DialogCombo<CardCible>(parent, "Cible", card.getCible()) {
						private static final long serialVersionUID = -4807395720412058131L;

						@Override
						public void eventOk(CardCible enumCombo) {
							card.setCible(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					break;
					
				case 7:
					new DialogCombo<CardFaction>(parent, "Cible Faction", card.getCibleFaction()) {
						private static final long serialVersionUID = -4807395720412058129L;

						@Override
						public void eventOk(CardFaction enumCombo) {
							card.setCibleFaction(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					break;
				
				case 8:
					new DialogBase<JSpinner>(parent, "Mana", new JSpinner(new SpinnerNumberModel(card.getMana(), 0, Integer.MAX_VALUE, 1))) {
						private static final long serialVersionUID = -4613034932048272120L;

						@Override
						public void eventOk(JSpinner comp) {
							card.setMana((int) comp.getValue());
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					break;
					
				case 9:
					Card cardC = card.clone();
					DialogBase<FeaturesTable> dialog = new DialogBase<FeaturesTable>(parent, "Features",
							new FeaturesTable(cardsPanel, cardC.getFeatures(), cardC.getType() == CardType.ENTITY), new Dimension(500, 300)) {
						private static final long serialVersionUID = -4613024932048272120L;

						@Override
						public void eventOk(FeaturesTable comp) {
							card.setFeatures(comp.getCardFeatures());
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
						
						@Override
						protected void initComp(FeaturesTable comp) {
							comp.init(this);
						}
					};
					dialog.setResizable(true);
					break;
					
				case 10:
					DialogBase<JTextArea> dialogTextArea = new DialogBase<JTextArea>(parent, "Description",
							new JTextArea(TextList.listToText(card.getDescription())), new Dimension(500, 300)) {
						private static final long serialVersionUID = 6649269953841487465L;

						@Override
						public void eventOk(JTextArea comp) {
							card.setDescription(TextList.textToList(comp.getText()));
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					dialogTextArea.setResizable(true);
					break;
					
				case 11:
					new DialogBase<JCheckBox>(parent, "Orignal Name",
							new JCheckBox("", card.isOriginalName())) {
						private static final long serialVersionUID = 664926995384148851L;

						@Override
						public void eventOk(JCheckBox comp) {
							card.setOriginalName(comp.isSelected());
							cardsPanel.getCards().update(card);
							cardsPanel.refrech();
						}
					};
					break;
						
				default:
					Object ob = edit.getValueAt(row, col);
					if (ob != null)
						JOptionPane.showMessageDialog(table, "name: "+ob.getClass().getSimpleName()+"\nvalue: "+ob.toString(), "Feature card no editable !", JOptionPane.INFORMATION_MESSAGE);
					break;
			}
		}
	}
	
	
	
	private void eventOkMaterial(JTextField comp, Card card, DialogBase<?> base) {
		CardMaterial material = null;
		String text = comp.getText();
		if (text.startsWith("skull: ")) {
			try {
				int id = Integer.parseInt(text.replaceFirst("skull: ", ""));
				material = new CardMaterial(CardSkin.createCardSkin(id));
			} catch (Exception e) {}
		} else {
			String[] split = text.split(":");
			if (split.length == 2 || split.length == 1) {
				try {
					int id = Integer.parseInt(split[0]);
					int data = 0;
					if (split.length == 2)
						data = Byte.parseByte(split[1]);
					material = new CardMaterial(id, data);
				} catch (Exception e) {}
			}
		}
		if (material != null) {
			card.setMaterial(material);
			cardsPanel.getCards().update(card);
			cardsPanel.refrech();
		} else {
			JOptionPane.showMessageDialog(base, "Error invalid Material", "Error Card Material", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
