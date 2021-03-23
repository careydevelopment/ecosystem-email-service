package com.careydevelopment.ecosystem.email.util;

/**
 * Utility methods for getting info from HTML markup
 */
public class WebParser {

	public static void main(String[] args) {
		String url = "https://plarium.com?landings=4";
		String domain = getDomainNameFromUrl(url);
		//System.err.println(domain);
	}
	
	public static String stripTags(String content) {
		content = content.replaceAll("<.*?>" , "");
		return content;
	}
	
	public static String stripTagsWithCarets(String content) {
		content = content.replaceAll("<.*?>" , "^");
		return content;
	}
	
	public static String getStringDelimitedString(String line, String startString, String endString) {
		String returnValue = "";
		
		int start = line.indexOf(startString);
		
		if (start > -1) {
			int end = line.indexOf(endString, start + 1);
			
			if (end > -1) {
				returnValue = line.substring(start, end);
			}
		}
		
		return returnValue;
	}
	
	public static String getAttributeFromLine(String line, String attribute) {
		String returnValue = "";
		attribute = attribute + "=";
		
		int start = line.indexOf(attribute);
		if (start > -1 && attribute != null) {
			int startLen = start + attribute.length() + 1;
			int end = line.indexOf("\"", startLen);
			
			if (end >-1) {
				returnValue = line.substring(startLen, end);
			}
		}
		
		return returnValue;
	}
	
	
	public static int findPointBeforeClosingElement(String contents, String elementName, int startingPoint) {
		String closingElement = transformElementNameToEndingElement(elementName);
		int closingPoint = contents.indexOf(closingElement, startingPoint + 1);
		return closingPoint;
	}
	
	
	public static int findPointAfterElement(String contents, String elementName, int startingPoint) {
		int closeTagLocation = -1;
		int startLocation = findElementLocation(contents, elementName, startingPoint);
		
		if (startLocation > -1) {
			closeTagLocation = findCloseTagLocation(contents, startLocation);			
		}
		
		return closeTagLocation;
	}
	
	public static int findCloseTagLocation(String contents, int startingPoint) {
		int closeTagLocation = contents.indexOf(">", startingPoint + 1);
		
		if (closeTagLocation > -1) {
			closeTagLocation++;
		}
		
		return closeTagLocation;
	}
	
	public static int findElementLocation(String contents, String elementName, int startingPoint) {
		int location = -1;
		String taggedElementName = "<" + elementName;
		location = contents.indexOf(taggedElementName, startingPoint);

		return location;
//		String endingChar = contents.substring(location + taggedElementName.length(), location + taggedElementName.length() + 1);
//		if (endingChar.equals("/") || endingChar.equals(" ")) {
//			return location;
//		} else {
//			return -1; //findElementLocation(contents, elementName, location + taggedElementName.length());
//		}
	}
	
	public static String transformElementNameToStartingElement(String elementName) {
		return "<" + elementName + ">";
	}
	
	public static String transformElementNameToEndingElement(String elementName) {
		return "</" + elementName + ">";
	}

	public static String getDomainNameFromUrl(String url) {
		String domainName = "";
		
		if (url.startsWith("http")) {
			int slashesLoc = url.indexOf("//");
			if (slashesLoc > -1) {
				int questionLoc = url.indexOf("?");
				if (questionLoc > -1) {
					url = url.substring(0, questionLoc);
				}

				int finalSlashLoc = url.indexOf("/", slashesLoc + 3);
				if (finalSlashLoc > -1) {
					domainName = url.substring(slashesLoc + 2, finalSlashLoc);
				} else {
					domainName = url.substring(slashesLoc + 2, url.length());
				}
			}
		}
		
		return domainName;
	}
}
