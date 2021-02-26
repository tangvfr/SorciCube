package fr.tangv.sorcicubeapp.card;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import fr.tangv.sorcicubeapp.dialog.DialogBase;
import fr.tangv.sorcicubeapp.dialog.DialogCombo;
import fr.tangv.sorcicubeapp.dialog.DialogSkin;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardMaterial;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardSkin;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.util.TextList;

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
					new DialogBase<JTextField>(parent, "Edit Card", "Name", new JTextField(card.getName())) {
						private static final long serialVersionUID = -4613034932047272121L;

						@Override
						public void eventOk(JTextField comp) throws IOException, ReponseRequestException, RequestException {
							card.setName(comp.getText());
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
					};
					break;
					
				case 2:
					JTextField textType = new JTextField(card.getType().name());
					textType.setEditable(false);
					new DialogBase<JTextField>(parent, "Edit Card", "Type", textType) {
						private static final long serialVersionUID = -4613094932047272122L;
						@Override
						public void eventOk(JTextField comp) throws IOException, ReponseRequestException, RequestException {}
					};
					break;
					
				case 3:
					if (card.getMaterial().hasSkin())
						new DialogSkin(parent, "Edit Card", "Material", card.getMaterial().getSkin(), true) {
							private static final long serialVersionUID = -4613034932047272123L;

							@Override
							public void eventOk(JTextField comp) throws IOException, ReponseRequestException, RequestException {
								eventOkMaterial(comp, card, this);
							}
						};
					else
						new DialogBase<JTextField>(parent, "Edit Card", "Material", new JTextField(card.getMaterial().toString())) {
							private static final long serialVersionUID = -4613034932047272124L;
	
							@Override
							public void eventOk(JTextField comp) throws IOException, ReponseRequestException, RequestException {
								eventOkMaterial(comp, card, this);
							}
						};
					break;
					
				case 4:
					new DialogCombo<CardRarity>(parent, "Edit Card", "Rarity", card.getRarity()) {
						private static final long serialVersionUID = -4807395720412058129L;

						@Override
						public void eventOk(CardRarity enumCombo) throws IOException, ReponseRequestException, RequestException {
							card.setRarity(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
					};
					break;
					
				case 5:
					new DialogCombo<CardFaction>(parent, "Edit Card", "Faction", card.getFaction()) {
						private static final long serialVersionUID = -4807395720412058130L;

						@Override
						public void eventOk(CardFaction enumCombo) throws IOException, ReponseRequestException, RequestException {
							card.setFaction(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
					};
					break;

				case 6:
					new DialogCombo<CardCible>(parent, "Edit Card", "Cible", card.getCible()) {
						private static final long serialVersionUID = -4807395720412058131L;

						@Override
						public void eventOk(CardCible enumCombo) throws IOException, ReponseRequestException, RequestException {
							card.setCible(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
					};
					break;
					
				case 7:
					new DialogCombo<CardFaction>(parent, "Edit Card", "Cible Faction", card.getCibleFaction()) {
						private static final long serialVersionUID = -4807395720412058129L;

						@Override
						public void eventOk(CardFaction enumCombo) throws IOException, ReponseRequestException, RequestException {
							card.setCibleFaction(enumCombo);
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
					};
					break;
				
				case 8:
					new DialogBase<JSpinner>(parent, "Edit Card", "Mana", new JSpinner(new SpinnerNumberModel(card.getMana(), 0, Integer.MAX_VALUE, 1))) {
						private static final long serialVersionUID = -4613034932048272120L;

						@Override
						public void eventOk(JSpinner comp) throws IOException, ReponseRequestException, RequestException {
							card.setMana((int) comp.getValue());
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
					};
					break;
					
				case 9:
					Card cardC = card.clone();
					DialogBase<FeaturesTable> dialog = new DialogBase<FeaturesTable>(parent, "Edit Card", "Features",
							new FeaturesTable(cardsPanel, cardC.getFeatures(), cardC.getType() == CardType.ENTITY), new Dimension(500, 300)) {
						private static final long serialVersionUID = -4613024932048272120L;

						@Override
						public void eventOk(FeaturesTable comp) throws IOException, ReponseRequestException, RequestException {
							card.setFeatures(comp.getCardFeatures());
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
						
						@Override
						protected void initComp(FeaturesTable comp) {
							comp.init(this);
						}
					};
					dialog.setResizable(true);
					break;
					
				case 10:
					DialogBase<JTextArea> dialogTextArea = new DialogBase<JTextArea>(parent, "Edit Card", "Description",
							new JTextArea(TextList.listToText(card.getDescription())), new Dimension(500, 300)) {
						private static final long serialVersionUID = 6649269953841487465L;

						@Override
						public void eventOk(JTextArea comp) throws IOException, ReponseRequestException, RequestException {
							card.setDescription(TextList.textToList(comp.getText()));
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
						}
					};
					dialogTextArea.setResizable(true);
					break;
					
				case 11:
					new DialogBase<JCheckBox>(parent, "Edit Card", "Orignal Name",
							new JCheckBox("", card.isOriginalName())) {
						private static final long serialVersionUID = 664926995384148851L;

						@Override
						public void eventOk(JCheckBox comp) throws IOException, ReponseRequestException, RequestException {
							card.setOriginalName(comp.isSelected());
							cardsPanel.getCards().update(card);
							cardsPanel.refresh();
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
	
	
	
	private void eventOkMaterial(JTextField comp, Card card, DialogBase<?> base) throws IOException, ReponseRequestException, RequestException {
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
			cardsPanel.refresh();
		} else {
			JOptionPane.showMessageDialog(base, "Error invalid Material", "Error Card Material", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
