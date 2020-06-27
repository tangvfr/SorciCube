package fr.tangv.sorcicubespell;

public class Test {

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			System.out.println("i: "+i);
			int r1 = ((i/5)*9)+(i%5)+3;
			System.out.println("r1: "+r1);
			int a = (r1%9)-3;
			//if (a >= 0 && a < 5)
			int r2 = ((r1/9)*5)+a;
			System.out.println("r2: "+r2);
		}
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 9; x++) {
				int n = (y*9)+x;
				int a = (n%9)-3;
				System.out.print("["+((a >= 0 && a < 5) ? " " : "X")+"]");
			}
			System.out.print("\r\n");
		}
	}
	
}
