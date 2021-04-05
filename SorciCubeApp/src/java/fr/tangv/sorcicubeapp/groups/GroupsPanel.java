package fr.tangv.sorcicubeapp.groups;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.tabbed.SearchPanel;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.handler.HandlerGroups;
import fr.tangv.sorcicubecore.player.Group;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class GroupsPanel extends SearchPanel<Group> {

	private static final long serialVersionUID = 2516520893332579991L;
	private final HandlerGroups handler;
	private final GroupPanel groupPanel;
	
	public GroupsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException {
		super(client, logi);
		this.handler = new HandlerGroups(client);
		this.groupPanel = new GroupPanel(this);
		//popmenu
		JMenuItem create = new JMenuItem("Create Group");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == ActionEvent.ACTION_FIRST) {
					String name = JOptionPane.showInputDialog(logi.getFramePanel(), "Name for group new", "Create Group", JOptionPane.QUESTION_MESSAGE);
					if (name != null && !name.isEmpty())
						try {
							handler.add(new Group(name, "§8[§f"+name+"§8]", 0, new Vector<String>()));
							refresh();
						} catch (IOException | ReponseRequestException | RequestException e1) {
							JOptionPane.showMessageDialog(logi.getFramePanel(), "Error: "+e1.getMessage(), "Create Group", JOptionPane.ERROR_MESSAGE);
						}
				}
			}
		});
		JMenuItem rename = new JMenuItem("Rename Group");
		rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == ActionEvent.ACTION_FIRST) {
					Group group = list.getSelectedValue();
					if (group != null) {
						String name = JOptionPane.showInputDialog(logi.getFramePanel(), "Name new for group", "Rename Group", JOptionPane.QUESTION_MESSAGE);
						if (name != null && !name.isEmpty())
							try {
								handler.remove(group);
								handler.add(new Group(name, group.getDisplay(), group.getWeight(), group.getPerms()));
								refresh();
							} catch (IOException | ReponseRequestException | RequestException e1) {
								JOptionPane.showMessageDialog(logi.getFramePanel(), "Error: "+e1.getMessage(), "Rename Group", JOptionPane.ERROR_MESSAGE);
							}
					} else {
						JOptionPane.showMessageDialog(logi.getFramePanel(), "No selected group ?", "Rename Packet", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JMenuItem delete = new JMenuItem("Remove Group");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == ActionEvent.ACTION_FIRST) {
					Group group = list.getSelectedValue();
					if (group != null) {
						if (0 == JOptionPane.showConfirmDialog(logi.getFramePanel(), "Are you sure remove group nommed \""+group.getName()+"\" ?", "Remove Group", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
							try {
								handler.remove(group);
								refresh();
							} catch (IOException | ReponseRequestException | RequestException e1) {
								JOptionPane.showMessageDialog(logi.getFramePanel(), "Error: "+e1.getMessage(), "Remove Packet", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(logi.getFramePanel(), "No selected group ?", "Remove Packet", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		//menu add
		menu.add(create);
		menu.add(rename);
		menu.add(delete);
		this.addMainComponent(groupPanel);
		refresh();
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Group> list, Group group, int index, boolean isSelected, boolean cellHasFocus) {
		return new JLabel(
				"<html><body><span>"+(isSelected ? ">" : "")
				+group.getWeight()+" | "+group.getName()+" | </span>"
				+ColorMCToHTML.replaceColor(group.getDisplay())
				+"</html></body>"
			);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		groupPanel.showGroup(list.getSelectedValue());
		groupPanel.repaint();
	}

	@Override
	public void refresh() {
		try {
			String nameSelect = this.groupPanel.getGroupName();
			handler.refresh();
			int max = 0;
			Vector<Group> list = new Vector<Group>();
			Group g = null;
			String searchName = search.getText().toLowerCase();
			for (Group group : handler.cloneValues()) {
				String name = group.getName();
				if (name.equals(nameSelect))
					g = group;
				if (searchName.isEmpty() || name.toLowerCase().contains(searchName))
					list.add(group);
				max++;
			}
			this.list.setListData(list);
			if (g != null)
				this.list.setSelectedValue(g, true);
			groupPanel.showGroup(g);
			refresh.setText("Refresh | "+max+" groups "+list.size()+" find");
			this.repaint();
		} catch (IOException | ReponseRequestException | RequestException e) {
			warningBug(e, "refresh");
		}
	}
	
	public void update(Group group) {
		try {
			handler.update(group);
		} catch (IOException | ReponseRequestException | RequestException e) {
			warningBug(e, "update");
		}
	}

	public Vector<String> cloneListWithEmpty() {
		Vector<String> list = new Vector<String>();
		list.add("");
		for (Group group : handler.cloneValues())
			list.add(group.getName());
		return list;
	}
	
}
