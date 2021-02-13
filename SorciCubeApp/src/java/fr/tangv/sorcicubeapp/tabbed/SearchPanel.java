package fr.tangv.sorcicubeapp.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

@SuppressWarnings("serial")
public abstract class SearchPanel<T> extends JSplitPane implements ListCellRenderer<T>, MouseListener {
		
	protected final FrameLogi logi;
	protected final JButton refresh;
	protected final JLabel clear;
	protected final JTextField search;
	protected final JList<T> list;
	
	public SearchPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException {
		super(HORIZONTAL_SPLIT);
		this.logi = logi;
		//refresh
		this.refresh = new JButton("");
		this.refresh.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refresh();
			}
		});
		//clear
		this.clear = new JLabel(" X ");
		clear.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search.setText("");
				refresh();
			}
		});
		clear.setForeground(Color.RED);
		//info
		this.search = new JTextField();
		search.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					refresh();
				}
			}
		});
		//main panel
		this.list = new JList<T>();
		this.list.setCellRenderer(this);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addMouseListener(this);
		//makeup
		JPanel panelUp = new JPanel(new BorderLayout());
		panelUp.add(refresh, BorderLayout.NORTH);
		panelUp.add(clear, BorderLayout.WEST);
		panelUp.add(search, BorderLayout.CENTER);
		panelUp.setBorder(new EmptyBorder(0, 0, 5, 0));
		JPanel panelLeft = new JPanel(new BorderLayout());
		panelLeft.add(panelUp, BorderLayout.NORTH);
		panelLeft.add(list, BorderLayout.CENTER);
		this.add(panelLeft, 0);
		JPopupMenu menu = new JPopupMenu();
		this.add(this.initSelectPanel(client, menu), 1);
		this.list.setComponentPopupMenu(menu);
		refresh();
	}
	
	protected void warningBug(Exception e, String action) {
		JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error "+action, JOptionPane.ERROR_MESSAGE);
		logi.showConnection("Error: "+e.getMessage(), Color.MAGENTA);
	}
	
	protected abstract JComponent initSelectPanel(SorciClient client, JPopupMenu menu) throws IOException, ReponseRequestException, RequestException;
	public abstract void refresh();
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
}
