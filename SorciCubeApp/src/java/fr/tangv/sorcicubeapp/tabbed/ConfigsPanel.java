package fr.tangv.sorcicubeapp.tabbed;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.handler.HandlerConfigYAML;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class ConfigsPanel extends SearchPanel<String> {

	private static final long serialVersionUID = -1896197255749294906L;
	private HandlerConfigYAML handler;
	private JPanel editor;
	private String name;
	private JTextArea text;
	private JScrollPane scroll;
	private JPanel btns;
	
	public ConfigsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException {
		super(client, logi);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
		return new JLabel((isSelected ? "> " : "")+value);
	}

	@Override
	protected JComponent initSelectPanel(SorciClient client, JPopupMenu menu) throws IOException, ReponseRequestException, RequestException {
		this.handler = new HandlerConfigYAML(client);
		this.editor = new JPanel(new BorderLayout());
		this.text = new JTextArea("");
		this.scroll = new JScrollPane(text);
		this.btns = new JPanel(new GridLayout(1, 2, 5, 5));
		//btns
		JButton save = new JButton("Save");
		save.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					System.out.println(name);
					System.out.println(text.getText());
					handler.updateConfig(name, text.getText());
					refresh();
					openConfig(name);
				} catch (IOException | ReponseRequestException | RequestException e1) {
					warningBug(e1, "save config");
				}
			}
		});
		btns.add(save);
		JButton cancel = new JButton("Cancel");
		cancel.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refresh();
			}
		});
		btns.add(cancel);
		return editor;
	}

	public void openConfig(String name) throws IOException, ReponseRequestException, RequestException {
		this.name = name;
		this.text.setText(handler.getConfig(name));
		editor.setBorder(new TitledBorder("Config: "+name));
		editor.add(this.scroll, BorderLayout.CENTER);
		editor.add(this.btns, BorderLayout.SOUTH);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			openConfig(list.getSelectedValue());
		} catch (IOException | ReponseRequestException | RequestException e1) {
			warningBug(e1, "open config");
		}
	}
	
	@Override
	public void refresh() {
		try {
			editor.setBorder(null);
			this.editor.removeAll();
			String[] l = handler.listConfig(); 
			this.list.setListData(l);
			this.refresh.setText("Refresh | "+l.length+" configs");
			this.repaint();
		} catch (IOException | ReponseRequestException | RequestException e) {
			warningBug(e, "refresh");
		}
	}

}
