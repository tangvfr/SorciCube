package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class LocationConfig implements ElementConfig {

	public String world;
	public double x;
	public double y;
	public double z;
	public float pitch;
	public float yaw;

	public LocationConfig(Document doc) {
		if (doc == null) {
			this.world = "world";
			this.x = 0.0;
			this.y = 0.0;
			this.z = 0.0;
			this.pitch = 0.0F;
			this.yaw = 0.0F;
		} else {
			this.world = doc.get("world", "world");
			this.x = doc.get("x", 0.0);
			this.y = doc.get("y", 0.0);
			this.z = doc.get("z", 0.0);
			this.pitch = doc.get("pitch", 0.0).floatValue();
			this.yaw = doc.get("yaw", 0.0).floatValue();
		}
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("world", world)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("pitch", (double) pitch)
				.append("yaw", (double) yaw);
		
	}

}
