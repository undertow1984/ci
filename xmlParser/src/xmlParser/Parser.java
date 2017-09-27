package xmlParser;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
	private static final String resource = "//resources";
	private static String namePath = "/name";// [contains(text(),'PLACEHOLDER')]";
	private static String externalId = "/externalId";
	private static String uxDuration = "/uxDuration";
	private static String statusPath = "/status";
	private static String startTimePath = "/startTime";
	private static String devicePath = "//deviceId";
	private static String modelPath = "//model";
	private static String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private static SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public static void main(String[] args) {
		api.Api.CQL_NAME = args[0];
		api.Api.SECURITY_TOKEN = args[1];
		api.Api.jobName = args[3];
		api.Api.jobNumber = args[4];
		String tags = args[2];
		JSONObject json = null;
		String tagParam="";
		HashMap<String, String> deviceMap = new HashMap<String, String>();

		try {
			String[] tagList = tags.split("\\;;");
			int z=0;
			for (String tagItem : tagList) {
				tagParam=tagParam+"&tags["+z+"]="+tagItem;
				z++;
			}

			json = api.Api.retrieveTestExecutions(tagParam);
			String xml = xmlHeader + "<test>" + System.lineSeparator() + XML.toString(json) + "</test>";
			HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>> files = new HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>>();

			NodeList nodes = getXPathList(xml, resource);
			for (int i = 1; i <= nodes.getLength(); i++) {
				String name = getXPathValue(xml, resource + "[" + i + "]" + namePath);

				String runid = getXPathValue(xml, resource + "[" + i + "]" + externalId);
				if (!files.containsKey(runid)) {
					files.put(runid, new HashMap<String, ArrayList<HashMap<String, String>>>());
				}
				String device = getXPathValue(xml, resource + "[" + i + "]" + devicePath);
				if (!files.get(runid).containsKey(device)) {
					files.get(runid).put(device, new ArrayList<HashMap<String, String>>());
				}
				if (!deviceMap.containsKey(device)) {
					deviceMap.put(device, getXPathValue(xml, resource + "[" + i + "]" + modelPath));
				}
				HashMap<String, String> values = new HashMap<String, String>();
				values.put("name", name);
				values.put("duration", getXPathValue(xml, resource + "[" + i + "]" + uxDuration));
				values.put("status", getXPathValue(xml, resource + "[" + i + "]" + statusPath));
				values.put("startTime", getXPathValue(xml, resource + "[" + i + "]" + startTimePath));
				files.get(runid).get(device).add(values);
			}

			System.out.println(xml);
			for (String id : files.keySet()) {
				for (String key : files.get(id).keySet()) {
					StringBuilder xmlText = new StringBuilder();
					xmlText.append(xmlHeader);
					Long timeStamp = Long.MAX_VALUE;
					StringBuilder testCases = new StringBuilder();
					StringBuilder attributes = new StringBuilder();
					float time = 0;
					int fails = 0;
					int errors = 0;
					for (HashMap<String, String> tests : files.get(id).get(key)) {
						Long start = Long.parseLong(tests.get("startTime"));
						if (start < timeStamp) {
							timeStamp = start;
						}
						String[] status = { tests.get("status") };
						if (status[0].equals("FAILED")) {
							fails++;
						} else if (status[0].equals("ERROR")) {
							errors++;
						}
						String dur = tests.get("duration");
						time += Float.parseFloat(dur);
						testCases.append(parser(tests.get("name"), dur, status, 2) + System.lineSeparator());
					}
					String fileName = deviceMap.get(key) + "_" + key + "_" + sdf.format((new Date(timeStamp)));
					attributes.append(attribute("name", fileName) + " " + attribute("errors", String.valueOf(errors))
							+ " " + attribute("failures", String.valueOf(fails)) + " "
							+ attribute("time", String.valueOf(time)) + " "
							+ attribute("timestamp", ts.format(new Date(timeStamp))));
					xmlText.append(buildTag("TestSuite", attributes.toString(), testCases.toString(), 0));
					writeToFile(fileName + ".xml", xmlText.toString());
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

	private static void writeToFile(String filename, String text) {
		BufferedWriter writer = null;
		try {
			File file = new File(filename);
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public static String parser(String name, String time, String[] status, int tabs) {
		String[] names = name.split("#", 2);
		String attribs = attribute("classname", names[0]) + " " + attribute("name", names[1]) + " "
				+ attribute("time", time);
		ArrayList<String> subs = new ArrayList<String>();
		if (status[0].equals("FAILED")) {
			ArrayList<String> fail = new ArrayList<String>();
			String atts = "";
			if (status.length > 2) {
				fail.add(buildTabs(tabs + 1) + status[2]);

			}
			if (status.length > 1) {
				atts = attribute("message", status[1]);
			}
			subs.add(buildTag("failure", atts, fail, tabs + 1) + System.lineSeparator());
		}
		return buildTag("testcase", attribs, subs, tabs);
	}

	public static String attribute(String name, String value) {
		return name + "=" + "\"" + value + "\"";
	}

	public static String buildTag(String tag, String attributes, ArrayList<String> subs, int tabs) {
		StringBuilder xml = new StringBuilder();
		xml.append(buildTabs(tabs) + "<" + tag + " " + attributes);
		if (subs != null && subs.isEmpty()) {
			xml.append("/>");
		} else {
			xml.append(">" + System.lineSeparator());
			for (String sub : subs) {
				xml.append(/* buildTabs(1)+ */ sub /* + System.lineSeparator() */);
			}
			xml.append(buildTabs(tabs) + "</" + tag + ">");
		}
		return xml.toString();
	}

	public static String buildTag(String tag, String attributes, String subs, int tabs) {
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.add(subs);
		return buildTag(tag, attributes, tempList, tabs);
	}

	public static String buildTabs(int count) {
		StringBuilder tabs = new StringBuilder();
		for (int i = 0; i < count; i++) {
			tabs.append("\t");
		}
		return tabs.toString();
	}

	public static String getXPathValue(String xml, String XpathString)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		NodeList result = getXPathList(xml, XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0).getTextContent();
		}
	}

	public String getXPathAttribute(String xml, String attribute, String XpathString)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		NodeList result = getXPathList(xml, XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0).getAttributes().getNamedItem(attribute).getTextContent();
		}
	}

	public static NodeList getXPathList(String xml, String XpathString)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(XpathString);
		return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	}

}
