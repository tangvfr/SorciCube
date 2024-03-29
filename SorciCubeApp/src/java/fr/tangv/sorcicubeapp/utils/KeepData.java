package fr.tangv.sorcicubeapp.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.bson.Document;

import fr.tangv.sorcicubeapp.dialog.Item;

public class KeepData {

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
						if (legacyID.isEmpty())
							legacyID = minecraftID;
						mi.find();
						String numID = mi.group(1);
						if (numID.isEmpty())
							continue;
						if (minecraftID.equals("minecraft:clay"))
							numID = "82:0";
						if (!Pattern.compile(":\\d*\\z").matcher(numID).find())
							numID += ":0";
						int size = 64;
						BufferedImage img;
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
			File file = new File(System.getenv("appdata")+"/KeepData/items.json");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			Document items = new Document();
			for (Item item : list)
				items.append(item.numID, item.toDocument());
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
			out.write(items.toJson());
			out.close();
			System.out.println("Finish !");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getImage(int size, String name) throws MalformedURLException, IOException {
		return ImageIO.read(new URL("https://minecraftitemids.com/item/"+size+"/"+name+".png"));
	}

}
