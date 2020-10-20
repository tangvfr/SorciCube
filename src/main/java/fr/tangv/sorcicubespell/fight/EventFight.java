package fr.tangv.sorcicubespell.fight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.fight.PlayerFight.ResultFightHead;
import fr.tangv.sorcicubespell.manager.ManagerFight;
import fr.tangv.sorcicubespell.util.ItemHead;
import fr.tangv.sorcicubespell.util.RenderException;

public class EventFight implements Listener {
	
	private final static double TOLERANCE_MOVE = 3.5;
	
	private final ManagerFight manager;
	private final String formatChat;
	private final String spectatorChat;
	private final String playerChat;
	
	public EventFight(ManagerFight manager) {
		this.manager = manager;
		this.formatChat = manager.getSorci().getParameter().getString("chat_format_fight");
		this.spectatorChat = manager.getSorci().getParameter().getString("spectator_fight");
		this.playerChat = manager.getSorci().getParameter().getString("player_fight");
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		if (manager.isSpectator(e.getPlayer().getUniqueId())) {
			FightSpectator spectator = manager.getSpectator(e.getPlayer().getUniqueId());
			String message;
			if (spectator.isFightPlayer()) {
				message = formatChat
						.replace("{spectator}", playerChat)
						.replace("{displayname}", e.getPlayer().getDisplayName())
						.replace("{message}", e.getMessage());
				spectator.getFight().sendMessage(message);
			} else {
				message = formatChat
					.replace("{spectator}", spectatorChat)
					.replace("{displayname}", e.getPlayer().getDisplayName())
					.replace("{message}", e.getMessage());
				spectator.getFight().sendMessageSpectator(message);
			}
			Bukkit.getConsoleSender().sendMessage(message);
		}
	}
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		if (manager.isSpectator(uuid) || manager.inPreFight(uuid))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	private void useCard(PlayerFight player, Card card, Runnable run) {
		player.removeMana(card.getMana());
		player.setCardHand(player.getCardSelect(), null);
		player.setCardSelect(-1);
		player.getFight().addHistoric(card, player.isFisrt());
		player.getFight().sendMessage(
				player.getFight().getSorci().getMessage().getString(
						(card.getType() == CardType.ENTITY) ? "message_player_invoke_card" : "message_player_use_card")
				.replace("{player}", player.getNamePlayer())
				.replace("{card}", card.renderName())
		);
		run.run();
		player.initHotBar();
		player.setFirstSelection(null);
		player.setEntityAttack(null);
		player.showEntityAttackPossible();
	}
	
