package aiintegration.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.json.*;
import java.io.*;
import java.net.*;


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

    private String requestBodyBuilder() {

        final String model = "gpt-3.5-turbo-1106";
        final String prompt = "say test";

        JsonArrayBuilder    messageBuilder = Json.createArrayBuilder();
        JsonObjectBuilder   bodyBuilder = Json.createObjectBuilder();

        JsonObject systemMessage = Json.createObjectBuilder()
                .add("role", "system")
                .add("content", "you are an insurance expert with the task to analyze this data and provide a comprehensive risk evaluation on a scale of 0 to 100. The output should include a detailed JSON report outlining the factors contributing to the risk score, offering a clear and concise summary of the identified risks")
                .build();

        JsonObject userMessage = Json.createObjectBuilder()
                .add("role","user")
                .add("content", this.data.toString())
                .build();

        JsonObject formatType = Json.createObjectBuilder()
                        .add("type", "json_object")
                        .build();

        messageBuilder.add(systemMessage)
                        .add(userMessage);

        bodyBuilder.add("model", model)
                .add("response_format", formatType)
                .add("messages", messageBuilder.build());

        return bodyBuilder.build().toString();


    }

    public void makeApiRequest() throws IOException {

        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";

        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // Adding neccesary http headers for the request
        httpPost.setHeader("Authorization", "Bearer " + apiKey);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");

        // Adding the prompt information to the request as the body
        StringEntity body_entity;
        String body_data = this.requestBodyBuilder();
        System.out.println("Body->" + body_data);
        try {
            body_entity = new StringEntity(body_data);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        httpPost.setEntity(body_entity);

        // Executing request
        try {
            HttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Response body- " + responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
