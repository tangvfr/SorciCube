package fr.tangv.sorcicubecore.config;

public class ConfigParseException extends Exception {

	private static final long serialVersionUID = -6934841316457113363L;

	public ConfigParseException(String message) {
		super(message);
	}
	
	public ConfigParseException(String message, Throwable thro) {
		super(message, thro);
	}
	
	public ConfigParseException(Throwable thro) {
		super(thro.getClass().getSimpleName()+": "+thro.getMessage(), thro);
	}
	
}
