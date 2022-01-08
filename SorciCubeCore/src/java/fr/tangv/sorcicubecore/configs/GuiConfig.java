package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;

public class GuiConfig extends AbstractConfig {

	public GuiAdminViewCardsGuiConfig guiAdminViewCards;
	public GuiSelectDeckDefaultGuiConfig guiSelectDeckDefault;
	public GuiEditOrViewGuiConfig guiEditOrView;
	public GuiViewCardsGuiConfig guiViewCards;
	public GuiDecksGuiConfig guiDecks;
	public GuiEditDeckGuiConfig guiEditDeck;
	public GuiSwapCardGuiConfig guiSwapCard;
	public GuiChangeSortGuiConfig guiChangeSort;
	public GuiCreateDeckGuiConfig guiCreateDeck;
	public GuiOpenPacketGuiConfig guiOpenPacket;
	public GuiFightGuiConfig guiFight;
	public BasicGuiConfig guiHistoric;
	public BasicGuiConfig guiViewEntity;
	public BasicGuiConfig guiSwapFight;
	public GuiPlayerGuiConfig guiPlayer;
	public BossBarGuiConfig bossBar;
	public ScoreboardGuiConfig scoreboard;
	public GuiListFightGuiConfig guiListFight;
	public GuiIncreaseDeckGuiConfig guiIncreaseDeck;
	public GuiSellerPacketsGuiConfig guiSellerPackets;
	public GuiSellerItemsGuiConfig guiSellerItems;
	public GuiTrashGuiConfig guiTrash;

	public GuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}