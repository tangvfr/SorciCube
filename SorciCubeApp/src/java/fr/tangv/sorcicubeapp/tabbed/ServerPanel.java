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
	
	public ServerPanel(SorciClient client) throws IOException, ReponseRequestException, RequestException {
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
		stop.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String valid = Integer.toString((int) (Math.random()*Short.MAX_VALUE));
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
		//style
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.add(table);
		this.add(refresh);
		this.add(stop);
		refresh();
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
		for (Document doc : handler.getSpigotServerList())
			System.out.println(doc.toJson());
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
			else {
				if (column == 0)
					return list.get(row-1).getString("name");
				else
					return formatTime(Long.parseLong(list.get(row-1).getString("time_connected"), 16));
			}
		}
		
		private final String formatTime(long time) {
			
			return time+"";
		}
		
	}
	
}
