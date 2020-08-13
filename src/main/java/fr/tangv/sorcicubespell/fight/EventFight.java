package fr.tangv.sorcicubespell.fight;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import fr.tangv.sorcicubespell.card.CardEntity;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.fight.PlayerFight.ResultFightHead;
import fr.tangv.sorcicubespell.manager.ManagerFight;
import fr.tangv.sorcicubespell.util.ItemHead;
import fr.tangv.sorcicubespell.util.RenderException;
import io.netty.util.internal.ConcurrentSet;

public class EventFight implements Listener {

	//static
	
	private static final ConcurrentSet<Material> materialTransparent;
	private static final int distanceGetBlock = 100;
	
	static {
		materialTransparent = new ConcurrentSet<Material>();
		materialTransparent.add(Material.AIR);
		materialTransparent.add(Material.LAVA);
		materialTransparent.add(Material.WATER);
		materialTransparent.add(Material.PORTAL);
		materialTransparent.add(Material.ENDER_PORTAL);
		materialTransparent.add(Material.STATIONARY_LAVA);
		materialTransparent.add(Material.STATIONARY_WATER);
		materialTransparent.add(Material.CROPS);
		materialTransparent.add(Material.VINE);
		materialTransparent.add(Material.SUGAR_CANE_BLOCK);
		materialTransparent.add(Material.SAPLING);
		materialTransparent.add(Material.PUMPKIN_STEM);
		materialTransparent.add(Material.POTATO);
		materialTransparent.add(Material.MELON_STEM);
		materialTransparent.add(Material.WATER_LILY);
		materialTransparent.add(Material.DOUBLE_PLANT);
		materialTransparent.add(Material.LONG_GRASS);
		materialTransparent.add(Material.RED_ROSE);
		materialTransparent.add(Material.YELLOW_FLOWER);
		materialTransparent.add(Material.RED_MUSHROOM);
		materialTransparent.add(Material.BROWN_MUSHROOM);
		materialTransparent.add(Material.DEAD_BUSH);
		materialTransparent.add(Material.COCOA);
		materialTransparent.add(Material.CARROT);
		materialTransparent.add(Material.BEETROOT_BLOCK);
		materialTransparent.add(Material.CHORUS_FLOWER);
		materialTransparent.add(Material.CHORUS_PLANT);
		materialTransparent.add(Material.TRAP_DOOR);
		materialTransparent.add(Material.IRON_TRAPDOOR);
		materialTransparent.add(Material.STONE_PLATE);
		materialTransparent.add(Material.GOLD_PLATE);
		materialTransparent.add(Material.WOOD_PLATE);
		materialTransparent.add(Material.IRON_PLATE);
		materialTransparent.add(Material.STONE_BUTTON);
		materialTransparent.add(Material.WOOD_BUTTON);
		materialTransparent.add(Material.TORCH);
		materialTransparent.add(Material.REDSTONE_TORCH_OFF);
		materialTransparent.add(Material.REDSTONE_TORCH_ON);
		materialTransparent.add(Material.LEVER);
		materialTransparent.add(Material.LADDER);
		materialTransparent.add(Material.RAILS);
		materialTransparent.add(Material.ACTIVATOR_RAIL);
		materialTransparent.add(Material.DETECTOR_RAIL);
		materialTransparent.add(Material.POWERED_RAIL);
		materialTransparent.add(Material.STANDING_BANNER);
		materialTransparent.add(Material.WALL_BANNER);
		materialTransparent.add(Material.END_ROD);
		materialTransparent.add(Material.REDSTONE_WIRE);
		materialTransparent.add(Material.REDSTONE_COMPARATOR_OFF);
		materialTransparent.add(Material.REDSTONE_COMPARATOR_ON);
		materialTransparent.add(Material.DIODE_BLOCK_OFF);
		materialTransparent.add(Material.DIODE_BLOCK_ON);
		materialTransparent.add(Material.SIGN_POST);
		materialTransparent.add(Material.WALL_SIGN);
		materialTransparent.add(Material.SNOW);
		materialTransparent.add(Material.TRIPWIRE);
		materialTransparent.add(Material.TRIPWIRE_HOOK);
		materialTransparent.add(Material.WEB);
	}
	
	private final static double TOLERANCE_MOVE = 3.2;
	
	//dynamic
	
	private ManagerFight manager;

