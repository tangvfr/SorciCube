package fr.tangv.sorcicubeapi.clients;

public enum ClientType {

	Spigot(0b0000_0001);
	
	public final int mask;
	
	private ClientType(int mask) {
		this.mask = mask;
	}
	
	public boolean isThisType(int type) {
		return (type & mask) > 0;
	}
	
}
