package file;

import java.util.ArrayList;

public class XMLManager {
	public static String buildXMLLine(String tag, String value) {
		StringBuilder xml = new StringBuilder();
		xml.append("<").append(tag).append(">");
		xml.append(value);
		xml.append("</").append(tag).append(">");
		return xml.toString();
	}
	
	public static String buildXMLLine(String tag, double value) {
		return buildXMLLine(tag, ""+value);
	}
	
	public static ArrayList<String> encapsulateInTag(String tag, String encapsulatedXml) {
		ArrayList<String> xmlLines = new ArrayList<String>();
		String xml = "";
		xml = "<" + tag + ">";
		xmlLines.add(xml);
		xml = "\t" + encapsulatedXml;
		xmlLines.add(xml);
		xml = "</" + tag + ">";
		xmlLines.add(xml);
		return xmlLines;
	}
	
	public static ArrayList<String> encapsulateInTag(String tag, ArrayList<String> encapsulatedXml) {
		ArrayList<String> xmlLines = new ArrayList<String>();
		String xml = "";
		xml = "<" + tag + ">";
		xmlLines.add(xml);
		for(String line : encapsulatedXml) {
			xml = "\t" + line;
			xmlLines.add(xml);
		}
		xml = "</" + tag + ">";
		xmlLines.add(xml);
		return xmlLines;
	}
	
	public static String getTagFromXMLLine(String xml) {
		StringBuilder tag = new StringBuilder();
		boolean openBracket = false;
		for(int i=0; i<xml.length(); i++) {
			char c = xml.charAt(i);
			if(c == '<') {
				openBracket = true;
			}
			else if(c == '>' && openBracket) {
				break;
			}
			else if(openBracket) {
				tag.append(c);
			}
		}
		return tag.toString();
	}
	
	public static String getValueFromXMLLine(String xml) {
		StringBuilder value = new StringBuilder();
		boolean closedBracket = false;
		for(int i=0; i<xml.length(); i++) {
			char c = xml.charAt(i);
			if(c == '<' && closedBracket) {
				break;
			}
			else if(c == '>') {
				closedBracket = true;
			}
			else if(closedBracket) {
				value.append(c);
			}
		}
		return value.toString();
	}
	
	public static String getSpecificValueFromXMLFile(ArrayList<String> xmlLines, String tag) {
		String value = "";
		
		for(String xml : xmlLines) {
			String lineTag = getTagFromXMLLine(xml);
			if(lineTag.equals(tag)) {
				value = getValueFromXMLLine(xml);
			}
		}
		
		return value;
	}
	
	public static int getIntegerValueFromXMLFile(ArrayList<String> xmlLines, String tag) {
		String valueString = getSpecificValueFromXMLFile(xmlLines, tag);
		int value = 0;
		if(!valueString.equals("")) {
			try {
				double valueDouble = Double.parseDouble(valueString);
				value = (int)Math.round(valueDouble);
			}
			catch(NumberFormatException e) {
				value = -1;
			}
		}
		return value;
	}
	
	public static double getDoubleValueFromXMLFile(ArrayList<String> xmlLines, String tag) {
		String valueString = getSpecificValueFromXMLFile(xmlLines, tag);
		double value = 0;
		if(!valueString.equals("")) {
			try {
				value = Double.parseDouble(valueString);
			}
			catch(NumberFormatException e) {
				value = -1;
			}
		}
		return value;
	}
	
	public static ArrayList<String> getEncapsulatedXML(ArrayList<String> xmlLines, String tag) {
		ArrayList<String> encapsulatedXmlLines  = new ArrayList<String>();
		
		boolean isEncapsulated = false;
		for(String xml : xmlLines) {
			String lineTag = getTagFromXMLLine(xml);
			if(lineTag.equals(tag)) {
				isEncapsulated = !isEncapsulated;
			}
			else if(isEncapsulated) {
				encapsulatedXmlLines.add(xml);
			}
		}
		
		return encapsulatedXmlLines;
	}
	
	public static ArrayList<String> removeSections(ArrayList<String> xmlLines, String tag, boolean firstOnly) {
		ArrayList<String> newXmlLines = new ArrayList<String>();
		newXmlLines.addAll(xmlLines);
		boolean inTag = false;
		for(String xml : xmlLines) {
			String currentTag = getTagFromXMLLine(xml);
			if(currentTag.equals(tag)) {
				inTag = true;
				newXmlLines.remove(xml);
			}
			else if(currentTag.equals("/" + tag)) {
				inTag = false;
				newXmlLines.remove(xml);
				if(firstOnly) {
					break;
				}
			}
			else if(inTag) {
				newXmlLines.remove(xml);
			}
		}
		return newXmlLines;
	}
}
