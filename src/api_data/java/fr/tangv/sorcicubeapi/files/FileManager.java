package fr.tangv.sorcicubeapi.files;

import java.util.concurrent.ConcurrentHashMap;

public class FileManager {

	private final static char SEPARATOR = java.io.File.separatorChar;
	
	private ConcurrentHashMap<String, File> files;
	private java.io.File folder;
	
	public FileManager(String pathFolder) throws Exception {
		this.files = new ConcurrentHashMap<String, File>();
		this.folder = new java.io.File("."+SEPARATOR+"data"+SEPARATOR+pathFolder);
		if (folder.exists()) {
			if (folder.isDirectory()) {
				for (java.io.File file : folder.listFiles())
					files.put(file.getName(), new File(file));
			} else
				throw new Exception("That don't is folder !");
		} else {
			folder.mkdirs();
		}
	}
	
	public boolean has(String name) {
		return files.containsKey(name);
	}
	
	public File get(String name) {
		return files.get(name);
	}
	
	public void insert(String name, String data) throws Exception {
		files.put(name, new File(new java.io.File(folder.getPath()+SEPARATOR+name)));
	}
	
	public boolean delete(String name) {
		return files.remove(name).delete();
	}
	
	public void update(String name, String data) throws Exception {
		files.get(name).writeData(data);;
	}
	
}
