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

    protected final ProcessRules rules;

    protected final JsonArrayBuilder jsonArrayBuilder;
    private int entry_id;

    protected Input(ProcessRules rules) {
        this.entry_id = 0;
        this.rules = rules;
        this.jsonArrayBuilder = Json.createArrayBuilder();
    }

    // Method were the input data is processed into a Json data structure that the AI is capable of understanding
   public JsonArray getDataAsJson() {
        return this.jsonArrayBuilder.build();
   }

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
