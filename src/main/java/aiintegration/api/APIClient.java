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
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Base Class for connecting to OpenAI and retrieving the proccessed data
 */
public class APIClient {
    private final JsonArray data;
    private final URI endpoint;
    private final String prompt;

    public APIClient(JsonArray data) {

        Path promptFilePath = Path.of("src/main/resources/prompt.txt");

        this.data = data;
        try {
            this.endpoint = new URI("https://api.openai.com/v1/chat/completions");
        } catch (URISyntaxException e) {
            System.out.println("The URL for API client is not valid");
            throw new RuntimeException(e);
        }

        try {
            this.prompt = Files.readString(promptFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String requestBodyBuilder() {

        final String model = "gpt-3.5-turbo-1106";
        final String prompt = "say test";

        System.out.println(this.prompt);

        JsonArrayBuilder    messageBuilder = Json.createArrayBuilder();
        JsonObjectBuilder   bodyBuilder = Json.createObjectBuilder();

        JsonObject systemMessage = Json.createObjectBuilder()
                .add("role", "system")
                .add("content", this.prompt)
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

    public String makeApiRequest() throws IOException {

        String apiKey = "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";

        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(this.endpoint);

        // Adding neccesary http headers for the request
        httpPost.setHeader("Authorization", "Bearer " + apiKey);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");

        // Adding the prompt information to the request as the body
        StringEntity body_entity;
        String body_data = this.requestBodyBuilder();
//        System.out.println("Body->" + body_data);
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

            responseBody.replaceAll("\n", " ");
//            System.out.println("Response body- " + responseBody);
            return responseBody;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
