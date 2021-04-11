package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiConfig extends AbstractConfig {

	public GuiAdminViewCardsGuiConfig GUI_ADMIN_VIEW_CARDS;
	public GuiSelectDeckDefaultGuiConfig GUI_SELECT_DECK_DEFAULT;
	public GuiEditOrViewGuiConfig GUI_EDIT_OR_VIEW;
	public GuiViewCardsGuiConfig GUI_VIEW_CARDS;
	public GuiDecksGuiConfig GUI_DECKS;
	public GuiEditDeckGuiConfig GUI_EDIT_DECK;
	public GuiSwapCardGuiConfig GUI_SWAP_CARD;
	public GuiChangeSortGuiConfig GUI_CHANGE_SORT;
	public GuiCreateDeckGuiConfig GUI_CREATE_DECK;
	public GuiOpenPacketGuiConfig GUI_OPEN_PACKET;
	public GuiFightGuiConfig GUI_FIGHT;
	public GuiHistoricGuiConfig GUI_HISTORIC;
	public GuiViewEntityGuiConfig GUI_VIEW_ENTITY;
	public GuiSwapFightGuiConfig GUI_SWAP_FIGHT;
	public GuiPlayerGuiConfig GUI_PLAYER;
	public BossBarGuiConfig BOSS_BAR;
	public ScoreboardGuiConfig SCOREBOARD;
	public GuiListFightGuiConfig GUI_LIST_FIGHT;
	public GuiIncreaseDeckGuiConfig GUI_INCREASE_DECK;
	public GuiSellerPacketsGuiConfig GUI_SELLER_PACKETS;
	public GuiSellerItemsGuiConfig GUI_SELLER_ITEMS;
	public GuiTrashGuiConfig GUI_TRASH;

	public GuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}