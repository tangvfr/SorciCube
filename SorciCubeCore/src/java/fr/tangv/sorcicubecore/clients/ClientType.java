package fr.tangv.sorcicubecore.clients;

public enum ClientType {

	SPIGOT((byte) 		0b0000_0001),
	APPLICATION((byte) 	0b0000_0010);
	
	public final byte mask;
	
	private ClientType(byte mask) {
		this.mask = mask;
	}
	
	public boolean isThisType(byte type) {
		return (type & mask) != 0;
	}
	
}
