package datainput;

import javax.json.*;
import java.io.IOException;

/**
 * Input abstract class for handling how data enters the workflow
 *
 * @version 0.1 Nov 2023
 * @author Benat Castro
 */
public abstract class Input {
    protected final ProcessRules rules;

    protected final JsonArrayBuilder jsonArrayBuilder;


    protected Input(ProcessRules rules) {
        this.rules = rules;
        this.jsonArrayBuilder = Json.createArrayBuilder();
    }

    // Method were the input data is processed into a Json data structure that the AI is capable of understanding
   public JsonArray getDataAsJson() {
        return this.jsonArrayBuilder.build();
   }

    /**
     * Reads data from the input and returns a Json object with only one entry
     * -> "age":"19","sex":"female","bmi":"27.9"...
     */
    abstract protected JsonObject generateEntry() throws IOException;

    public void processData() throws IOException {
        for (int i = 0; i < this.rules.batch_size; i++) {
            this.jsonArrayBuilder.add(this.generateEntry());
        }
    }


}
