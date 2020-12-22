package fr.tangv.sorcicubeapi.requests;

public enum RequestType {
	
	//SERVER
	
	//ERROR
	ERROR(RequestDataType.TEXT),
	//IDENTIFICATION
	DONT_AUTHENTIFIED(RequestDataType.NOTHING),
	AUTHENTIFIED(RequestDataType.NOTHING),
	IDENTIFICATION_REFUSED(RequestDataType.TEXT),
	KICK_BY_SERVER(RequestDataType.TEXT),
	CLOSED_SERVER(RequestDataType.NOTHING),
	
	//CLIENT
	
	//PLAYER
	PLAYER_INIT(RequestDataType.JSON),
	PLAYER_GET(RequestDataType.NOTHING),
	PLAYER_EXIST(RequestDataType.NOTHING),
	PLAYER_UPDATE(RequestDataType.JSON),
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
