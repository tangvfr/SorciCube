package fr.tangv.sorcicubecore.player;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

public class Group {

	private final String name;
	private final String display;
	private final String prefix;
	private final int weight;
	private final Vector<String> perms;
	private final ConcurrentHashMap<String, Boolean> allPerms;
	
	public Group(
				String name,
				String display,
				String prefix,
				int weight,
				Vector<String> perms
				) {
		this.name = name;
		this.display = display;
		this.prefix = prefix;
		this.weight = weight;
		this.perms = perms;
		this.allPerms = new ConcurrentHashMap<String, Boolean>();
	}
	
	public void calcAllPerms(Map<String, Group> groups) {
		allPerms.clear();
		calcPerms(groups, name);
	}
	
	private void calcPerms(Map<String, Group> groups, String name) {
		Group group = groups.remove(name);
		if (group != null)
			for (String perm : group.getPerms())
				if (perm.startsWith("[")) {
					calcPerms(groups, perm.substring(1));
				} else if (perm.startsWith("-")) {
					addPerm(perm.substring(1), false);
				} else {
					addPerm(perm, true);
				}
	}
	
	private void addPerm(String perm, boolean permissions) {
		if (!perm.isEmpty())
			allPerms.put(perm, permissions);
	}

	public String getName() {
		return name;
	}

	public String getDisplay() {
		return display;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public int getWeight() {
		return weight;
	}
	
	public boolean isUpperTo(Group group) {
		return group.weight < weight;
	}

	public Vector<String> getPerms() {
		return perms;
	}

	public ConcurrentHashMap<String, Boolean> getAllPerms() {
		return allPerms;
	}
	
	public Document toDocument() {
		return new Document()
				.append("name", name)
				.append("display", display)
				.append("prefix", prefix)
				.append("weight", weight)
				.append("perms", perms);
	}
	
	public static Group toGroup(Document doc) {
		return new Group(
				doc.getString("name"),
				doc.getString("display"),
				doc.getString("prefix"),
				doc.getInteger("weight"),
				new Vector<String>(doc.getList("perms", String.class))
			);
	}
	
}
