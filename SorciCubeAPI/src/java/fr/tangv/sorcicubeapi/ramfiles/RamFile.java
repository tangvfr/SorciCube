package fr.tangv.sorcicubeapi.ramfiles;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import fr.tangv.sorcicubecore.clients.Client;

public class RamFile {
	
	private volatile String data;
	private final java.io.File file;
	
	public RamFile(java.io.File file) throws IOException {
		this.file = file;
		if (file.exists()) {
			if (file.isFile()) {
				loadData();
			} else {
				throw new IOException("That don't is File !");
			}
		} else {
			file.createNewFile();
		}
	}

	public synchronized boolean delete() {
		return file.delete();
	}
	
	public synchronized void loadData() throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		in.read(data);
		in.close();
		this.data = new String(data, Client.CHARSET);
	}
	
	public synchronized void writeData(String data) throws IOException {
		this.data = data;
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), Client.CHARSET);
		out.write(data);
		out.close();
	}

	public String getData() {
		return data;
	}
	
}
