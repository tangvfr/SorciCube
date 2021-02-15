package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class PlayersPanel extends SearchPanel<PlayerFeature> {

	private static final long serialVersionUID = 933468304672805743L;
	private HandlerPlayers handler;
	
	public PlayersPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException {
		super(client, logi);
	}
	
	@Override
	protected JComponent initSelectPanel(SorciClient client, JPopupMenu menu) throws IOException, ReponseRequestException, RequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends PlayerFeature> list, PlayerFeature player, int index, boolean isSelected, boolean hasFocus) {
		
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