	public EventFight(ManagerFight manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
		for (PreFight preFight : manager.getPreFights().values())
			if (preFight.getPlayerUUID1().equals(e.getPlayer().getUniqueId())) {
				e.setCancelled(true);
				return;
			}
	}
	
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer())) {
			PlayerFight player = manager.getPlayerFights().get(e.getPlayer());
			if (player.hasInHandStick()) {
				Block block = player.getPlayer().getTargetBlock(materialTransparent, distanceGetBlock);
				if (block != null) {
					FightCible cible = player.getFight().getCibleForBlock(block, player.isFisrt());
					if (cible != null && !cible.isHero()) {
						FightEntity entity = (FightEntity) player.getForCible(cible);
						if (!entity.isDead()) {
							player.openInvViewEntity(CardRender.cardToItem(
									entity.getCard().getCard(), manager.getSorci(),
									cible.isAlly() ? 2 : 1, false
							));
						}
					}
				}
			} else if (!player.canPlay() 
					|| e.getAction() == Action.LEFT_CLICK_AIR 
					|| e.getAction() == Action.LEFT_CLICK_BLOCK 
					|| e.getAction() == Action.PHYSICAL) {
				player.openInvHistoric();
				if (player.canPlay()) {
					player.setCardSelect(-1);
					player.setEntityAttack(null);
					player.showEntityAttackPossible();
				}
			} else {
				Block block = player.getPlayer().getTargetBlock(materialTransparent, distanceGetBlock);
				if (block != null) {
					FightCible cible = player.getFight().getCibleForBlock(block, player.isFisrt());
					if (cible != null) {
						if (player.getCardSelect() == -1) {
							if (player.hasEntityAttack()) {
								Card card = player.getEntityAttack().getCard().getCard();
								FightHead head = player.getForCible(cible);
								if (!head.isDead() && player.testHeadValidForAttack(card, head)) {
									FightEntity entity = player.getEntityAttack();
									entity.setAttackPossible(false);
									player.setEntityAttack(null);
									player.showEntityAttackPossible();
									//start action fight entity
									int cAttack = head.damage(entity.getAttack());
									if (cAttack != 0)
										entity.damage(cAttack);
									//end action fight entity
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
											try {
												player.removeMana(card.getMana());
												entity.setCard(new CardEntity(card));
												player.setCardHand(player.getCardSelect(), null);
												player.setCardSelect(-1);
												player.addHistoric(card, player);
												player.getEnemie().addHistoric(card, player);
												player.initHotBar();
											} catch (Exception e1) {
												Bukkit.getLogger().warning(RenderException.renderException(e1));
											}
										}
									}
								} else {
									if (!card.getCible().hasChoose()) {
										player.removeMana(card.getMana());
										player.executeFightHeadIsGoodCible(card, new ResultFightHead() {
											@Override
											public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement) {
												FightSpell.startActionSpell(player, card.getFeatures(), fightHeads);
												return true;
											}
										});
										player.setCardHand(player.getCardSelect(), null);
										player.setCardSelect(-1);
										player.addHistoric(card, player);
										player.getEnemie().addHistoric(card, player);
										player.initHotBar();
									} else {
										FightHead head = player.getForCible(cible);
										if (player.testHeadValidForAttack(card, head)) {
											player.removeMana(card.getMana());
											FightSpell.startActionSpell(player, card.getFeatures(), head);
											player.setCardHand(player.getCardSelect(), null);
											player.setCardSelect(-1);
											player.addHistoric(card, player);
											player.getEnemie().addHistoric(card, player);
											player.initHotBar();
										}
									}
								}
							} else {
								player.getPlayer().sendMessage(manager.getSorci().getMessage().getString("message_mana_insufficient"));
							}
							player.setEntityAttack(null);
							player.showEntityAttackPossible();
						}
						//end cards action
					}
				}
			}
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		} else if (!e.getPlayer().hasPermission(manager.getSorci().getParameter().getString("perm_admin"))) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		}
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		if (manager.getPlayerFights().containsKey(e.getWhoClicked())) {
			e.setCancelled(true);
			PlayerFight player = manager.getPlayerFights().get(e.getWhoClicked());
			if (e.getInventory().hashCode() == player.getInvHistoric().hashCode() ||
					e.getInventory().hashCode() == player.getInvViewEntity().hashCode()) {
				if (player.canPlay()) {
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
								
							default:
								break;
						}
				}
			} else {
				player.openInvHistoric();
			}
		}
	}
	
	@EventHandler
	public void onOpenInv(InventoryOpenEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer()) ? 
			manager.getPlayerFights().get(e.getPlayer()).getInvHistoric().hashCode() !=  e.getInventory().hashCode()
			: !e.getPlayer().hasPermission(manager.getSorci().getParameter().getString("perm_admin"))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer())) {
			PlayerFight player = manager.getPlayerFights().get(e.getPlayer());
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
		for (Player other : Bukkit.getOnlinePlayers()) {
			other.hidePlayer(player);
			player.hidePlayer(other);
		}
		manager.playerJoin(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		manager.playerQuit(e.getPlayer());
	}
	
}
