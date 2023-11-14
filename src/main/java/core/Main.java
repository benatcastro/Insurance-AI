package core;

import aiintegration.api.APIClient;
import datainput.Input;
import datainput.file.csv.CSVInputHandler;
import datainput.ProcessRules;
import javax.json.JsonArray;
import java.io.IOException;
import static spark.Spark.*;


public class Main {

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