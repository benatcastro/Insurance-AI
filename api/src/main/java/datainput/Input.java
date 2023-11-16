package datainput;

import javax.json.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Input abstract class for handling how data enters the workflow
 *
 * @version 0.1 Nov 2023
 * @author Benat Castro
 */
public abstract class Input {

    // Rules used to handle how the input is saved
    protected final ProcessRules rules;

    // This class will create the final JSON formatted data
    protected final JsonArrayBuilder jsonArrayBuilder;

    // Unique identifier for every data entry we handle
    private int entry_id;

    /**
     * Constructor for Input class
     * @param rules needed for construction so the user defines the handling details
     */
    protected Input(ProcessRules rules) {
        this.entry_id = 0;
        this.rules = rules;
        this.jsonArrayBuilder = Json.createArrayBuilder();
    }

    /**
     * Getter for the formatted data
     * @return JsonArray object filled with the data
     */
   public JsonArray getDataAsJson() {
        return this.jsonArrayBuilder.build();
   }

    /**
     * Gets an array of keys for an entry of the input
     * @return array of Strings with all the keys
     */
    abstract protected String[] extractKeys();

    /**
     * Gets an array of values for an entry of the input
     * @return array of Strings with all the keys
     */
    abstract protected String[] extractValues();

    /**
     * Generates a Json for an entry of the input
     * @return a JSON object with the entry
     */
    protected JsonObject generateEntry() {

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        final String[] keys = this.extractKeys();
        final String[] values = this.extractValues();
        final ArrayList<String> ignoredKeys = this.rules.getIgnoredKeys();

        jsonObjectBuilder.add("id", this.entry_id++);

        for (int i = 0; i < values.length; i++) {

            // Ignore adding the key to json if it is found on the processing rules
            if(ignoredKeys.contains(keys[i]))
                continue;

            jsonObjectBuilder.add(keys[i], values[i]);
        }
        return jsonObjectBuilder.build();
    }

    /**
     * Handler and initializer of the input getting logic
     * @throws IOException in case of read failure
     */
    public void processData() throws IOException {
        for (int i = 0; i < this.rules.getBatchSize(); i++) {
            JsonObject jsonEntry = this.generateEntry();
            this.jsonArrayBuilder.add(jsonEntry);
        }
    }
}
