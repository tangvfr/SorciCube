package fr.tangv.sorcicubeapp.tabbed;

import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.bson.Document;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.handler.HandlerServer;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class ServerPanel extends JPanel {

	private static final long serialVersionUID = -7377720399280596193L;
	
	private final HandlerServer handler;
	private final JTable table;
	private volatile Vector<Document> list;
	
	public ServerPanel(SorciClient client) {
		this.handler = new HandlerServer(client);
		this.list = new Vector<Document>();
		this.table = new JTable(new ServerPanelTable());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//refresh
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
		JButton stop = new JButton("Stop API");
		
		//style
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.add(table);
		this.add(refresh);
		this.add(stop);
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
		this.list = new Vector<Document>(handler.getSpigotServerList());
		this.repaint();
	}
	
	private class ServerPanelTable extends AbstractTableModel {

		private static final long serialVersionUID = 271451896582449130L;

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return list.size()+1;
		}

		private final String[] HEAD = new String[] {"Server Name", "Time Connected"};
		
		@Override
		public Object getValueAt(int row, int column) {
			if (row == 0)
				return HEAD[column];
			else
				if (column == 0)
					return list.get(row-1).getString("name");
				else
					return list.get(row-1).getString("time_connected");
		}
		
	}
	
}
