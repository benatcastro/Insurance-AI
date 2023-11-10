package main;

import aiintegration.api.APIClient;
import datainput.Input;
import datainput.file.csv.CSVInputHandler;
import datainput.ProcessRules;
import org.apache.http.HttpResponse;

import javax.json.JsonArray;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ProcessRules processRules = new ProcessRules();
        processRules.setBatchSize(5);
        processRules.ignoreKey("charges");

        try {
            Input inputCSV = new CSVInputHandler(processRules, "src/main/resources/inputs/insurance.csv");
            inputCSV.processData();

            JsonArray data = inputCSV.getDataAsJson();
            System.out.println(data.toString());

            APIClient apiClient = new APIClient(data);
           HttpResponse apiResponse =  apiClient.makeApiRequest();




        } catch (IOException e) {
            System.out.println("cannot open file");
        }


    }
}
