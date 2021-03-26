package fr.tangv.sorcicubecore.requests;

public enum RequestType {
	
	/*
	 * SERVER
	 */
	
	//SERVER
	SPITGOT_SERVER_LIST(RequestDataType.JSON),
	SERVER_REFRESH(RequestDataType.NONE),
	//DATA
	ERROR(RequestDataType.TEXT),
	SUCCESSFUL(RequestDataType.NONE),
	//PLAYER
	PLAYER_REPONSE(RequestDataType.JSON),
	PLAYER_EXITSTING(RequestDataType.TEXT),
	PLAYER_UPDATING(RequestDataType.NONE),
	PLAYER_SEND(RequestDataType.TEXT),
	//CARDS
	CARDS_LIST(RequestDataType.JSON),
	//DEFAULT DECK
	DEFAULT_DECK_REPONSE(RequestDataType.JSON),
	//SPECTATOR
	SPECTATOR_UUID(RequestDataType.TEXT),
	//FIGHT_DATA
	FIGHT_DATA_ONE(RequestDataType.JSON),
	FIGHT_DATA_LIST(RequestDataType.JSON),
	//CONFIG
	CONFIG_SERVER_LIST_CONFIG(RequestDataType.ARRAY_TEXT),
	CONFIG_SERVER_CONFIG(RequestDataType.YAML),
	//PACKETS
	PACKETS_LIST(RequestDataType.JSON),
	PACKETS_NEWED(RequestDataType.JSON),
	//IDENTIFICATION
	DONT_AUTHENTIFIED(RequestDataType.NONE),
	AUTHENTIFIED(RequestDataType.NONE),
	ALREADY_AUTHENTIFIED(RequestDataType.NONE),
	COOLDOWN_AUTHENTIFIED(RequestDataType.NONE),
	IDENTIFICATION_REFUSED(RequestDataType.TEXT),
	//DISCONNECT ACRTIONS
	KICK(RequestDataType.TEXT),
	
	/*
	 * CLIENT
	 */
	
	//SERVER
	STOP_SERVER(RequestDataType.NONE),
	GET_SPITGOT_SERVER_LIST(RequestDataType.NONE),
	PLAYER_JOIN(RequestDataType.NONE),
	PLAYER_LEAVE(RequestDataType.NONE),
	START_SERVER_REFRESH(RequestDataType.NONE),
	//PLAYER
	PLAYER_INIT(RequestDataType.ARRAY_TEXT),
	PLAYER_GET(RequestDataType.NONE),
	PLAYER_EXIST(RequestDataType.NONE),
	PLAYER_UPDATE(RequestDataType.JSON),
	PLAYER_START_UPDATING(RequestDataType.NONE),
	PLAYER_START_SEND(RequestDataType.TEXT),
	//SPECTATOR
	SPECTATOR_ADD(RequestDataType.TEXT),
	SPECTATOR_PEEK(RequestDataType.NONE),
	//CARDS
	CARDS_INSERT(RequestDataType.JSON),
	CARDS_UPDATE(RequestDataType.JSON),
	CARDS_GET_ALL(RequestDataType.NONE),
	CARDS_DELETE(RequestDataType.NONE),
	//DEFAULT DECK
	DEFAULT_DECK_GET(RequestDataType.NONE),
	DEFAULT_DECK_UPDATE(RequestDataType.JSON),
	//FIGHT_DATA
	FIGHT_DATA_PLAYER_GET(RequestDataType.NONE),
	FIGHT_DATA_PLAYER_REMOVE(RequestDataType.NONE),
	FIGHT_DATA_GET_LIST(RequestDataType.NONE),
	FIGHT_DATA_ADD(RequestDataType.JSON),
	FIGHT_DATA_UPDATE(RequestDataType.JSON),
	FIGHT_DATA_REMOVE(RequestDataType.NONE),
	FIGHT_DATA_SERVER_REMOVE(RequestDataType.NONE),
	//CONFIG
	CONFIG_SERVER_LIST(RequestDataType.NONE),
	CONFIG_SERVER_GET(RequestDataType.NONE),
	CONFIG_SERVER_UPDATE(RequestDataType.YAML),
	//PACKETS
	PACKETS_NEW(RequestDataType.TEXT),
	PACKETS_REMOVE(RequestDataType.TEXT),
	PACKETS_UPDATE(RequestDataType.JSON),//name = last name in base 64
	PACKETS_GET_ALL(RequestDataType.NONE),
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
