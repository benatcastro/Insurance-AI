package datainput.file.csv;

import datainput.ProcessRules;
import datainput.file.FileInputHandler;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.management.RuntimeErrorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVInputHandler extends FileInputHandler {

    private final BufferedReader bufferedReader;

    private final String[] headers;
    protected CSVInputHandler(ProcessRules rules, File file) throws IOException {
        super(rules, file);
        this.bufferedReader = Files.newBufferedReader(Path.of(this.dataFile.getPath()));
        this.headers = bufferedReader.readLine().split(",");
    }


    public CSVInputHandler(ProcessRules rules, String filename) throws IOException {
        super(rules, filename);
        this.bufferedReader = Files.newBufferedReader(Path.of(this.dataFile.getPath()));
        this.headers = this.getFileHeaders();

    }

    private String[] getFileHeaders() {
        try {
            final String raw_headers = this.bufferedReader.readLine();
            return raw_headers.split(",");
        }
        catch (IOException e) {
            throw new RuntimeErrorException(new Error(), "Failed to read header from csv");
        }
    }

    @Override
    protected String[] extractValues() {

        final String line;

        try {
            line = this.bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String[] values = line.split(",");
        return values;
    }

    @Override
    protected String[] extractKeys() {
        return this.headers;
    }
}
