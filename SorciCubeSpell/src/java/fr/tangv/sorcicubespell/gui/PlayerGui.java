package fr.tangv.sorcicubespell.gui;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardComparator;
import fr.tangv.sorcicubecore.configs.LevelConfig;
import fr.tangv.sorcicubecore.configs.ParameterConfig;
import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.player.Group;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.player.DataPlayer;
import fr.tangv.sorcicubespell.util.NameTag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerGui {

	private final Player player;
	private Card card;
	private Inventory invOfGui;
	private AbstractGui<?> gui;
	private CardComparator cardComparator;
	private int pageViewGui;
	private int deckEdit;
	private int deckCardEdit;
	private PlayerFeatures playerFeature;
	private AbstractGui<?> previousGui;
	private FightType fightType;
	private Player inviteDuel;
	private boolean viewHideCards;
	private DataPlayer dataPlayer;
	
	public PlayerGui(Player player, ParameterConfig parameter) {
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
		this.dataPlayer = new DataPlayer(player.getName(), parameter);
	}
	
	public UUID getUUID() {
		return player.getUniqueId();
	}
	
	public String getName() {
		return player.getName();
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
	
	public AbstractGui<?> getGui() {
		return gui;
	}
	
	public boolean isViewHideCards() {
		return viewHideCards;
	}

	public void setViewHideCards(boolean viewHideCards) {
		this.viewHideCards = viewHideCards;
	}

	public void setGui(AbstractGui<?> gui) {
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
		Group group = sorci.getManagerPermissions().applyPermission(player, playerFeature.isAdmin(), playerFeature.getGroup());
		dataPlayer = new DataPlayer(playerFeature, group, sorci.config().parameter);
		NameTag.sendNameTag(dataPlayer, Bukkit.getOnlinePlayers());
	}
	
	public void uploadPlayerFeatures(HandlerPlayers handler) {
		if(playerFeature != null)
			try {
				handler.update(playerFeature);
			} catch (IOException | ResponseRequestException | RequestException e) {
				e.printStackTrace();
			}
		else
			new Exception("PlayerFeature is null !").printStackTrace();
	}
	
	public void updateDisplay(String messageActionBar, LevelConfig lc) {
		int level = playerFeature.getLevel();
		player.setLevel(level);
		int exp = 0;
		int expMax = 0;
		if (!playerFeature.isLevel((byte) lc.maxLevel.value)) {
			exp = playerFeature.getExperience();
			expMax = lc.getExperience(level+1);
			player.setExp(playerFeature.getExperience()/(float) expMax);
		} else {
			player.setExp(1.0F);
		}
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
				dataPlayer.replace(messageActionBar)
					.replace("{exp}", Integer.toString(exp))
					.replace("{exp_max}", Integer.toString(expMax))
					.replace("{money}", Integer.toString(playerFeature.getMoney()))
		));
	}

	public AbstractGui<?> getPreviousGui() {
		return previousGui;
	}

	public void setPreviousGui(AbstractGui<?> previousGui) {
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

	public DataPlayer getDataPlayer() {
		return dataPlayer;
	}
	
}
