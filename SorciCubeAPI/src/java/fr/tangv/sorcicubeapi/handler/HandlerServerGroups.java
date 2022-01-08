package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;

import fr.tangv.sorcicubecore.handler.formater.HandlerGroupsFormater;
import fr.tangv.sorcicubecore.player.Group;

public class HandlerServerGroups extends HandlerServerObjectsAbstract<String, Group> {

	public volatile Group lowWeightGroup;
	
	public HandlerServerGroups() throws IOException {
		super(new HandlerGroupsFormater());
	}

	@Override
	public void update() {
		Group low = null;
		for (Group group : map.values())
			if (low == null || low.isUpperTo(group))
				low = group;
		this.lowWeightGroup = low;
	}
	
	public Group getLowWeightGroup() {
		return lowWeightGroup;
	}
	
}
