package fr.tangv.sorcicubecore.util;

public class Format {

	public static String complet(String src, int lenght, char c) {
		while (src.length() < lenght)
			src = c+src;
		return src;
	}
	
	public static String complet(long src, int lenght) {
		return complet(Long.toString(src), lenght, '0');
	}
	
	public static String formatTime(long time) {
		long h = time/3600_000;
		long m = (time%3600_000)/60_000;
		long s = ((time%3600_000)%60_000)/1000;
		return complet(h, 4)+":"+complet(m, 2)+":"+complet(s, 2);
	}
	
}
