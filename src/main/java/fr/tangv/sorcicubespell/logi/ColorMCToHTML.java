package fr.tangv.sorcicubespell.logi;

public class ColorMCToHTML {

	public static String replaceColor(String text) {
		//colours
		//#000 #00A #0A0 #0AA #A00 #A0A #FA0 #AAA #555 #55F #5F5 #5FF #F55 #F5F #FF5 #FFF
		text = text.replace("§0","</span>§r<span color='#000000'>");
		text = text.replace("§1","</span>§r<span color='#0000AA'>");
		text = text.replace("§2","</span>§r<span color='#00AA00'>");
		text = text.replace("§3","</span>§r<span color='#00AAAA'>");
		text = text.replace("§4","</span>§r<span color='#AA0000'>");
		text = text.replace("§5","</span>§r<span color='#AA00AA'>");
		text = text.replace("§6","</span>§r<span color='#FFAA00'>");
		text = text.replace("§7","</span>§r<span color='#AAAAAA'>");
		text = text.replace("§8","</span>§r<span color='#555555'>");
		text = text.replace("§9","</span>§r<span color='#5555FF'>");
		text = text.replace("§a","</span>§r<span color='#55FF55'>");
		text = text.replace("§b","</span>§r<span color='#55FFFF'>");
		text = text.replace("§c","</span>§r<span color='#FF5555'>");
		text = text.replace("§d","</span>§r<span color='#FF55FF'>");
		text = text.replace("§e","</span>§r<span color='#FFFF55'>");
		text = text.replace("§f","</span>§r<span color='#FFFFFF'>");
		//bold
		text = text.replace("§l","<span style='font-weight:900;'>");
		//italic
		text = text.replace("§o","<span style='font-style:italic;'>");
		//strikethrough
		text = text.replace("§m","<span style='text-decoration:line-through'>");
		//underlined
		text = text.replace("§n","<span style='text-decoration:underline'>");
		//obfuscated
		text = text.replace("§k","<span style='background: BLACK;'>");
		//reset
		text = text.replace("§r", "</span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span>");
		//return
		return text;
	}
	
}
