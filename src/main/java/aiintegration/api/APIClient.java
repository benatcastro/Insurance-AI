package aiintegration.api;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**
 * Base Class for connecting to OpenAI and retrieving the proccessed data
 */
public class APIClient {
    private final JsonArray data;
    private final URI endpoint;

    public APIClient(JsonArray data) {
        this.data = data;
        try {
            this.endpoint = new URI("https://api.openai.com/v1/chat/completions");
        } catch (URISyntaxException e) {
            System.out.println("The URL for API client is not valid");
            throw new RuntimeException(e);
        }
    }

    private JsonArray requestBodyBuilder() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonObject body = Json.createObjectBuilder()
                .add("model","gpt-3.5-turbo-1106")
                .add("prompt", "Say test")
                .build();

        System.out.println(body.toString());
        jsonArrayBuilder.add(body);
        return jsonArrayBuilder.build();


    }

    public void makeApiRequest() {

        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(this.endpoint);

        httpPost.addHeader("Authorization", "Bearer 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");
        StringEntity requestEntity = null;
        try {
            requestEntity = new StringEntity(this.requestBodyBuilder().toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        httpPost.setEntity(requestEntity);

        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println("****Response****");
            System.out.println(httpResponse.toString());
        } catch (Exception e) {
            System.out.println("error in http client execute");
            e.printStackTrace();
        }
    }

}
