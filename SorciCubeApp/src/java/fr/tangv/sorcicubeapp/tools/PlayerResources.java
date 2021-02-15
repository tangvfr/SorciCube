package fr.tangv.sorcicubeapp.tools;

import java.awt.BorderLayout;
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
	
	private PlayerResources(UUID uuid) throws ExceptionPlayerResources, IOException {
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
				this.head = skin.getSubimage(8, 8, 8, 8);
				head.getGraphics().drawImage(skin.getSubimage(8+32, 8, 8, 8), 0, 0, null);
				return;
			}
		throw new ExceptionPlayerResources("Data are not valid");
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public BufferedImage getSkin() {
		return skin;
	}

	public BufferedImage getHead() {
		return head;
	}

	public static void main(String[] args) {
		try {
			PlayerResources pr = new PlayerResources(UUID.fromString("ffe558be-2e97-4eca-a7d6-b6f9012ff1ba"));
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
