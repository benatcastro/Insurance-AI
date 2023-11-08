package aiintegration.api;

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

    private void requestBodyBuilder() {

        final String model = "gpt-3.5-turbo";
        final String prompt = "say test";

        JsonArrayBuilder    messageBuilder = Json.createArrayBuilder();
        JsonObjectBuilder   bodyBuilder = Json.createObjectBuilder();

        JsonObject userMessage = Json.createObjectBuilder()
                .add("role","user")
                .add("content", prompt)
                .build();
        messageBuilder.add(userMessage);

        bodyBuilder.add("model", model)
                .add("messages", messageBuilder.build());

        System.out.print("Mybody ->");
        System.out.println(bodyBuilder.build().toString());
//        return jsonArrayBuilder.build();


    }

    public void makeApiRequest() {

        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            this.requestBodyBuilder();
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + "say test" + "\"}]}";

            System.out.println("Body->" + body);
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            System.out.println(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
