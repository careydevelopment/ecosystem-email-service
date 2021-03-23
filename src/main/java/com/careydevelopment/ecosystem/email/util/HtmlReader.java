package com.careydevelopment.ecosystem.email.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * For reading and parsing entire web pages to get multiple pieces of info
 */
public class HtmlReader {

	private static final Logger LOG = LoggerFactory.getLogger(HtmlReader.class);
	
	private String contents;
	
	public HtmlReader(String contents) {
		this.contents = contents;
	}
	
	public List<String> getLinesByKeyword(String keyword) throws IOException, MalformedURLException {
		List<String> list = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new StringReader(contents))) {
			list = br.lines().filter(line -> {
				return (line.indexOf(keyword) > -1);
			}).collect(Collectors.toList());
		} catch (IOException ie) {
			LOG.error("Problem getting attribute!", ie);
		}
		
		return list;
	}
	
	public String getTitle() {
		return getElementValue("title");
	}
	
	public void stripTag(String elementName) {
		String startTag = WebParser.transformElementNameToStartingElement(elementName);
		String endTag = WebParser.transformElementNameToEndingElement(elementName);
		
		int start = contents.indexOf(startTag);
		if (start > -1) {
			int end = contents.indexOf(endTag, start + 1);
			if (end > -1) {
				end += startTag.length();
				
				StringBuilder sb = new StringBuilder();
				sb.append(contents.substring(0, start));
				sb.append(contents.substring(end + 1, contents.length()));
				
				contents = sb.toString();
			}
		}
	}
	
	public String getElementValue(String elementName) {
		String startTag = WebParser.transformElementNameToStartingElement(elementName);
		String endTag = WebParser.transformElementNameToEndingElement(elementName);
		
		String value = "";
		int start = contents.indexOf(startTag);
		if (start > -1) {
			int end = contents.indexOf(endTag, start + 1);
			if (end > -1) {
				start += startTag.length();
				value = contents.substring(start, end);
			}
		}
		
		return value;
	}
	
	public String getEverythingBetweenTwoString(String startString, String endString) {
		String everything = "";
		
		if (contents != null) {
			int start = contents.indexOf(startString);
			if (start > -1) {
				int end = contents.indexOf(endString, start + 1);
				if (end > -1) {
					start = start + startString.length();
					everything = contents.substring(start, end);
				}
			}			
		}
		
		return everything;
	}
	
	public String getAttributeFromLineWithToken(String token, String attribute) {
		String value = "";
		
		if (contents != null) {
			try (BufferedReader br = new BufferedReader(new StringReader(contents))) {
				List<String> lines = br.lines().filter(line -> {
					return (line.indexOf(token) > -1);
				}).collect(Collectors.toList());

				for (String line : lines) {
					if (line.indexOf(attribute) > -1) {
						value = WebParser.getAttributeFromLine(line, attribute);
						break;
					}
				}
			} catch (IOException ie) {
				LOG.error("Problem getting attribute!", ie);
			}			
		}
		
		return value;
	}	
	
		
	public String getContents() {
		return contents;
	}
	
	
	public String getJsonValue(String jsonPropertyName) {
		String value = "";
		
		jsonPropertyName = "\"" + jsonPropertyName + "\"";
		int loc = contents.indexOf(jsonPropertyName);
		if (loc > -1) {
			loc += jsonPropertyName.length();
			int colonLoc = contents.indexOf(":", loc);
			if (colonLoc > -1) {
				int firstQuoteLoc = contents.indexOf("\"", colonLoc);
				if (firstQuoteLoc > -1) {
					int secondQuoteLoc = contents.indexOf("\"", firstQuoteLoc + 1);
					if (secondQuoteLoc > -1) {
						value = contents.substring(firstQuoteLoc + 1, secondQuoteLoc);
					}
				}
			}
		}
		
		return value;
	}
	
	
	public List<String> getStanzasByFootprint(String footprint) {
		List<String> stanzas = new ArrayList<String>();
		
		int start = 0;
		int loc = contents.indexOf(footprint, start);
		
		while (loc > -1) {
			int firstChevronLoc = contents.lastIndexOf("<", loc);
			
			if (firstChevronLoc > -1) {
				String stanza = getWholeStanza(firstChevronLoc);
				//LOG.debug(stanza);
				if (stanzas != null) stanzas.add(stanza);
			}
			
			loc = contents.indexOf(footprint, loc+footprint.length());
		}
		
		return stanzas;
	}
	
	
	public List<String> getStanzasByEverythingBetweenTwoStrings(String frontFootprint, String backFootprint) {
		List<String> stanzas = new ArrayList<String>();
		
		int start = 0;
		int loc = contents.indexOf(frontFootprint, start);
		
		while (loc > -1) {
			int endLoc = contents.indexOf(backFootprint, loc + 1);
			
			if (endLoc > -1) {
				String stanza = contents.substring(loc, endLoc + backFootprint.length());
				stanzas.add(stanza);
			}
			
			loc = contents.indexOf(frontFootprint, loc + frontFootprint.length());
		}
		
		return stanzas;
	}
	
	
	private String getWholeStanza(int loc) {
		int startingPoint = loc;
		String elementName = getElementNameAtLocation(loc);
		
		String startingElement = WebParser.transformElementNameToStartingElement(elementName);
		startingElement = startingElement.substring(0, startingElement.length() - 1);
		String endingElement = WebParser.transformElementNameToEndingElement(elementName);

		int endingElementLoc = contents.indexOf(endingElement, loc + startingElement.length());
		int startingElementLoc = contents.indexOf(startingElement, loc + startingElement.length());
				
		//System.err.println("Ending element loc=" + endingElementLoc);
		
		while (endingElementLoc > -1 && startingElementLoc > -1 && startingElementLoc < endingElementLoc) {
			loc = endingElementLoc;
			endingElementLoc = contents.indexOf(endingElement, loc + endingElement.length());
			startingElementLoc = contents.indexOf(startingElement, loc + endingElement.length());
		}
		
		//System.err.println("start & end "+ startingPoint + " " + (endingElementLoc + endingElement.length()));
		
		try {
			String stanza = contents.substring(startingPoint, endingElementLoc + endingElement.length());
			return stanza;
		} catch (StringIndexOutOfBoundsException se) {
			LOG.warn("Problem reading all stanzas");
		}
		
		return null;
	}
	
	private String getElementNameAtLocation(int loc) {
		String elementName = "";
		
		int closeChevron = contents.indexOf(">", loc + 1);
		
		if (closeChevron > -1) {
			int spaceLoc = contents.indexOf(" ", loc + 1);
			if (spaceLoc > -1 && spaceLoc < closeChevron) {
				elementName = contents.substring(loc+1, spaceLoc);
			} else {
				elementName = contents.substring(loc+1, closeChevron);
			}
		}
		
		return elementName;
	}
}
