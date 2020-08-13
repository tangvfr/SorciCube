package fr.tangv.sorcicubespell.card;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.bson.Document;

public class CardSkin {

	private int id;
	private String url;
	private String texture;
	private String signature;
	
	public CardSkin(int id, String url, String texture, String signature) {
		this.id = id;
		this.url = url;
		this.texture = texture;
		this.signature = signature;
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
	
	public static CardSkin createCardSkin(int id) throws Exception {
		String json = "";
		Scanner sc = new Scanner(new URL("https://api.mineskin.org/get/id/"+id).openStream());
		while (sc.hasNextLine())
			json += "\r\n"+sc.nextLine();
		sc.close();
		Document texture = Document.parse(json)
			.get("data", Document.class)
			.get("texture", Document.class);
		String value = texture.getString("value");
		String signature = texture.getString("signature");
		String url = texture.getString("url");
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
		return Integer.toString(id);
	}
	
}
