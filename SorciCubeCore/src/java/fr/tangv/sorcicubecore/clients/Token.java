package fr.tangv.sorcicubecore.clients;

public class Token {

	public static String generateToken() {
		String token = "";
		for (int c = 0; c < 64; c++) {
			int i = (int) (Math.random()*62);
			if (i >= 36)
				i += 48+7+6;
			else if (i >= 10)
				i += 48+7;
			else
				i += 48;
			token += ((char) i);
		}
		return token;
	}
	
	public static void main(String[] args) {
		System.out.println("token: "+generateToken());
	}
	
}
