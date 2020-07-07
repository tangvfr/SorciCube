package fr.tangv.sorcicubespell.logi;

import java.io.File;

import fr.tangv.sorcicubespell.util.LibLoader;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerLogi {

	public static void main(String[] args) {
		try {
			new ManagerLogi(!(args.length >= 1 && args[0].equalsIgnoreCase("-noloadlibs")));
		} catch (Exception e) {
			System.err.println(RenderException.renderException(e));
		}
	}
	
	private FrameLogi frameLogi;
	
	public ManagerLogi(boolean loadLibs) throws Exception {
		System.out.println("Load libs: "+loadLibs);
		if (loadLibs)
			LibLoader.loadLibs(new File("."+File.separatorChar+"libs"));
		this.frameLogi = new FrameLogi(this);
		this.frameLogi.setVisible(true);
	}
	
}
