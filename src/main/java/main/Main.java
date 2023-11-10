package main;

import aiintegration.api.APIClient;
import datainput.Input;
import datainput.file.csv.CSVInputHandler;
import datainput.ProcessRules;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.StringReader;

import static spark.Spark.*;


public class Main {

    public static String getRequestHandler() {
        ProcessRules processRules = new ProcessRules();
        processRules.setBatchSize(5);
        processRules.ignoreKey("charges");

        try {
            Input inputCSV = new CSVInputHandler(processRules, "src/main/resources/inputs/insurance.csv");
            inputCSV.processData();

            JsonArray data = inputCSV.getDataAsJson();
            System.out.println(data.toString());

            APIClient apiClient = new APIClient(data);
            String apiResponse =  apiClient.makeApiRequest();
//            System.out.println("Returning ->" + apiResponse);
//            JsonObject filtered = Json.createReader(new StringReader(apiResponse)).readObject();
//            String test = filtered.get("choices").toString();
//
//            System.out.println(test);
//
//            System.out.println(filtered.toString());
            return apiResponse;

        } catch (IOException e) {
            System.out.println("cannot open file");
        }
        return null;
    }
    public static void main(String[] args) {


        get("/hello", (req, res) -> {
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
