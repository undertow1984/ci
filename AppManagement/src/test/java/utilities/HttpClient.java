package utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class HttpClient {
	private static final String UTF_8 = "UTF-8";
	private static final String HTTPS = "https://";
	private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
	private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
	private Proxy proxy;

	// constructor for the Proxy connection
	public HttpClient(Proxy proxy) {
		this.proxy = proxy;
	}

	// default contructor
	public HttpClient() {

	}

	// makes the api call
	public String sendRequest(String url) throws IOException, URISyntaxException {
		URL obj = new URL(url);
		HttpURLConnection con = null;

		// setting proxy if available
		if (proxy != null) {
			con = (HttpURLConnection) obj.openConnection(proxy);
		} else {
			con = (HttpURLConnection) obj.openConnection();
		}
		System.out.println("Sending GET request to URL : " + obj.toURI());
		con.setRequestMethod("GET");
		con.connect();

		int responseCode = con.getResponseCode();
		String response = "";

		if (responseCode > HttpURLConnection.HTTP_OK) {
			handleError(con);
		} else {
			response = getStream(con);
		}

		System.out.println("Response Code : " + responseCode);
		System.out.println("Response message: " + response.toString());

		return response;
	}

	// processes errors returned by server
	private void handleError(HttpURLConnection connection) throws IOException {
		String msg = "Request failed: ";
		InputStream errorStream = connection.getErrorStream();
		if (errorStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(errorStream, UTF_8);
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			try {
				StringBuilder builder = new StringBuilder();
				String outputString;
				while ((outputString = bufferReader.readLine()) != null) {
					if (builder.length() != 0) {
						builder.append("\n");
					}
					builder.append(outputString);
				}
				String response = builder.toString();
				msg += "Response: " + response;
			} finally {
				bufferReader.close();
			}
		}
		System.out.println(msg);
		throw new RuntimeException(msg);
	}

	// gets body of the response
	private String getStream(HttpURLConnection connection) throws IOException {
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, UTF_8);
		BufferedReader bufferReader = new BufferedReader(inputStreamReader);
		String response = "";
		try {
			StringBuilder builder = new StringBuilder();
			String outputString;
			while ((outputString = bufferReader.readLine()) != null) {
				if (builder.length() != 0) {
					builder.append("\n");
				}
				builder.append(outputString);
			}
			response = builder.toString();
		} finally {
			bufferReader.close();
		}
		return response;
	}

	// parses the xml response body
	public String getXMLValue(String xml, String node) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));

		return parse.getElementsByTagName(node).item(0).getTextContent();
	}

	// parses the json response body
	public String getJsonValue(String json, String node) {
		JSONObject obj = new JSONObject(json);
		String n = obj.getString(node);
		System.out.println(n);
		return n;
	}

	private byte[] readFile(File path) throws FileNotFoundException, IOException {
		int length = (int) path.length();
		byte[] content = new byte[length];
		InputStream inStream = new FileInputStream(path);
		try {
			inStream.read(content);
		} finally {
			inStream.close();
		}
		return content;
	}

	/**
	 * Download the report. type - pdf, html, csv, xml Example:
	 * downloadReport(driver, "pdf", "C:\\test\\report");
	 * 
	 */
	public static void download(String url, String fileName) throws IOException {
		FileUtils.copyURLToFile(new URL(url), new File(fileName));
	}

	/**
	 * Uploads content to the media repository. Example:
	 * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com",
	 * "123456", content, "PRIVATE:apps/ApiDemos.apk");
	 */
	public void uploadMedia(String host, String user, String password, File file, String repositoryKey)
			throws UnsupportedEncodingException, MalformedURLException, IOException {
		byte[] content = readFile(file);
		if (content != null) {
			String encodedUser = URLEncoder.encode(user, "UTF-8");
			String encodedPassword = URLEncoder.encode(password, "UTF-8");
			String urlStr = HTTPS + host + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&user="
					+ encodedUser + "&password=" + encodedPassword;
			URL url = new URL(urlStr);

			sendRequest(content, url);
		}
	}

	public boolean repoFileExists(String host, String user, String password, String repoKey, String fileName) throws Exception {
		try {
			if (sendRequest("https://" + host + "/services/repositories/media/"+repoKey+"?operation=list&user="
					+ user + "&password=" + password).contains(fileName)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			throw ex;
		}

	}

	private void sendRequest(byte[] content, URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/octet-stream");
		connection.connect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		outStream.write(content);
		outStream.writeTo(connection.getOutputStream());
		outStream.close();
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
	}
}
