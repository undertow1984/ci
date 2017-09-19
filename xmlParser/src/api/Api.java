package api;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class Api {
	public static String SECURITY_TOKEN = "MY_CONTINUOUS_QUALITY_LAB_SECURITY_TOKEN";
	public static String CQL_NAME = "demo";
	
	public static JSONObject retrieveTestExecutions() throws URISyntaxException, IOException {
		return retrieveTestExecutions(Long.toString(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5)), Long.toString(System.currentTimeMillis()));
	}
    public static JSONObject retrieveTestExecutions(String start, String end) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(getServerURL() + "/export/api/v1/test-executions");

        // Optional: Filter by range. In this example: retrieve test executions of the past month (result may contain tests of multiple driver executions)
        uriBuilder.addParameter("startExecutionTime[0]", start);
        uriBuilder.addParameter("endExecutionTime[0]", end);

        // Optional: Filter by a specific driver execution ID that you can obtain at script execution
        // uriBuilder.addParameter("externalId[0]", "SOME_DRIVER_EXECUTION_ID");

        HttpGet getExecutions = new HttpGet(uriBuilder.build());
        addDefaultRequestHeaders(getExecutions);
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpResponse getExecutionsResponse = httpClient.execute(getExecutions);
        JSONObject executions = null;
        try (InputStreamReader inputStreamReader = new InputStreamReader(getExecutionsResponse.getEntity().getContent())) {
            String response = IOUtils.toString(inputStreamReader);
            try {
                executions = new JSONObject(response);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Unable to parse response: " + response);
            }
            System.out.println("\nList of test executions response:\n" + executions);
        }
        catch(Exception e)
        {
        	System.out.println(e.getMessage());
        	System.out.println(e.getMessage());
        }
        return executions;
    }
    private static void addDefaultRequestHeaders(HttpRequestBase request) {
        request.addHeader("PERFECTO_AUTHORIZATION", SECURITY_TOKEN);
    }
    public static String getServerURL() {
    	return  "https://" + CQL_NAME + ".reporting.perfectomobile.com";
    }
}
