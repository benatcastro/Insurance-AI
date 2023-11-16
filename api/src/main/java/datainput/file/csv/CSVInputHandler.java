package datainput.file.csv;

import datainput.Input;
import datainput.ProcessRules;
import javax.management.RuntimeErrorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handler for csv datasets
 *
 * @version 0.1 Nov 2023
 * @author Benat Castro
 */
public class CSVInputHandler extends Input {

    private final BufferedReader bufferedReader;

    private final String[] headers;

    /**
     *
     * @param rules rules that will be applied to the input
     * @param filename where the input will be read
     * @throws IOException In case the file cannot be open
     */
    public CSVInputHandler(ProcessRules rules, String filename) throws IOException {
        super(rules);
        this.bufferedReader = Files.newBufferedReader(Path.of(filename));
        this.headers = this.getFileHeaders();

    }

    /**
     * Gets the keys of the colummns from the file and formats it.
     *
     * @return an array with the keys for a CSV file
     */
    private String[] getFileHeaders() {
        try {
            final String raw_headers = this.bufferedReader.readLine();
            return raw_headers.split(",");
        }
        catch (IOException e) {
            throw new RuntimeErrorException(new Error(), "Failed to read header from csv");
        }
    }

    /**
     * Gets the values of one line of the dataset
     * @return a String for each colummn of the csv with its value
     */
    @Override
    protected String[] extractValues() {

        final String line;

        try {
            line = this.bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line.split(",");
    }

    /**
     * Returns the keys of the colummns
     * @return array of Strings with the keys for each colummn
     */
    @Override
    protected String[] extractKeys() {
        return this.headers;
    }
}
