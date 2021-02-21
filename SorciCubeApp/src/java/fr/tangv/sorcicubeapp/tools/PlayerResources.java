package fr.tangv.sorcicubeapp.tools;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bson.Document;

public class PlayerResources {

	private final UUID uuid;
	private final String name;
	private final BufferedImage skin;
	private final BufferedImage head;
	
	public PlayerResources(UUID uuid) throws ExceptionPlayerResources, IOException {
		this.uuid = uuid;
		if (uuid == null)
			throw new ExceptionPlayerResources("UUID is null");
		URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid.toString().replace("-", ""));
		InputStreamReader input = new InputStreamReader(url.openStream());
		CharArrayWriter w = new CharArrayWriter();
		char[] in = new char[512];
		int len;
		while ((len = input.read(in)) != -1)
			w.write(in, 0, len);
		input.close();
		String json = new String(w.toCharArray());
		if (json.isEmpty())
			throw new ExceptionPlayerResources("No found UUID");
		Document doc = Document.parse(json);
		String error = doc.getString("error");
		if (error != null)
			throw new ExceptionPlayerResources("Error "+error);
		this.name = doc.getString("name");
		List<Document> values = doc.getList("properties", Document.class);
		for (Document value : values)
			if ("textures".equals(value.getString("name"))) {
				Document texture = Document.parse(new String(Base64.getDecoder().decode(value.getString("value"))));
				this.skin = ImageIO.read(new URL(texture.get("textures", Document.class).get("SKIN", Document.class).getString("url")));
				this.head = new BufferedImage(8, 8, BufferedImage.TYPE_4BYTE_ABGR);
				head.getGraphics().drawImage(skin.getSubimage(8, 8, 8, 8), 0, 0, null);
				head.getGraphics().drawImage(skin.getSubimage(8+32, 8, 8, 8), 0, 0, null);
				return;
			}
		throw new ExceptionPlayerResources("Data are not valid");
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public BufferedImage getSkin() {
		return skin;
	}
	
	public Image getSkin(int size) {
		return skin.getScaledInstance(size, size, BufferedImage.SCALE_SMOOTH);
	}

	public BufferedImage getHead() {
		return head;
	}
	
	public Image getHead(int size) {
		return head.getScaledInstance(size, size, BufferedImage.SCALE_SMOOTH);
	}

	public static UUID findUserName(String userName) throws ExceptionPlayerResources {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+userName+"?at="+(System.currentTimeMillis()/1000));
			InputStreamReader input = new InputStreamReader(url.openStream());
			CharArrayWriter w = new CharArrayWriter();
			char[] in = new char[512];
			int len;
			while ((len = input.read(in)) != -1)
				w.write(in, 0, len);
			input.close();
			String json = new String(w.toCharArray());
			if (json.isEmpty())
				throw new ExceptionPlayerResources("Error find UserName");
			Document doc = Document.parse(json);
			String error = doc.getString("error");
			if (error != null)
				throw new ExceptionPlayerResources("Error find UserName");
			return UUID.fromString(doc.getString("id").replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
		} catch (Exception e) {
			throw new ExceptionPlayerResources("Error find UserName");
		}
	}
	
	public static void main(String[] args) {
		try {
			PlayerResources pr = new PlayerResources(UUID.fromString("c7690732-20b3-4fb5-ab26-f976544b3fe5"));
			BufferedImage skin = pr.getSkin();
			JFrame frame = new JFrame();
			JPanel panel = new JPanel(new BorderLayout(10, 10));
			panel.add(new JLabel(new ImageIcon(pr.getHead().getScaledInstance(512, 512, BufferedImage.SCALE_DEFAULT))), BorderLayout.WEST);
			panel.add(new JLabel(new ImageIcon(skin.getScaledInstance(skin.getWidth()*8, skin.getHeight()*8, BufferedImage.SCALE_DEFAULT))), BorderLayout.EAST);
			frame.setContentPane(panel);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		} catch (ExceptionPlayerResources | IOException e) {
			e.printStackTrace();
		}
	}
	
}
