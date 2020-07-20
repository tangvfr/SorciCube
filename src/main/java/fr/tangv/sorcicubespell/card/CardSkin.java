package fr.tangv.sorcicubespell.card;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.bson.Document;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CardSkin {

	private int id;
	private String url;
	private String texture;
	private String signature;
	private boolean lastVersion;
	
	public CardSkin(int id, String url, String texture, String signature) {
		this.id = id;
		this.url = url;
		this.texture = texture;
		this.signature = signature;
		this.lastVersion = (id == -1);
	}
	
	public CardSkin(String url) {
		this(-1, url, new String(Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes())), "");
	}
	
	public int getId() {
		return id;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public boolean isLastVersion() {
		return lastVersion;
	}
	
	public static CardSkin createCardSkin(int id) throws Exception {
		String json = IOUtils.toString(new URL("https://api.mineskin.org/get/id/"+id));
		JsonObject texture = new JsonParser().parse(json).getAsJsonObject()
			.get("data").getAsJsonObject()
			.get("texture").getAsJsonObject();
		String value = texture.get("value").getAsString();
		String signature = texture.get("signature").getAsString();
		String url = texture.get("url").getAsString();
		return new CardSkin(id, url, value, signature);
	}
	
	public Image toImageTexture() throws Exception {
		BufferedImage img = ImageIO.read(new URL(url));
		return img.getScaledInstance(img.getWidth()*4, img.getHeight()*4, Image.SCALE_DEFAULT);
	}
	
	public BufferedImage toImageHead() throws Exception {
		return ImageIO.read(new URL("https://api.mineskin.org/render/"+id+"/head"));
	}
	
	public BufferedImage toImageSkin() throws Exception {
		return ImageIO.read(new URL("https://api.mineskin.org/render/"+id+"/skin"));
	}
	
	public Document toDocument() {
		Document document = new Document()
				.append("id", id)
				.append("url", url)
				.append("texture", texture)
				.append("signature", signature);
		return document;
	}
	
	public static CardSkin toCartSkin(Document document) {
		int id = document.getInteger("id");
		String url = document.getString("url");
		String texture = document.getString("texture");
		String signature = document.getString("signature");
		return new CardSkin(id, url, texture, signature);
	}
	
	@Override
	public String toString() {
		if (lastVersion)
			return url;
		else
			return Integer.toString(id);
	}
	
}
