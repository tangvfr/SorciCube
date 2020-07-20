package fr.tangv.sorcicubespell.packet;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.manager.ManagerPacketCards;
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.RenderException;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class EventPacket implements Listener, Runnable {

	private ManagerPacketCards manager;
	private ConcurrentHashMap<Player, PlayerPacket> packetsPlayers;
	private ConfigurationSection config;
	private ItemStack itemQuestion;
	private ItemStack itemBack;
	
	public EventPacket(ManagerPacketCards manager) {
		this.manager = manager;
		this.packetsPlayers = new ConcurrentHashMap<Player, PlayerPacket>();
		this.config = manager.getSorci().gertGuiConfig().getConfigurationSection("gui_open_packet");
		this.itemQuestion = ItemBuild.buildSkull(SkullUrl.QUESTION, 1, config.getString("no_view"), null, false);
		this.itemBack = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("back"), null, false);
	}
	
	private static final byte[] dataPane = {/*0, */1, 2, 3, 4, 5, 6, /*7, 8, */9, 10, 11, 12, 13, 14/*, 15*/};
	
	private ItemStack genratedItemPane(Random random) {
		return ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, dataPane[random.nextInt(dataPane.length)], " ", null, false);
	}
	
	private ItemStack[] generatedInvPane() {
		Random random = new Random();
		ItemStack[] inv = new ItemStack[27];
		for (int i = 0; i < 9; i++) {
			inv[i] = genratedItemPane(random);
			inv[i+18] = genratedItemPane(random);
		}
		inv[9] = genratedItemPane(random);;
		inv[17] = genratedItemPane(random);;
		return inv;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getHand() == EquipmentSlot.HAND) {
			Player player = e.getPlayer();
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item != null && manager.itemIsPacket(item)) {
				String name = item.getItemMeta().getDisplayName();
				PacketCards packet = manager.getPacketCards(name);
				if (packet == null) {
					player.sendMessage(
						manager.getSorci().getMessage().getString("message_packet_no_found")
						.replace("{name}", name)
					);
				} else {
					try {
						if (packet.getSize() < 1 || packet.getSize() > 7)
							throw new Exception("The max and min number cards in packet is 1 to 7 included !");
						Inventory inv = Bukkit.createInventory(null, 27, packet.getName());
						inv.setContents(generatedInvPane());
						int start = ((7-packet.getSize())/2)+10;
						Card[] cardTake = manager.packetTakeCard(packet);
						ItemStack[] itemCards = new ItemStack[cardTake.length];
						boolean[] newCards = new boolean[cardTake.length];
						PlayerFeature feature = manager.getSorci().getManagerPlayers().getPlayerFeature(player);
						if (feature != null) {
							for (int i = 0; i < cardTake.length; i++) {
								if (cardTake[i] == null) {
									newCards[i] = false;
									itemCards[i] = null;
								} else {
									String uuid = cardTake[i].getUUID().toString();
									if (feature.getCardsUnlocks().contains(uuid)) {
										newCards[i] = false;
									} else {
										feature.getCardsUnlocks().add(uuid);
										newCards[i] = true;
									}
									itemCards[i] = CardRender.cardToItem(cardTake[i], manager.getSorci());
								}
								inv.setItem(start+i, itemQuestion);
							}
							manager.getSorci().getManagerPlayers().update(feature);
							if (item.getAmount() > 1)
								item.setAmount(item.getAmount()-1);
							else
								player.getInventory().setItemInMainHand(null);
							packetsPlayers.put(player, new PlayerPacket(player, inv, itemCards, newCards ,start));
							player.openInventory(inv);
						}
					} catch (Exception e1) {
						Bukkit.getLogger().warning(RenderException.renderException(e1));
						player.sendMessage(manager.getSorci().getMessage().getString("message_packet_error_take"));
					}
				}
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCloseInv(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			Player player = (Player) e.getPlayer();
			if (packetsPlayers.containsKey(player)) {
				PlayerPacket playerP = packetsPlayers.get(player);
				if (playerP.getInventory().hashCode() == e.getInventory().hashCode())
					packetsPlayers.remove(player);
			}
		}
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player player = (Player) e.getWhoClicked();
			if (packetsPlayers.containsKey(player)) {
				PlayerPacket playerP = packetsPlayers.get(player);
				if (playerP.getInventory().hashCode() == e.getInventory().hashCode()) {
					if (e.getRawSlot() == 22 && !playerP.needActual())
						player.closeInventory();
					e.setCancelled(true);
				}
			}
		}
	}

	@Override
	public void run() {
		ItemStack[] border = generatedInvPane();
		for (int i = 0; i < border.length; i++)
			if (border[i] != null)
				for (PlayerPacket player : packetsPlayers.values())
					if (player.needActual()) {
						player.getInventory().setItem(i, border[i]);
					}
		for (PlayerPacket player : packetsPlayers.values()) {
			if (player.needActual() && player.getCooldown().update()) {
				int index = player.getViewCard();
				if (index >= player.getSizeItemCards()) {
					player.stopActual();
					player.getCooldown().stop();
					player.getInventory().setItem(22, itemBack);
				} else {
					boolean newCard = player.isNewCard(index);
					player.getInventory().setItem(player.getStart()+index, player.getItemCard(index));
					player.setViewCard(index+1);
					if (newCard)
						player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.75F);
					else
						player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_GENERIC_BURN, 1.0F, 2F);
				}
			}
		}
	}
	
}
