package fr.tangv.sorcicubeapp.dialog;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.bson.Document;

public class Item {
	
	public final String name;
	public final String minecraftID;
	public final String legacyID;
	public final String numID;
	public final BufferedImage img;
	
	public Item(String name,
				String minecraftID,
				String legacyID,
				String numID,
				BufferedImage img) {
		this.name = name;
		this.minecraftID = minecraftID;
		this.legacyID = legacyID;
		this.numID = numID;
		this.img = img;
	}
	
	public Document toDocument() throws IOException {
		Document doc = new Document();
		doc.append("name", name);
		doc.append("minecraftID", minecraftID);
		doc.append("legacyID", legacyID);
		doc.append("numID", numID);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "png", out);
		out.close();
		doc.append("img", new String(Base64.getEncoder().encode(out.toByteArray())));
		return doc;
	}
	
	public static Item toItem(Document doc) throws IOException {
		return new Item(
				doc.getString("name"),
				doc.getString("minecraftID"),
				doc.getString("legacyID"),
				doc.getString("numID"),
				ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(doc.getString("img")))));
	}
	
}
