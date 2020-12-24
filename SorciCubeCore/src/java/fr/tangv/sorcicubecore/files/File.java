package fr.tangv.sorcicubecore.files;

import java.io.CharArrayWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import fr.tangv.sorcicubecore.clients.Client;

public class File {
	
	private volatile String data;
	private final java.io.File file;
	
	public File(java.io.File file) throws Exception {
		this.file = file;
		if (file.exists()) {
			if (file.isFile()) {
				loadData();
			} else
				throw new Exception("That don't is File !");
		} else {
			file.createNewFile();
		}
	}

	public synchronized boolean delete() {
		return file.delete();
	}
	
	public synchronized void loadData() throws IOException {
		InputStreamReader in = new InputStreamReader(new FileInputStream(file), Client.CHARSET);
		CharArrayWriter data = new CharArrayWriter();
		char[] chars = new char[16_384];//16kio
		int len;
		while ((len = in.read(chars)) != -1)
			data.write(chars, 0, len);
		in.close();
		this.data = new String(data.toCharArray());
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
