package fr.tangv.sorcicubespell.logi;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubespell.cards.CardFeature;
import fr.tangv.sorcicubespell.cards.CardFeatureType;
import fr.tangv.sorcicubespell.cards.CardFeatures;
import fr.tangv.sorcicubespell.cards.CardValue;
import fr.tangv.sorcicubespell.logi.dialog.DialogBase;
import fr.tangv.sorcicubespell.logi.dialog.DialogCombo;

public class FeaturesTable extends JTable {

	private static final long serialVersionUID = 3573798367579198241L;
	private CardFeatures cardFeatures;
	private boolean isEntity;
	private Map<Integer, String> mapName;
	
	public FeaturesTable(Window window, CardFeatures cardFeatures, boolean isEntity) {
		this.cardFeatures = cardFeatures;
		this.isEntity = isEntity;
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.init(window);
	}
	
	private void init(Window window) {
		//sort list
		ArrayList<CardFeature> list = new ArrayList<CardFeature>(cardFeatures.values());
		list.sort(new Comparator<CardFeature>() {
			@Override
			public int compare(CardFeature f1, CardFeature f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		list.sort(new Comparator<CardFeature>() {
			@Override
			public int compare(CardFeature f1, CardFeature f2) {
				return f1.getType().compareTo(f2.getType());
			}
		});
		//init mapname
		this.mapName = new HashMap<Integer, String>();
		int i = 0;
		for (CardFeature feature : list) {
			mapName.put(i, feature.getName());
			i++;
		}
		this.setModel(new FeaturesTableModel(this));
		JPopupMenu popManage = new JPopupMenu();
		JMenuItem itemAddFeature = new JMenuItem("Add feature");
		itemAddFeature.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == 1001) {
					new DialogCombo<CardFeatureType>(window, "Add Feature", CardFeatureType.SKIN) {
						private static final long serialVersionUID = 1721551878121090783L;

						@Override
						public void eventOk(CardFeatureType enumCombo) {
							String name = enumCombo.name().toLowerCase()+"_noname";
							if (!cardFeatures.hasFeature(name)) {
								cardFeatures.putFeature(new CardFeature(name, enumCombo, CardValue.createCardValue(enumCombo.getTypeValue())));
								FeaturesTable.this.init(window);
								FeaturesTable.this.repaint();
							} else {
								JOptionPane.showMessageDialog(FeaturesTable.this, "Name alredy exist !", "Add Feature", JOptionPane.ERROR_MESSAGE);
							}
						}
					};
				}
			}
		});
		popManage.add(itemAddFeature);
		JMenuItem itemRemoveFeature = new JMenuItem("Remove feature");
		itemRemoveFeature.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == 1001) {
					int id = getSelectedRow()-1;
					if (id >= 0) {
						String name = mapName.get(id);
						if (isEntity && (name.equals("Health") || name.equals("AttackDamage")))
							return;
						if (cardFeatures.hasFeature(name)) {
							if (0 == JOptionPane.showConfirmDialog(FeaturesTable.this, "Are you sure to delete this feature !", "Remove Feature", JOptionPane.WARNING_MESSAGE)) {
								cardFeatures.removeFeature(name);
								FeaturesTable.this.init(window);
								FeaturesTable.this.repaint();
							}
						} else {
							JOptionPane.showMessageDialog(FeaturesTable.this, "Feature not exist !", "Remove Feature", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		popManage.add(itemRemoveFeature);
		JMenuItem itemRenameFeature = new JMenuItem("Rename feature");
		itemRenameFeature.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == 1001) {
					int id = getSelectedRow()-1;
					if (id >= 0) {
						String name = mapName.get(id);
						if (cardFeatures.hasFeature(name)) {
							CardFeature feature = cardFeatures.getFeature(name);
							new DialogBase<JTextField>(window, "Rename Feature", new JTextField(feature.getName())) {
								private static final long serialVersionUID = 1731551878121090783L;
		
								@Override
								public void eventOk(JTextField jTextField) {
									String newName = jTextField.getText();
									if (!cardFeatures.hasFeature(newName)) {
										cardFeatures.renameFeature(feature, newName);
										FeaturesTable.this.init(window);
										FeaturesTable.this.repaint();
									} else {
										JOptionPane.showMessageDialog(FeaturesTable.this, "Name alredy exist !", "Rename Feature", JOptionPane.ERROR_MESSAGE);
									}
								}
							};
						} else {
							JOptionPane.showMessageDialog(FeaturesTable.this, "Feature not exist !", "Rename Feature", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		popManage.add(itemRenameFeature);
		this.setComponentPopupMenu(popManage);
		//add click event
	}
	
	public String getName(int id) {
		return mapName.get(id);
	}
	
	public CardFeatures getCardFeatures() {
		return cardFeatures;
	}
	
	public boolean isEntity() {
		return isEntity;
	}
	
}
