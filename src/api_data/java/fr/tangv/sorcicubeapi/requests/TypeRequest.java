package fr.tangv.sorcicubeapi.requests;

public enum TypeRequest {

	//PLAYER
	PLAYER_INIT(TypeData.JSON),
	PLAYER_GET(TypeData.NOTHING),
	PLAYER_EXIST(TypeData.NOTHING),
	PLAYER_UPDATE(TypeData.JSON),
	//IDENTIFICATION
	IDENTIFICATION(TypeData.JSON);
	
	private TypeData type;
	
	private TypeRequest(TypeData type) {
		this.type = type;
	}
	
	public TypeData getType() {
		return type;
	}
	
	public boolean isType(TypeData type) {
		return this.type == type;
	}
	
}
