package core;

import aiintegration.api.APIClient;
import datainput.Input;
import datainput.file.csv.CSVInputHandler;
import datainput.ProcessRules;
import java.io.IOException;
import static spark.Spark.*;


public class Core {
    private static void enableCORS() {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "http://localhost:5173");
            response.type("application/json");
        });
    }

    /**
     * This function handles all the program logic for reading the dataset and sending a response to the final client.
     *
     * @param file dataset that will be used
     * @param entries number of entries that will be read
     * @return the proccesed and formatted data recieved from OpenAi api.
     */
    public static String getRequestHandler(final String file, final int entries) {

        ProcessRules processRules = new ProcessRules();
       // Input class that will read and parse the file
        Input inputCSV = null;

        // Initialization of process rules with the query params we recieved
        processRules.setBatchSize(entries);
        processRules.ignoreKey("charges");

        // Input processing
        try {
            System.out.printf("Handling request (file: %s, entries: %d)\n", file, entries);
            inputCSV = new CSVInputHandler(processRules, "classes/inputs/" + file);
            inputCSV.processData();
        } catch (IOException e) {
            System.out.println("cannot open file");
            throw new RuntimeException();
        }

        // OpenAi api call
        APIClient apiClient = new APIClient(inputCSV.getDataAsJson());

        String apiResponse =  apiClient.makeApiRequest();
        String responseContent = apiClient.extractResponseContent(apiResponse);
        return apiClient.formatResponseContent(responseContent);
    }
    public static void main(String[] args) {
        System.out.println("Listening on: http://localhost:4567");
        get("/", (req, res) -> {
            // Saving query params into variables
            final String file = req.queryParams("file");
            final int entries = Integer.parseInt(req.queryParams("entries"));

            // Handling the request
            return Core.getRequestHandler(file, entries);
        });
        System.out.println("Enabling CORS...");
        Core.enableCORS();
    }
}
