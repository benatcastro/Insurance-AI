package core;

import aiintegration.api.APIClient;
import datainput.Input;
import datainput.file.csv.CSVInputHandler;
import datainput.ProcessRules;
import javax.json.JsonArray;
import java.io.IOException;
import static spark.Spark.*;


public class Main {
    private static void enableCORS(final String origin) {

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
            response.header("Access-Control-Allow-Origin", origin);
//            response.header("Access-Control-Request-Method", methods);
//            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
    public static String getRequestHandler() {
        ProcessRules processRules = new ProcessRules();
        processRules.setBatchSize(5);
        processRules.ignoreKey("charges");
        Input inputCSV = null;
        JsonArray data;

        try {
            inputCSV = new CSVInputHandler(processRules, "classes/inputs/insurance.csv");
            inputCSV.processData();



        } catch (IOException e) {
            System.out.println("cannot open file");
        }

        try {
            data = inputCSV.getDataAsJson();
            System.out.println(data.toString());
            APIClient apiClient = new APIClient(data);
            String apiResponse =  apiClient.makeApiRequest();
            return apiResponse;

        }
        catch (IOException e) {
            System.out.println("API error");
        }
        return null;
    }
    public static void main(String[] args) {




        System.out.println("Listening on: http://localhost:4567");
        get("/", (req, res) -> {
            return Main.getRequestHandler();
        });
        System.out.println("Enabling CORS...");
        Main.enableCORS("http://localhost:5173");

//        ProcessRules processRules = new ProcessRules();
//        processRules.setBatchSize(5);
//        processRules.ignoreKey("charges");
//
//        try {
//            Input inputCSV = new CSVInputHandler(processRules, "src/main/resources/inputs/insurance.csv");
//            inputCSV.processData();
//
//            JsonArray data = inputCSV.getDataAsJson();
//            System.out.println(data.toString());
//
//            APIClient apiClient = new APIClient(data);
//            HttpResponse apiResponse =  apiClient.makeApiRequest();
//
//        } catch (IOException e) {
//            System.out.println("cannot open file");
//        }
    }
}
