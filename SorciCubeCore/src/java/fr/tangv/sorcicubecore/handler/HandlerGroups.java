package fr.tangv.sorcicubecore.handler;

import java.io.IOException;

import fr.tangv.sorcicubecore.handler.formater.HandlerGroupsFormater;
import fr.tangv.sorcicubecore.player.Group;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerGroups extends HandlerObjectsAbstract<String, Group> {

	public HandlerGroups(SorciClient sorci) throws IOException, ResponseRequestException, RequestException {
		super(sorci, new HandlerGroupsFormater());
	}
	
	@Override
	public void refresh() throws IOException, ResponseRequestException, RequestException {
		super.refresh();
		for (Group group : map.values())
			group.calcAllPerms(cloneMap());
	}
	
	@Override
	protected void updateValue(Group group) {
		group.calcAllPerms(cloneMap());
	}
	
}
