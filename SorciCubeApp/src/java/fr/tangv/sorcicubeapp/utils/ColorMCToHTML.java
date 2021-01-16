package fr.tangv.sorcicubeapp.utils;

public class ColorMCToHTML {

	public static String replaceColor(String text) {
		//colors
		//#000 #00A #0A0 #0AA #A00 #A0A #FA0 #AAA #555 #55F #5F5 #5FF #F55 #F5F #FF5 #FFF
		text = text.replace("§0","</span><span color='#000000'>");
		text = text.replace("§1","</span><span color='#0000AA'>");
		text = text.replace("§2","</span><span color='#00AA00'>");
		text = text.replace("§3","</span><span color='#00AAAA'>");
		text = text.replace("§4","</span><span color='#AA0000'>");
		text = text.replace("§5","</span><span color='#AA00AA'>");
		text = text.replace("§6","</span><span color='#FFAA00'>");
		text = text.replace("§7","</span><span color='#AAAAAA'>");
		text = text.replace("§8","</span><span color='#555555'>");
		text = text.replace("§9","</span><span color='#5555FF'>");
		text = text.replace("§a","</span><span color='#55FF55'>");
		text = text.replace("§b","</span><span color='#55FFFF'>");
		text = text.replace("§c","</span><span color='#FF5555'>");
		text = text.replace("§d","</span><span color='#FF55FF'>");
		//text = text.replace("§e","</span><span color='#FFFF55'>");
		text = text.replace("§e","</span><span color='#E4E422'>");
		text = text.replace("§f","</span><span color='#FFFFFF'>");
		//return
		return "<span color='#000000'>"+text+"</span>";
	}
	
}
