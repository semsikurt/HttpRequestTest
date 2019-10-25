import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NetworkService {

    private final String HEADER_CONTENT_TYPE = "Content-Type";


    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGetWithParameter() throws Exception {

        URIBuilder builder = new URIBuilder("http://ergast.com/api/f1/2005/last.json");

        builder.setParameter(NetworkConstants.Parameters.SERIES, "f1");
        builder.setParameter(NetworkConstants.Parameters.SEASON, "2018");
        builder.setParameter(NetworkConstants.Parameters.CONSTRUCTOR_ID, "adams");
        builder.setParameter(NetworkConstants.Parameters.DRIVER_ID, "ahrens");
        builder.setParameter(NetworkConstants.Parameters.FASTEST, "8");

        URI requestUri = builder.build();

        HttpGet request = new HttpGet(requestUri);
        request.addHeader(HEADER_CONTENT_TYPE, "application/json");

        System.out.println("\n~Sending Request to " + requestUri.getHost() +
                           "\n~Endpoint: " + requestUri.getPath() +
                           "\n~Query String: " + requestUri.getQuery() +
                           "\n~Request Header: " + Arrays.toString(request.getAllHeaders()) +
                           "\n~Waiting for response...");

        Thread.sleep(2000);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();

            System.out.println("\n-----Response has come-----\n" +
                               "\n~Response status: " + response.getStatusLine().toString() +
                               "\n~Response Header: " + headers);

            String result = EntityUtils.toString(entity);
            System.out.println("~Response: " + result);

        }

    }

    private void sendGetWithoutParameter() throws Exception {

        HttpGet request = new HttpGet("http://ergast.com/api/f1/2018/drivers/ahrens/constructors/adams/fastest/8/results/8/races.json");
        request.addHeader(HEADER_CONTENT_TYPE, "application/json");

        System.out.println("\n~Sending Request to " + request.getURI());
        Thread.sleep(2000);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();

            System.out.println("\n-----Response has come-----\n" +
                               "\n~Response status: " + response.getStatusLine().toString());

            String result = EntityUtils.toString(entity);
            System.out.println("~Response: " + result);
        }

    }

    private void sendPost() throws Exception {

        HttpPost post = new HttpPost("https://reqres.in/api/register");


        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("email", "eve.holt@reqres.in"));
        urlParameters.add(new BasicNameValuePair("password", "pistol"));


        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters);
        post.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));

        }

    }


    public static void main(String[] args) throws Exception {

        NetworkService service = new NetworkService();

        try {
            System.out.println("\n\n[---------------||---------------]\n\n");

            System.out.println("*Testing 1 - Send Http GET request with parameter");
            service.sendGetWithParameter();

            System.out.println("\n\n[---------------||---------------]\n\n");

            Thread.sleep(1000);

            System.out.println("*Testing 2 - Send Http GET request without parameter");
            service.sendGetWithoutParameter();

            System.out.println("\n\n[---------------||---------------]\n\n");

            Thread.sleep(1000);

            System.out.println("*Testing 3 - Send Http POST request");
            service.sendPost();

            System.out.println("\n\n[---------------||---------------]\n\n");
        } finally {
            service.close();
        }
    }


}
