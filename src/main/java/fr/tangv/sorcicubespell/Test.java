package fr.tangv.sorcicubespell;

public class Test {
	
	public static void main(String[] args) {
		
		for (int round = 0; round < 10; round++) {
			int mana = ((round+1)/2)+2;
			/*if (round == 1)
				mana = start_mana+1;*/
			System.out.println("round:"+(round+1)+" "+(round%2 == 0 ? "player1" : "player2")+" mana:"+mana);
		}
		
	}
	
}
