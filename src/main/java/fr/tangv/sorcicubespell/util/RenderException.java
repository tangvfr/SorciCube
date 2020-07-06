package fr.tangv.sorcicubespell.util;

public class RenderException {
	
	/*
	 *  Create By Tangv 
	*/
	
	/*private static String rightString(char c, String text, int max) {
		return generatedChar(' ', max-text.length())+text;
	}*/
	
	private static String leftString(char c, String text, int max) {
		return text+generatedChar(' ', max-text.length());
	}
	
	private static String centerString(char c, String text, int max) {
		int stringDecal = (max-text.length())/2;
		return generatedChar(' ', stringDecal)+text+generatedChar(' ', max-(text.length()+stringDecal));
	}
	
	private static String generatedChar(char c, int number) {
		String text = "";
		for (int i = 0; i < number; i++)
			text += c;
		return text;
	}
	
	public static String renderException(Exception e) {
		String lineLabel = "Line";
		String classeLabel = "Class";
		String methodeLabel = "Methode";
		String messageLabel = "Error Message:";
		int max = messageLabel.length();
		int messageLenght = e.getMessage() == null ? 4 : e.getMessage().length();
		if (max < messageLenght)
			max = messageLenght;
		int line = lineLabel.length();
		int classe = classeLabel.length();
		int methode = methodeLabel.length();
		StackTraceElement[] s = e.getStackTrace();
		for (int i = s.length-1; i > 0; i--) {
			StackTraceElement trace = s[i];
			String lineString = Integer.toString(trace.getLineNumber());
			if (line < lineString.length())
				line = lineString.length();
			if (classe < trace.getClassName().length())
				classe = trace.getClassName().length();
			String methodeString = trace.getMethodName();
			if (methode < methodeString.length())
				methode = methodeString.length();
		}
		int maxLine = line+3+classe+3+methode;
		if (max < maxLine)
			max = maxLine;
		methode = max-(maxLine-methode);
		String text = "\r\n";
		//new line
		text += "┏━"+generatedChar('━', max)+"━┓\r\n";
		//new line
		text += "┃ "+centerString(' ', messageLabel, max)+" ┃\r\n";
		//new line
		text += "┃ "+centerString(' ', e.getMessage(), max)+" ┃\r\n";
		//new line
		text += "┣━"+generatedChar('━', line)
		+"━┳━"+generatedChar('━', classe)
		+"━┳━"+generatedChar('━', methode)+"━┫\r\n";
		//new line
		text += "┃ "+centerString(' ', lineLabel, line)
			+" ┃ "+centerString(' ', classeLabel, classe)
			+" ┃ "+centerString(' ', methodeLabel, methode)+" ┃\r\n";
		//new line
		text += "┣━"+generatedChar('━', line)
		+"━╋━"+generatedChar('━', classe)
		+"━╋━"+generatedChar('━', methode)+"━┫\r\n";
		//new line
		for (int i = s.length-1; i > 0; i--) {
			StackTraceElement trace = s[i];
			String lineString = Integer.toString(trace.getLineNumber());
			text += "┃ "+leftString(' ', lineString, line)
			+" ┃ "+leftString(' ', trace.getClassName(), classe)
			+" ┃ "+leftString(' ', trace.getMethodName(), methode)+" ┃\r\n";
		}
		//new line
		text += "┗━"+generatedChar('━', line)
		+"━┻━"+generatedChar('━', classe)
		+"━┻━"+generatedChar('━', methode)+"━┛\r\n";
		//example border
		/*
		 * ┏━┳━┓
		 * ┃ ┃ ┃
		 * ┣━╋━┫
		 * ┃ ┃ ┃
		 * ┗━┻━┛
		*/
		return text;
	}

}
