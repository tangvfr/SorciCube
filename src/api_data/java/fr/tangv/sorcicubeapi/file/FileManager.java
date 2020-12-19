package fr.tangv.sorcicubeapi.file;

import java.util.concurrent.ConcurrentHashMap;

public class FileManager {

	private ConcurrentHashMap<String, File> files;
	private java.io.File folder;
	
	public FileManager(String pathFolder) throws Exception {
		this.files = new ConcurrentHashMap<String, File>();
		this.folder = new java.io.File(pathFolder);
		if (folder.exists()) {
			if (folder.isDirectory()) {
				for (File file : folder.listFiles())
					
			} else
				throw new Exception("FileManager don't is Folder !");
		} else {
			folder.mkdirs();
		}
	}
	
}
