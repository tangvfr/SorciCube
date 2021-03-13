package fr.tangv.sorcicubecore.ramfiles;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class RamFilesManager {

	private final static char SEPARATOR = File.separatorChar;
	
	private ConcurrentHashMap<String, RamFile> files;
	private File folder;
	
	public RamFilesManager(String pathFolder) throws IOException {
		this.files = new ConcurrentHashMap<String, RamFile>();
		this.folder = new java.io.File("."+SEPARATOR+"data"+SEPARATOR+pathFolder);
		if (folder.exists()) {
			if (folder.isDirectory()) {
				for (java.io.File file : folder.listFiles())
					files.put(file.getName(), new RamFile(file));
			} else
				throw new IOException("That don't is folder !");
		} else {
			folder.mkdirs();
		}
	}
	
	public Enumeration<String> list() {
		return files.keys();
	}
	
	public boolean has(String name) {
		return files.containsKey(name);
	}
	
	public RamFile get(String name) {
		return files.get(name);
	}
	
	public void insert(String name, String data) throws IOException {
		RamFile rf = new RamFile(new File(folder.getPath()+SEPARATOR+name));
		rf.writeData(data);
		files.put(name, rf);
	}
	
	public boolean delete(String name) {
		return files.remove(name).delete();
	}
	
	public void update(String name, String data) throws IOException {
		files.get(name).writeData(data);;
	}
	
}
