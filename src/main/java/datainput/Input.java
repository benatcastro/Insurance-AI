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

    // Method were the input data is processed into a Json data structure that the AI is capable of understanding

    /**
     * Getter for the data as Json
     * @return Array of JSONs
     */
   public JsonArray getDataAsJson() {
        return this.jsonArrayBuilder.build();
   }

    /**
     * Returns the the keys for each value found in the document
     * Since this class doesn't handle specific type of formats, like CSV, XML...
     * and every format have their own way to store the keys the responsability
     * lies in the specific format handler
     * @return array of Strings with all the keys
     * @todo refactor to PairList
     */
    abstract protected String[] extractKeys();
    abstract protected String[] extractValues();
    /**
     * Gets data from the concrete class, applies rules and creates the entry with Json format
     * -> "age":"19","sex":"female","bmi":"27.9"...
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
    };

    public void processData() throws IOException {
        for (int i = 0; i < this.rules.getBatchSize(); i++) {
            JsonObject jsonEntry = this.generateEntry();
            this.jsonArrayBuilder.add(jsonEntry);
        }
    }


}
