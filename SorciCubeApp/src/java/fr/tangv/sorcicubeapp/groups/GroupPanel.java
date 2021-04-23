package fr.tangv.sorcicubeapp.groups;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.component.ComponentAreaText;
import fr.tangv.sorcicubeapp.component.ComponentLabel;
import fr.tangv.sorcicubeapp.component.ComponentNumberInt;
import fr.tangv.sorcicubeapp.component.ComponentText;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.player.Group;

public class GroupPanel extends JScrollPane {

	private static final long serialVersionUID = 2848497329580995498L;
	private volatile Group group;
	private final ComponentLabel name;
	private final ComponentText display;
	private final ComponentText prefix;
	private final ComponentNumberInt weight;
	private final ComponentAreaText perms;
	private final JPanel empty;
	private final JPanel panel;
	private final JButton apply;
	private final JButton cancel;

	public GroupPanel(GroupsPanel manager) {
		this.empty = new JPanel();
		this.panel = new JPanel();
		//pakcet
		this.name = new ComponentLabel("Name");
		this.display = new ComponentText("Display");
		this.prefix = new ComponentText("Prefix");
		this.weight = new ComponentNumberInt("Weight", Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
		this.perms = new ComponentAreaText("Permissions");
		perms.setToolTipText("[addgroupperms, -removeperm, addperm");
		//button
		this.apply = new JButton("Save");
		this.apply.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Group g = createGroup();
				manager.put(g);
				manager.refresh();
				showGroup(g);
				GroupPanel.this.repaint();
			}
		});
		this.cancel = new JButton("Cancel");
		this.cancel.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				initValue();
			}
		});
		JPanel btns = new JPanel(new GridLayout(1, 2, 10, 10));
		btns.add(apply);
		btns.add(cancel);
		//panel
		this.panel.setLayout(new GridBagLayout());
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS)); 
		pan.add(name);
		pan.add(display);
		pan.add(weight);
		pan.add(perms);
		pan.add(btns);
		this.panel.add(pan);
		this.getVerticalScrollBar().setUnitIncrement(10);
		showGroup(null);
	}
	
	public String getGroupName() {
		if (group == null)
			return null;
		return group.getName();
	}
	
	public void showGroup(Group group) {
		this.group = group;
		if (group == null) {
			this.setViewportView(empty);
		} else {
			initValue();
			panel.setBorder(new TitledBorder(
					"<html><body><span>"+group.getWeight()+" | "
					+group.getName()+" | </span>"
					+ColorMCToHTML.replaceColor(group.getDisplay())
					+"</html></body>"
				));
			this.setViewportView(panel);
		}
	}
	
	private String permsToString(Vector<String> list) {
		String text = "";
		Iterator<String> ite = list.iterator();
		while (ite.hasNext()) {
			text += ite.next();
			if (ite.hasNext())
				text += "\r\n";
		}
		return text;
	}
	
	private Vector<String> stringToPerms(String perms) {
		Vector<String> list = new Vector<String>();
		for (String perm : perms.replace(" ", "").replace("\t", "").replace("\r", "").split("\n"))
			if (!perm.isEmpty())
				list.add(perm);
		return list;
	}
	
	public void initValue() {
		this.name.setLabel(group.getName());
		this.display.setText(group.getDisplay());
		this.prefix.setText(group.getPrefix());
		this.weight.setInt(group.getWeight());
		this.perms.setArea(permsToString(group.getPerms()));
	}
	
	public Group createGroup() {
		return new Group(name.getLabel(), display.getText(), prefix.getText(), weight.getInt(), stringToPerms(perms.getArea()));
	}
	
}