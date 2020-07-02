package fr.tangv.sorcicubespell;

import fr.tangv.sorcicubespell.util.RenderException;

public class Test {
	
	public static void main(String[] args) {
		try {
			int number = Integer.parseInt("dada");
		} catch (Exception e) {
			System.out.println(RenderException.renderException(e));
		}
	}
	
}
