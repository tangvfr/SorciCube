package fr.tangv.sorcicubespell;

public class Test {
	
	public static void main(String[] args) {
		try {
			int number = Integer.parseInt("dada");
		} catch (Exception e) {
			String message = "Error Message: "+e.getMessage()+"\r\n";
			StackTraceElement[] s = e.getStackTrace();
			for (int i = s.length-1; i > 0; i--) {
				StackTraceElement trace = s[i];
				message += "\r\n"+"  class: "+trace.getClassName()+" | methode: "+trace.getMethodName()+"()"+" | line: "+trace.getLineNumber()+" | file: "+trace.getFileName();
			}
			System.out.println(message);
		}
	}
	
}
