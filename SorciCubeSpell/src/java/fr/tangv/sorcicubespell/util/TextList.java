package fr.tangv.sorcicubespell.util;

import java.util.Arrays;
import java.util.List;

public class TextList {

	public static List<String> textToList(String text) {
		if (text == null)
			return Arrays.asList(new String[0]);
		return Arrays.asList(text.replace("\r", "").split("\n"));
	}
	
	public static String listToText(List<String> list) {
		if (list == null)
			return "";
		String text = list.size() > 0 ? list.get(0) : "";
		for (int i = 1; i < list.size(); i++)
			text += "\r\n"+list.get(i);
		return text;
	}
	
}
