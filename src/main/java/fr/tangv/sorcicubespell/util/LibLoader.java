package fr.tangv.sorcicubespell.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class LibLoader {

	public static void loadLibs(File folder, Object sorci) throws Exception {
		if (!folder.exists())
			folder.mkdirs();
		extractAndLoadLib(folder, "bson-4.0.3.jar", sorci);
		extractAndLoadLib(folder, "mongodb-driver-core-4.0.3.jar", sorci);
		extractAndLoadLib(folder, "mongodb-driver-sync-4.0.3.jar", sorci);
	}
	
	private static InputStream getInput(Object sorci, String name) throws Exception {
		if (sorci == null)
			return ClassLoader.getSystemResourceAsStream(name);
		Method method = sorci.getClass().getMethod("getResource", String.class);
		return (InputStream) method.invoke(sorci, name);
	}
	
	private static void extractAndLoadLib(File folder, String name, Object sorci) throws Exception {
		File file = new File(folder.getAbsolutePath()+File.separatorChar+name);
		if (!file.exists()) {
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			InputStream in = getInput(sorci, name);
			int lenght;
			byte[] buf = new byte[102_400];
			while ((lenght = in.read(buf)) != -1)
				out.write(buf, 0, lenght);
			out.close();
			in.close();
		}
		addURL(file.toURI().toURL());
	}
	
	private static void addURL(URL url) throws Exception {
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{url}); 
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }        
    }
	
}
