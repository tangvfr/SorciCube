package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.bson.Document;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.handler.HandlerServer;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.util.Format;

public class ServerPanel extends JScrollPane {

	private static final long serialVersionUID = -7377720399280596193L;
	
	private final HandlerServer handler;
	private final JTable table;
	private volatile Vector<Document> list;
	
	public ServerPanel(SorciClient client) throws IOException, ReponseRequestException, RequestException {
		this.handler = new HandlerServer(client);
		this.list = new Vector<Document>();
		//table
		this.table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//refresh
		Dimension dim = new Dimension(300, 30);
		JButton refresh = new JButton("Refresh");
		refresh.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					refresh();
				} catch (IOException | ReponseRequestException | RequestException e1) {
					JOptionPane.showMessageDialog(ServerPanel.this, e1.getMessage(), "Error Refresh", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		refresh.setPreferredSize(dim);
		JButton stop = new JButton("Stop API");
		stop.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					short m = Short.MAX_VALUE/2;
					String valid = Integer.toString((int) (Math.random()*m)+m);
					if (valid.equals(JOptionPane.showInputDialog(ServerPanel.this, "Enter this number \""+valid+"\" for stop API", "Valid Stop API", JOptionPane.WARNING_MESSAGE))) {
						handler.stopServer();
					} else {
						JOptionPane.showMessageDialog(ServerPanel.this, "Stoping server canceled !", "Canceled Stop API", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException | ReponseRequestException | RequestException e1) {
					JOptionPane.showMessageDialog(ServerPanel.this, e1.getMessage(), "Error Stop API", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		stop.setPreferredSize(dim);
		//style
		JPanel pan = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(3, 0, 3, 0);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pan.add(table, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		pan.add(refresh, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		pan.add(stop, c);
		
		this.setViewportView(pan);
		refresh();
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
		this.list = new Vector<Document>(handler.getSpigotServerList());
		this.table.setModel(new ServerPanelTable());
		this.repaint();
	}
	
	private class ServerPanelTable extends AbstractTableModel {

		private static final long serialVersionUID = 271451896582449130L;
		
		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return list.size()+2;
		}

		private final String[] HEAD = new String[] {"Server Name", "Time Connected", "Players"};
		
		private int totalPlayers() {
			int total = 0;
			for (int i = 0; i < list.size(); i++)
				total += list.get(i).getInteger("players", 0);
			return total;
		}
		
		@Override
		public Object getValueAt(int row, int column) {
			if (row == 0)
				return "<html><body><span color='#5555FF'>"+HEAD[column]+"</span></body></html>";
			else if (row == list.size()+1) {
				if (column == 2)
					return "<html><body><span color='#FF5555'>"+totalPlayers()+"</span></body></html>";
				else
					return "<html><body><span color='#FF5555'>--</span></body></html>";
			} else {
				if (column == 0)
					return list.get(row-1).getString("name");
				else if (column == 1)
					return Format.formatTime(Long.parseLong(list.get(row-1).getString("time_connected"), 16));
				else
					return Integer.toString(list.get(row-1).getInteger("players", 0));
			}
		}
		
	}
	
}
