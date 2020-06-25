package fr.tangv.sorcicubespell.logi;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardFeatures;
import fr.tangv.sorcicubespell.card.CardValue;
import fr.tangv.sorcicubespell.card.CardValue.TypeValue;
import fr.tangv.sorcicubespell.logi.dialog.DialogBase;
import fr.tangv.sorcicubespell.logi.dialog.DialogCombo;

public class FeaturesTable extends JTable {

	private static final long serialVersionUID = 3573798367579198241L;
	private CardFeatures cardFeatures;
	private boolean isEntity;
	private Map<Integer, String> mapName;
	private Window window;
	
	public FeaturesTable(CardFeatures cardFeatures, boolean isEntity) {
		this.cardFeatures = cardFeatures;
		this.isEntity = isEntity;
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setVisible(false);
		this.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (getSelectedColumn() == 2) {
					int id = getSelectedRow()-1;
					if (id >= 0) {
						String name = mapName.get(id);
						if (cardFeatures.hasFeature(name)) {
							CardFeature feature = cardFeatures.getFeature(name);
							CardValue value = feature.getValue();
							if (value.getType() == TypeValue.BOOL) {
								new DialogBase<JCheckBox>(window, "Value", new JCheckBox("", value.asBollean())) {
									private static final long serialVersionUID = 4116920655857733839L;

									@Override
									public void eventOk(JCheckBox comp) {
										feature.setValue(new CardValue(comp.isSelected()));
										FeaturesTable.this.init(window);
										FeaturesTable.this.repaint();
									}
								};
							} else if (value.getType() == TypeValue.NUMBER) {
								new DialogBase<JSpinner>(window, "Value", new JSpinner(new SpinnerNumberModel(value.asInt(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1))) {
									private static final long serialVersionUID = 4116920655857733840L;

									@Override
									public void eventOk(JSpinner comp) {
										feature.setValue(new CardValue((int) comp.getValue()));
										FeaturesTable.this.init(window);
										FeaturesTable.this.repaint();
									}
								};
							} else if (value.getType() == TypeValue.TEXT) {
								new DialogBase<JTextField>(window, "Value", new JTextField(value.asString())) {
									private static final long serialVersionUID = 4116920655857733839L;

									@Override
									public void eventOk(JTextField comp) {
										feature.setValue(new CardValue(comp.getText()));
										FeaturesTable.this.init(window);
										FeaturesTable.this.repaint();
									}
								};
							}
						} else {
							JOptionPane.showMessageDialog(FeaturesTable.this, "Feature not exist !", "Edit Value Feature", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
	}
	
	public void init(Window window) {
		this.window = window;
		this.setVisible(true);
		//sort list
		ArrayList<CardFeature> list = new ArrayList<CardFeature>(cardFeatures.listFeatures());
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
						if (isEntity && (name.equals("Health") || name.equals("AttackDamage")))
							return;
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
