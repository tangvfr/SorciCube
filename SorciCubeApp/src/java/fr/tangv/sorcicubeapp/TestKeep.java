package fr.tangv.sorcicubeapp;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class TestKeep {

	public static void main(String[] args) {
		ArrayList<Item> list = new ArrayList<Item>(); 
		int flags = Pattern.UNICODE_CASE+Pattern.MULTILINE;
		Pattern rmB = Pattern.compile("<[^>]*>" ,flags);
		String f = "none";
		String fm = "none";
		try {
			for (int page = 1; page <= 8; page++) {
				System.out.println("Page "+page);
				URL url = new URL("https://minecraftitemids.com/"+page);
				StringBuilder sb = new StringBuilder();
				InputStreamReader in = new InputStreamReader(url.openStream());
				char[] c = new char[1024];
				int len;
				while ((len = in.read(c)) != -1)
					sb.append(c, 0, len);
				in.close();
				Pattern patM = Pattern.compile("<tbody>(.*?)</tbody>", flags);
				Matcher matM = patM.matcher(sb);
				if (matM.find() && matM.groupCount() > 0) {
					Pattern pat = Pattern.compile("<tr class=\"tsr\">(.*?)</tr>", flags);
					Matcher mat = pat.matcher(matM.group(1));
					while(mat.find()) {
						Pattern pi = Pattern.compile("<td[^>]*>(.*?)</td>", flags);
						Matcher mi = pi.matcher(mat.group(1));
						mi.find();
						mi.find();
						String name = rmB.matcher(mi.group(1)).replaceAll("");
						mi.find();
						String minecraftID = mi.group(1);
						mi.find();
						String legacyID = mi.group(1);
						mi.find();
						String numID = mi.group(1);
						if (numID.isEmpty())
							continue;
						if (!Pattern.compile(":\\d*\\z").matcher(numID).find())
							numID += ":0";
						//System.out.println(name+" "+minecraftID+" "+legacyID+" "+numID);
						f = name;
						fm = minecraftID;
						String linkID = minecraftID.replace("minecraft:", "");
						Image img;
						try {
							img = ImageIO.read(new URL("https://minecraftitemids.com/item/32/"+linkID+".png"));
						} catch (IOException e) {
							img = ImageIO.read(new URL("https://minecraftitemids.com/item/32/"+numID.replace(":", "-")+".png"));
						}
						list.add(new Item(name, minecraftID, legacyID, numID, img));
					}
				}
			}
		//<tbody> 
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Error: "+f);
		}
	}
	
	public static class Item {
		
		public final String name;
		public final String minecraftID;
		public final String legacyID;
		public final String numID;
		public final Image img;
		
		public Item(String name,
					String minecraftID,
					String legacyID,
					String numID,
					Image img) {
			this.name = name;
			this.minecraftID = minecraftID;
			this.legacyID = legacyID;
			this.numID = numID;
			this.img = img;
		}
		
	}

}
