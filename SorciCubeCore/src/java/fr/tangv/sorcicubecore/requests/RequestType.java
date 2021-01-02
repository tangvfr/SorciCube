package fr.tangv.sorcicubecore.requests;

public enum RequestType {
	
	/*
	 * SERVER
	 */
	
	//DATA
	ERROR(RequestDataType.TEXT),
	SUCCESSFUL(RequestDataType.NOTHING),
	//PLAYER
	PLAYER_REPONSE(RequestDataType.JSON),
	PLAYER_EXITSTING(RequestDataType.TEXT),
	//CARDS
	CARDS_LIST(RequestDataType.JSON),
	//DEFAULT DECK
	DEFAULT_DECK_REPONSE(RequestDataType.JSON),
	//SPECTATOR
	SPECTATOR_UUID(RequestDataType.TEXT),
	//FIGHT_DATA
	
	//IDENTIFICATION
	DONT_AUTHENTIFIED(RequestDataType.NOTHING),
	AUTHENTIFIED(RequestDataType.NOTHING),
	ALREADY_AUTHENTIFIED(RequestDataType.NOTHING),
	COOLDOWN_AUTHENTIFIED(RequestDataType.NOTHING),
	IDENTIFICATION_REFUSED(RequestDataType.TEXT),
	//DISCONNECT ACRTIONS
	KICK(RequestDataType.TEXT),
	
	/*
	 * CLIENT
	 */
	
	//PLAYER
	PLAYER_INIT(RequestDataType.TEXT),
	PLAYER_GET(RequestDataType.NOTHING),
	PLAYER_EXIST(RequestDataType.NOTHING),
	PLAYER_UPDATE(RequestDataType.JSON),
	//SPECTATOR
	SPECTATOR_ADD(RequestDataType.TEXT),
	SPECTATOR_PEEK(RequestDataType.NOTHING),
	//CARDS
	CARDS_INSERT(RequestDataType.JSON),
	CARDS_UPDATE(RequestDataType.JSON),
	CARDS_GET_ALL(RequestDataType.NOTHING),
	CARDS_DELETE(RequestDataType.NOTHING),
	//DEFAULT DECK
	DEFAULT_DECK_GET(RequestDataType.NOTHING),
	DEFAULT_DECK_UPDATE(RequestDataType.JSON),
	//FIGHT_DATA
	
	//IDENTIFICATION
	IDENTIFICATION(RequestDataType.JSON);
	
	private RequestDataType typeData;
	
	private RequestType(RequestDataType typeData) {
		this.typeData = typeData;
	}
	
	public RequestDataType getTypeData() {
		return typeData;
	}
	
	public boolean isTypeData(RequestDataType typeData) {
		return this.typeData == typeData;
	}
	
}
