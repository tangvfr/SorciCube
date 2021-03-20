package fr.tangv.sorcicubecore.clients;

public enum ClientType {

	SPIGOT((byte) 		0b0000_0001),
	APPLICATION((byte) 	0b0000_0010),
	TEST((byte) 		0b0000_0100);
	
	public final byte mask;
	
	private ClientType(byte mask) {
		this.mask = mask;
	}
	
	public boolean isType(byte type) {
		return (type & mask) != 0;
	}
	
}
