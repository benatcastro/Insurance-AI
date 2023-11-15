package aiintegration.api;


import javax.json.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;


/**
 * Base Class for connecting to OpenAI and retrieving the proccessed data
 */
public class APIClient {
    private final JsonArray data;
    private final URI endpoint;
    private final String prompt;

    public APIClient(JsonArray data) {

        Path promptFilePath = Path.of("classes/prompt.txt");

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

//        System.out.println(this.prompt);

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

    public String formatResponseContent(String content) {

        content = content.replace("\\n", "\n");
        content = content.replace("\\", "");
//        content = content.replaceAll("\\s", "");
//        StringBuilder stringBuilder = new StringBuilder(content);

        content = content.substring(content.indexOf("{") + 1, content.lastIndexOf('}') - 1);
        content = content.substring(content.indexOf('['));
//        for (int i = 0; i < stringBuilder.length(); i++)  {
//            if (stringBuilder.charAt(i) == '\\' && stringBuilder.charAt(i + 1) == 'n') {
//                stringBuilder.deleteCharAt(i);
//                stringBuilder.deleteCharAt(i + 1);
//
//                i--;
//            }
//
//        }
//        JsonReader jsonReader = Json.createReader(new StringReader(content));
//        JsonObject dataJSON = jsonReader.readObject();

        return content;
    }
    public String extractResponseContent(String body) {

        System.out.println("Extracting content...");

        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject bodyJSON = jsonReader.readObject();
        JsonArray choicesArray = bodyJSON.getJsonArray("choices");
        JsonObject firstChoice = choicesArray.getJsonObject(0);
        System.out.println("First Choice:" + firstChoice.toString());
        JsonObject messageObject = firstChoice.getJsonObject("message");
        JsonValue contentValue = messageObject.get("content");

        return contentValue.toString();

    }
    public String makeApiRequest() throws IOException {

        String apiKey = "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";
        HttpClient client = HttpClient.newHttpClient();

        // Adding neccesary http headers for the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(this.endpoint)
                .header("Authorization", "Bearer " + apiKey)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(this.requestBodyBuilder()))
                .build();

        // Executing request
        try {
            System.out.println("Executing request");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response recieved status: " + response.statusCode());
            String body = response.body();
            return body;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
