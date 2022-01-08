package fr.tangv.sorcicubecore.handler.formater;

import org.bson.Document;

import fr.tangv.sorcicubecore.player.Group;

public class HandlerGroupsFormater implements HandlerObjectsFormater<String, Group> {
	
	@Override
	public String getType() {
		return "group";
	}

	@Override
	public Document toDocument(Group value) {
		return value.toDocument();
	}

	@Override
	public String getKey(Group value) {
		return value.getName();
	}

	@Override
	public Group toValue(Document doc) {
		return Group.toGroup(doc);
	}

}
