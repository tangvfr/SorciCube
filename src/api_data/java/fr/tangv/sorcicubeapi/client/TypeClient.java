package fr.tangv.sorcicubeapi.client;

public enum TypeClient {

	Config(0b0000_0001);
	
	public final int mask;
	
	private TypeClient(int mask) {
		this.mask = mask;
	}
	
	public boolean isThisType(int type) {
		return (type & mask) > 0;
	}
	
}
