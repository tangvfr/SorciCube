package fr.tangv.sorcicubespell;

public class Test {

	public static void main(String[] args) {
		int max = 6;
		for (int i = 0; i < max; i++) {
			if ((i % 5) == 0) {
				System.out.println("reset");
			}
			System.out.println("number: "+i);
		}
		System.out.println("post");
	}
	
}
