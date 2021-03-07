package fr.tangv.sorcicubeapp;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
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
						int size = 64;
						Image img;
						try {
							img = getImage(size, minecraftID.replace("minecraft:", ""));
						} catch (IOException e) {
							try {
								img = getImage(size, numID.replace(":", "-"));
							} catch (IOException e2) {
								if (numID.equalsIgnoreCase("373:16") || numID.equalsIgnoreCase("373:32") || numID.equalsIgnoreCase("373:64"))
									img = getImage(size, "373-0");
								else if (numID.equalsIgnoreCase("68:0"))
									img = getImage(size, "sign");
								else if (legacyID.equalsIgnoreCase("minecraft:mob_spawner"))
									img = getImage(size, "spawner");
								else {
									System.out.println(name+" "+minecraftID+" "+legacyID+" "+numID);
									throw e2;
								}
							}
						}
						list.add(new Item(name, minecraftID, legacyID, numID, img));
					}
				}
			}
			System.out.println("End !");
			File folder = new File(System.getenv("appdata")+"/TestKeep/items");
			if (!folder.exists())
				folder.mkdirs();
			for (int i = 0; i < list.size(); i++) {
				Item item = list.get(i);
				System.out.println(((i*100)/(list.size()*100))+"% > "+item.name+" with "+item.numID);
				File file = new File(folder.getPath()+"/"+item.numID.replace(":", "_"));
				if (!file.exists())
					file.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(item);
				out.close();
			}
			System.out.println("Finish !");
		//<tbody> 
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Image getImage(int size, String name) throws MalformedURLException, IOException {
		return ImageIO.read(new URL("https://minecraftitemids.com/item/"+size+"/"+name+".png"));
	}
	
	public static class Item implements Serializable {
		
		private static final long serialVersionUID = 7043385399714609173L;
		
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
