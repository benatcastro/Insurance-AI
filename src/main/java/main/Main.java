package main;

import datainput.Input;
import datainput.file.csv.CSVInputHandler;
import datainput.ProcessRules;
import javax.json.JsonArray;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ProcessRules processRules = new ProcessRules();
        processRules.batch_size = 5;

        try {
            Input inputCSV = new CSVInputHandler(processRules, "src/main/resources/inputs/insurance.csv");
            inputCSV.processData();
            JsonArray data = inputCSV.getDataAsJson();
            System.out.println(data.toString());
        } catch (IOException e) {
            System.out.println("cannot open file");
        }

    }
}
