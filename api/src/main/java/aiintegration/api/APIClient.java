package aiintegration.api;

import javax.json.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Class for connecting to OpenAI also processed and formats the data
 */
public class APIClient {
    private final JsonArray data;
    private final URI endpoint;
    private final String prompt;

    /**
     * Basic constructor
     * @param data data that will be sended to the OpenAI api to analyze
     */
    public APIClient(JsonArray data) {

        Path promptFilePath = Path.of("classes/prompt.txt");
        this.data = data;

        // Creating a URI object with our endpoint
        try {
            this.endpoint = new URI(System.getenv("API_ENDPOINT"));
        } catch (URISyntaxException e) {
            System.out.println("The URL for API client is not valid");
            throw new RuntimeException(e);
        }

        // Reading the prompt and storing it in a variable
        try {
            this.prompt = Files.readString(promptFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the body that will be sent to open AI
     * More info on OpenAI docs: https://platform.openai.com/docs/api-reference/making-requests
     * @return the body of the request in String format
     */
    private String bodyBuilder() {

        final String model = "gpt-3.5-turbo-1106";

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

    /**
     * Formats the plain data from the api to a JSON format
     * @param content the content extracted previously from the body
     * @return the content correctly formatted in JSON format.
     */
    public String formatResponseContent(String content) {

        // Replacing string literals into their ASCII value
        content = content.replace("\\n", "\n");
        content = content.replace("\\", "");

        // Deleting syntactically incorrect keys/curly-braces
        content = content.substring(content.indexOf("{") + 1, content.lastIndexOf('}') - 1);
        content = content.substring(content.indexOf('['));

        return content;
    }


    /**
     * Extracts the desired data in the plain body of the resposne
     * More info on OpenAI docs: https://platform.openai.com/docs/api-reference/completions/object
     * @param body the plain body recieved in the response from the OpenAI api as String
     * @return the analyzed data as unformatted JSON string
     */
    public String extractResponseContent(String body) {

        System.out.println("Extracting content...");

        // Each JSON objects goes deeper into the hierarchy of the body recieved
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject bodyJSON = jsonReader.readObject();
        JsonArray choicesArray = bodyJSON.getJsonArray("choices");
        JsonObject firstChoice = choicesArray.getJsonObject(0);
        JsonObject messageObject = firstChoice.getJsonObject("message");

        // This value is the desired analyzed data
        JsonValue contentValue = messageObject.get("content");

        return contentValue.toString();
    }


    /**
     * Handles all the logic to connect to Open AI api via an http client
     * @return the response body
     */
    public String makeApiRequest() {

        final String body = this.bodyBuilder();
        HttpClient client = HttpClient.newHttpClient();

        // Adding neccesary http headers for the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(this.endpoint)
                .header("Authorization", "Bearer " + System.getenv("API_KEY"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        // Executing request with some logging
        try {
            System.out.println("Executing request");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response recieved status: " + response.statusCode());
            return response.body();
        } catch (IOException e) {
            System.out.println("IOE sending request");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Interrupted sending request");
            throw new RuntimeException(e);
        }
    }
}
