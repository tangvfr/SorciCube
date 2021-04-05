package fr.tangv.sorcicubeapp.groups;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.player.Group;

public class GroupPanel extends JScrollPane {

	private static final long serialVersionUID = 2848497329580995498L;
	private volatile Group group;
	private final JPanel empty;
	private final JPanel panel;
	private final JButton apply;
	private final JButton cancel;

	public GroupPanel(GroupsPanel manager) {
		this.empty = new JPanel();
		this.panel = new JPanel();
		//pakcet

		//button
		this.apply = new JButton("Save");
		this.apply.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Group g = createGroup();
				manager.update(g);
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

		pan.add(btns);
		this.panel.add(pan);
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
					"<html><body><span>"
					+group.getName()+" | </span>"
					+ColorMCToHTML.replaceColor(group.getDisplay())
					+"</html></body>"
				));
			this.setViewportView(panel);
		}
	}
	
	public void initValue() {
		
		
	}
	
	public Group createGroup() {
		return new Group(group.getName(), "", 0, null);
	}
	
}