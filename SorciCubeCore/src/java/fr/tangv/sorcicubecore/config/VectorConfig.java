package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class VectorConfig implements ElementConfig {

	public double x;
	public double y;
	public double z;

	//for clone
	private VectorConfig(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public VectorConfig(Document doc) {
		if (doc == null) {
			this.x = 0.0;
			this.y = 0.0;
			this.z = 0.0;
		} else {
			this.x = doc.get("x", 0.0);
			this.y = doc.get("y", 0.0);
			this.z = doc.get("z", 0.0);
		}
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document()
				.append("x", x)
				.append("y", y)
				.append("z", z);
	}

	public VectorConfig clone() {
		return new VectorConfig(x, y, z);
	}
	
}
