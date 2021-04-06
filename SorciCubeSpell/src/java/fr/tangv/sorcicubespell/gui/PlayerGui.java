package fr.tangv.sorcicubespell.gui;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardComparator;
import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.Config;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerGui {

	private final Player player;
	private Card card;
	private Inventory invOfGui;
	private AbstractGui gui;
	private CardComparator cardComparator;
	private int pageViewGui;
	private int deckEdit;
	private int deckCardEdit;
	private PlayerFeatures playerFeature;
	private AbstractGui previousGui;
	private FightType fightType;
	private Player inviteDuel;
	private boolean viewHideCards;
	private String displayGroup;
	
	public PlayerGui(Player player) {
		this.player = player;
		this.card = null;
		this.gui = null;
		this.inviteDuel = null;
		this.cardComparator = CardComparator.BY_ID;
		this.setPageViewGui(0);
		this.invOfGui = null;
		this.setDeckEdit(1);
		this.setDeckCardEdit(0);
		this.playerFeature = null;
		this.setPreviousGui(null);
		this.setViewHideCards(false);
		this.displayGroup = "";
	}
	
	public UUID getUUID() {
		return player.getUniqueId();
	}
	
	public String getName() {
		return player.getName();
	}
	
	public String getDisplayGroup() {
		return displayGroup;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Card getCard() {
		return card;
	}
	
	public void setCart(Card card) {
		this.card = card;
	}
	
	public AbstractGui getGui() {
		return gui;
	}
	
	public boolean isViewHideCards() {
		return viewHideCards;
	}

	public void setViewHideCards(boolean viewHideCards) {
		this.viewHideCards = viewHideCards;
	}

	public void setGui(AbstractGui gui) {
		this.gui = gui;
		this.invOfGui = null;
	}

	public int getPageViewGui() {
		return pageViewGui;
	}

	public void setPageViewGui(int pageViewGui) {
		this.pageViewGui = pageViewGui;
	}

	public CardComparator getCardComparator() {
		return cardComparator;
	}

	public void setCardComparator(CardComparator cardComparator) {
		this.cardComparator = cardComparator;
	}

	public Inventory getInvOfGui() {
		return invOfGui;
	}
	
	public void setInvOfGui(Inventory invOfGui) {
		this.invOfGui = invOfGui;
	}

	public int getDeckEdit() {
		return deckEdit;
	}

	public void setDeckEdit(int deckEdit) {
		this.deckEdit = deckEdit;
	}

	public int getDeckCardEdit() {
		return deckCardEdit;
	}

	public void setDeckCardEdit(int deckCardEdit) {
		this.deckCardEdit = deckCardEdit;
	}

	public PlayerFeatures getPlayerFeatures() {
		return playerFeature;
	}
	
	public void setPlayerFeatures(PlayerFeatures playerFeature, SorciCubeSpell sorci) {
		player.closeInventory();
		this.playerFeature = playerFeature;
		this.displayGroup = sorci.applyPermission(player, playerFeature.isAdmin(), playerFeature.getGroup());
	}
	
	public void uploadPlayerFeatures(HandlerPlayers handler) {
		if(playerFeature != null)
			try {
				handler.update(playerFeature);
			} catch (IOException | ReponseRequestException | RequestException e) {
				e.printStackTrace();
			}
		else
			new Exception("PlayerFeature is null !").printStackTrace();
	}
	
	public void updateDisplay(Config lc, String messageActionBar) {
		int level = playerFeature.getLevel();
		player.setLevel(level);
		int exp = 0;
		int expMax = 0;
		if (!playerFeature.isLevel((byte) lc.getInt("level_max"))) {
			exp = playerFeature.getExperience();
			expMax = lc.getInt("level_experience."+(playerFeature.getLevel()+1)+".experience");
			player.setExp(playerFeature.getExperience()/(float) expMax);
		} else {
			player.setExp(1.0F);
		}
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
				messageActionBar
					.replace("{group}", displayGroup)
					.replace("{level}", Integer.toString(level))
					.replace("{exp}", Integer.toString(exp))
					.replace("{exp_max}", Integer.toString(expMax))
					.replace("{money}", Integer.toString(playerFeature.getMoney()))
		));
	}

	public AbstractGui getPreviousGui() {
		return previousGui;
	}

	public void setPreviousGui(AbstractGui previousGui) {
		this.previousGui = previousGui;
	}

	public FightType getFightType() {
		return fightType;
	}

	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}

	public Player getInviteDuel() {
		return inviteDuel;
	}

	public void setInviteDuel(Player inviteDuel) {
		this.inviteDuel = inviteDuel;
	}
	
}
