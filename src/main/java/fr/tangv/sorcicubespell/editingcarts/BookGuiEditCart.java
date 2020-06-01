package fr.tangv.sorcicubespell.editingcarts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftMetaBook;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartEntity;
import fr.tangv.sorcicubespell.carts.CartSort;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

public class BookGuiEditCart extends BookGui {

	public BookGuiEditCart(EditCartsGui ec) {
		super(ec, BookGuis.MAIN);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected BookMeta getBook(PlayerEditCart player, Cart cart, BookMeta meta) {
		List<IChatBaseComponent> book = new ArrayList<IChatBaseComponent>();
		//page1
		TextComponent page = new TextComponent(this.config.getString("title"));
		//name
		addTextConfig(page, "name", cart.getName());
		//material
		addTextConfig(page, "material", cart.getMaterial().getItemTypeId()+":"+cart.getMaterial().getData());
		//type
		addTextConfig(page, "type", this.ec.sorci.getEnumTool().typeToString(cart.getType()));
		//count mana
		addTextConfig(page, "count_mana", ""+cart.getCountMana());
		//damage
		addTextConfig(page, "damage", ""+cart.getDamage());
		//rarity
		addTextConfig(page, "rarity", this.ec.sorci.getEnumTool().rarityToString(cart.getRarity()));
		//faction
		addTextConfig(page, "faction", this.ec.sorci.getEnumTool().factionToString(cart.getFaction()));
		if (cart instanceof CartEntity) {
			CartEntity entity = (CartEntity) cart;
			//health
			addTextConfig(page, "health", ""+entity.getHealth());
		} else {
			CartSort sort = (CartSort) cart;
			//heal
			addTextConfig(page, "heal", ""+sort.getHeal());
			//give_mana
			addTextConfig(page, "give_mana", ""+sort.getGiveMana());
			//cible
			addTextConfig(page, "cible",  this.ec.sorci.getEnumTool().cibleToString(sort.getCible()));
		}
		book.add(this.toIChatBaseComposent(page));
		//page2
		String description = "";
		for (String string : cart.getDescription())
			description += string+"\n";
		TextComponent page2 = new TextComponent(this.config.getString("title"));
		//description
		addTextConfig(page2, "description", description);
		book.add(this.toIChatBaseComposent(page2));
		//page3
		TextComponent page3 = new TextComponent(this.config.getString("title"));
		//id
		page3.addExtra(new TextComponent(this.config.getString("id",cart.getId()).replace("{value}", cart.getId())));
		//remove
		addTextConfig(page3, "remove", "");
		addTextConfig(page3, "back", "");
		book.add(this.toIChatBaseComposent(page3));
		//set book
		((CraftMetaBook) meta).pages = book;
		return meta;
	}

	protected void addTextConfig(TextComponent page, String path, String value) {
		TextComponent comp = new TextComponent(this.config.getString(path).replace("{value}", value));
		comp.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/editcarts "+this.name+" "+path));
		comp.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
				new TextComponent[] {new TextComponent(this.config.getString("hover_"+path))}));
		page.addExtra(comp);
	}
	
	@Override
	protected boolean onCommand(PlayerEditCart player, Cart cart, String[] args) {
		if (args.length == 1) {
			this.open(player, cart);
		} else if (args.length == 2) {
			String action = args[1];
			switch (action) {
				//for all cart
				case "description":
					this.ec.setBookDesc(player.getPlayer(), cart);
					break;
			
				case "back":
					this.ec.guiEditList.open(player.getPlayer());
					break;
					
				case "name":
					new AnvilEditName(player, cart, this).open();
					break;
					
				case "material":
					new AnvilEditMaterial(player, cart, this).open();
					break;
					
				case "cible":
					this.ec.guiBooks.get(BookGuis.CIBLE).open(player, cart);
					break;
					
				case "faction":
					this.ec.guiBooks.get(BookGuis.FACTION).open(player, cart);
					break;
					
				case "rarity":
					this.ec.guiBooks.get(BookGuis.RARITY).open(player, cart);
					break;
					
				case "type":
					this.ec.guiBooks.get(BookGuis.TYPE).open(player, cart);
					break;
				
				case "count_mana":
					new AnvilEditNumberCart.AnvilEditCountMana(player, cart, this).open();
					break;
				
				case "damage":
					new AnvilEditNumberCart.AnvilEditDamage(player, cart, this).open();
					break;
					
				//for entity cart
				case "health":
					new AnvilEditNumberCart.AnvilEditHealth(player, cart, this).open();
					break;
				
				//for sort cart
				case "heal":
					new AnvilEditNumberCart.AnvilEditHeal(player, cart, this).open();
					break;
				
				case "give_mana":
					new AnvilEditNumberCart.AnvilEditGiveMana(player, cart, this).open();
					break;
					
				//for all cart
				//	"remove" add confirmation for remove and change type
				default:
					player.getPlayer().sendMessage("action: "+action);
					player.getPlayer().sendMessage("undefine !");
					break;
			}
		} else {
			return false;
		}
		return true;
	}
	
}
