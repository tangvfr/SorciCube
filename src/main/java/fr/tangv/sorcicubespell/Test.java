package fr.tangv.sorcicubespell;

public class Test {

	public static void main(String[] args) {
		String text = "caca proute !";
		System.out.println(text);
		String mix = "{textures:{SKIN:{url:\""+text+"\"}}}";
		System.out.println(mix);
		System.out.println(mix.substring(22, mix.length()-4));
	}
	
}