	private void fightWithEntityChoose(Fight fight, FightEntity entity, FightHead head) {
		//start action fight entity
		String damagerOwner = entity.getOwner().getNamePlayer();
		String damagedOwner = head.getOwner().getNamePlayer();
		String damager = entity.getNameInChat();
		String damaged = head.getNameInChat();
		int attack = entity.getAttack();
		//message in chat
		fight.sendMessage(
				fight.getSorci().getMessage().getString("message_attack_entity")
				.replace("{damager_owner}", damagerOwner)
				.replace("{damager}", damager)
				.replace("{attack}", Integer.toString(attack))
				.replace("{damaged_owner}", damagedOwner)
				.replace("{damaged}", damaged)
				.replace("{counter_attack}", Integer.toString(head.getCounterAttack()))
		);
		//apply damage
		int cAttack = head.damage(attack);
		if (cAttack > 0)
			entity.damage(cAttack);
		//excuting action
		if (head instanceof FightEntity)
			((FightEntity) head).executingAction();
		if (cAttack > 0)
			entity.executingAction();
		//end action fight entity
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		if (manager.isSpectator(uuid)) {
			e.setCancelled(true);
			FightSpectator spectator = manager.getSpectator(uuid);
			boolean next = true;
			if (e.getAction() == Action.LEFT_CLICK_AIR 
					|| e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (!spectator.inventoryIsAutorized(spectator.getInvOpenTop())) {
					spectator.openInvHistoric();
					if (spectator.isFightPlayer()) {
						PlayerFight player = (PlayerFight) spectator;
						if (player.canPlay()) {
							player.setCardSelect(-1);
							player.setFirstSelection(null);
							player.setEntityAttack(null);
							player.showEntityAttackPossible();
						}
					}
				}
				next = false;
			} else if (spectator.hasStickView()) {
				FightCible cible = spectator.getTargetCible();
				if (cible != null) {
					FightHead head = spectator.getForCible(cible);
					if (!head.isDead()) {
						ItemStack item;
						if (cible.isHero()) {
							item = ((FightHero) head).renderToItem(cible.isAlly());
						} else {
							item = CardRender.cardToItem(
									((FightEntity) head).getCard().getCard(), manager.getSorci(),
									cible.isAlly() ? 2 : 1, false
							);
						}
						spectator.openInvViewEntity(item);
						next = false;
					}
				}
			}
			if (next && spectator.isFightPlayer()) {
				PlayerFight player = (PlayerFight) spectator;
				if (player.canPlay()) {
					FightCible cible = player.getTargetCible();
					if (cible != null) {
						if (player.getCardSelect() == -1) {
							if (player.hasEntityAttack()) {
								Card card = player.getEntityAttack().getCard().getCard();
								FightHead head = player.getForCible(cible);
								if (!card.getCible().hasChoose()) {
									FightEntity entity = player.getEntityAttack();
									entity.setAttackPossible(false);
									player.setEntityAttack(null);
									player.executeFightHeadIsGoodCible(card, new ResultFightHead() {
										@Override
										public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement) {
											for (FightHead head1 : fightHeads)
												EventFight.this.fightWithEntityChoose(player.getFight(), entity, head1);
											return true;
										}
									});
									player.showEntityAttackPossible();
								} else if (!head.isDead() && player.testHeadValidForAttack(card, head)) {
									FightEntity entity = player.getEntityAttack();
									entity.setAttackPossible(false);
									player.setEntityAttack(null);
									this.fightWithEntityChoose(player.getFight(), entity, head);
									player.showEntityAttackPossible();
								}
							} else {
								if (!cible.isHero() && cible.isAlly()) {
									FightEntity entity = (FightEntity) player.getForCible(cible);
									if (!entity.isDead() && entity.attackIsPossible()) {
										Card card = entity.getCard().getCard();
										player.showHeadForAttack(card, ItemHead.SELECTABLE_ENTITY_DAMAGE);
										player.setEntityAttack(entity);
										entity.showHead(ItemHead.SELECTED_ENTITY);
									}
								}
							}
						} else {
							//start card actions
							Card card = player.getCardHand(player.getCardSelect());
							if (player.hasMana(card.getMana())) {
								if (card.getType() == CardType.ENTITY) {
									//entity
									if (!cible.isHero() && cible.isAlly()) {
										FightEntity entity = (FightEntity) player.getForCible(cible);
										if (!entity.isSelectable()) {
											this.useCard(player, card, () -> {
												try {
													entity.setCard(new CardEntity(card));
													entity.executingAction();
												} catch (Exception e1) {
													Bukkit.getLogger().warning(RenderException.renderException(e1));
												}
											});
										}
									}
								} else {
									if (!card.getCible().hasChoose()) {
										this.useCard(player, card, () -> {
											player.executeFightHeadIsGoodCible(card, new ResultFightHead() {
												@Override
												public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement) {
													FightSpell.startActionSpell(player, card.getFeatures(), fightHeads);
													return true;
												}
											});
										});
									} else {
										FightHead head = player.getForCible(cible);
										if (card.getCible() == CardCible.ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE) {
											if (player.hasFirstSelection()) {
												if (player.testHeadValidForAttack(CardCible.ONE_ENTITY_ENEMIE, card.getCibleFaction(), head)) {
													this.useCard(player, card, () -> {
														FightSpell.startActionSpell(player, card.getFeatures(), Arrays.asList(player.getFirstSelection(), head));
													});
												}
											} else if (player.testHeadValidForAttack(CardCible.ONE_ENTITY_ALLY, card.getCibleFaction(), head)) {
												player.setFirstSelection(head);
												player.showHeadForAttack(CardCible.ONE_ENTITY_ENEMIE, card.getCibleFaction(), ItemHead.SELECTABLE_ENTITY_AAE);
												player.getFirstSelection().showHead(ItemHead.SELECTABLE_ENTITY_DAMAGE);
											}
										} else if (player.testHeadValidForAttack(card, head)) {
											this.useCard(player, card, () -> {
												FightSpell.startActionSpell(player, card.getFeatures(), head);
											});
										}
									}
								}
							} else {
								player.sendMessageInsufficientMana();
							}
						}
						//end cards action
					}
				}
			}
			e.getPlayer().updateInventory();
		} else if (!e.getPlayer().hasPermission(manager.getSorci().getParameter().getString("perm_admin"))) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		}
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		UUID uuid = e.getWhoClicked().getUniqueId();
		if (manager.isSpectator(uuid)) {
			e.setCancelled(true);
			FightSpectator spectator = manager.getSpectator(uuid);
			if (spectator.inventoryIsAutorized(e.getInventory())) {
				if (spectator.isFightPlayer()) {
					PlayerFight player = (PlayerFight) spectator;
					if (player.canPlay()) {
						if (e.getInventory().hashCode() == player.getInvSwap().hashCode()) {
							if (e.getRawSlot() >= 0 && e.getRawSlot() < player.getMaxCardHand()) {
								Card card = player.getCardHand(e.getRawSlot());
								if (card != null) {
									player.setAlreadySwap(true);
									player.setCardHand(e.getRawSlot(), null);
									player.pickCard(1);
									player.initHotBar();
									player.openInvHistoric();
									player.playSound(Sound.ENTITY_GENERIC_BURN, 1.0F, 2F);
								}
								return;
							}
						}
						FightSlot slot = FightSlot.valueOfRaw(e.getRawSlot());
						if (slot != null)
							switch (slot) {
								case CARD_1:
									player.setCardSelect(0);
									break;
			
								case CARD_2:
									player.setCardSelect(1);
									break;
									
								case CARD_3:
									player.setCardSelect(2);
									break;
									
								case CARD_4:
									player.setCardSelect(3);
									break;
									
								case CARD_5:
									player.setCardSelect(4);
									break;
									
								case CARD_6:
									player.setCardSelect(5);
									break;
									
								case FINISH_ROUND:
									player.getFight().nextRound();
									break;
									
								case BUY_CARD:
									if (player.hasMana(ValueFight.V.priceCard)) {
										if (player.pickCard(1) > 0) {
											player.removeMana(ValueFight.V.priceCard);
											player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.75F);
											player.initHotBar();
										}
									} else {
										player.sendMessageInsufficientMana();
									}
									break;
									
								case SWAP_CARD:
									if (!player.hasAlreadySwap())
										player.openInvSwap();
									break;
									
								default:
									break;
							}
					}
				}
			} else {
				spectator.openInvHistoric();
			}
		}
	}
	
	@EventHandler
	public void onOpenInv(InventoryOpenEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		if (manager.isSpectator(uuid)) {
			FightSpectator spectator = manager.getSpectator(uuid);
			if (!spectator.inventoryIsAutorized(e.getInventory())) {
				e.setCancelled(true);
			} else {
				if (spectator.isFightPlayer())
					((PlayerFight) spectator).noAFK();
			}
		} else if (!e.getPlayer().hasPermission(manager.getSorci().getParameter().getString("perm_admin"))) {
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		if (manager.isSpectator(uuid)) {
			FightSpectator spectator = manager.getSpectator(uuid);
			if (spectator.isFightPlayer()) {
				PlayerFight player = (PlayerFight) spectator;
				Location loc = player.getLocBase();
				if (loc.getWorld().equals(e.getTo().getWorld())) {
					loc = new Location(loc.getWorld(), loc.getX(), e.getTo().getY(), loc.getZ());
					if (e.getTo().distance(loc) > TOLERANCE_MOVE) {
						Location newLoc = e.getFrom();
						newLoc.setX(e.getFrom().getX());
						newLoc.setZ(e.getFrom().getZ());
						e.setTo(newLoc);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage("");
		player.setGameMode(GameMode.ADVENTURE);
		player.setFoodLevel(20);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setExp(1F);
		player.setLevel(0);
		player.setCollidable(false);
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.setAllowFlight(false);
		player.setFlying(false);
		manager.playerJoin(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		manager.playerQuit(e.getPlayer());
	}
	
}
