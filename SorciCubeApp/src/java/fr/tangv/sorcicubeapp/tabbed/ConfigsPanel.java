package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JOptionPane;

import fr.tangv.sorcicubeapp.config.AbstractConfigPanel;
import fr.tangv.sorcicubeapp.config.MainConfigPanel;
import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.configs.EnumConfig;
import fr.tangv.sorcicubecore.handler.HandlerConfig;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class ConfigsPanel extends MainConfigPanel {

	private static final long serialVersionUID = -1896197255749294906L;
	private final HandlerConfig handler;
	private final FrameLogi logi;
	
	public ConfigsPanel(SorciClient client, FrameLogi logi) throws IOException, ResponseRequestException, RequestException, ConfigParseException {
		super();
		this.logi = logi;
		this.handler = new HandlerConfig(client);
		refresh();
	}

	public HandlerConfig getHandler() {
		return handler;
	}

	public void refresh() {
		try {
			handler.refreshConfig();
			EnumConfig enums = handler.getConfig().enums;
			CardFaction.initColors(enums.factionColor);
			CardRarity.initColors(enums.rarityColor);
			setView(new AbstractConfigPanel(this, null, "Config", handler.getConfig()));
		} catch (IOException | ResponseRequestException | RequestException | ConfigParseException e) {
			logi.showConnection("Error: "+e.getMessage(), Color.RED);
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			handler.uploadConfig();
		} catch (IOException | ResponseRequestException | RequestException | ConfigParseException e) {
			JOptionPane.showMessageDialog(logi.getFramePanel(), "Error: "+e.getMessage(), "Save Config", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void cancel() {
		refresh();
	}

}
