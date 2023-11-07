package input;

import javax.json.JsonObject;

/**
 * Input abstract class for handling how data enters the workflow
 *
 * @version 0.1 Nov 2023
 * @author Benat Castro
 */
public abstract class Input {
    protected final ProcessRules rules;

    protected Input(ProcessRules rules) {
        this.rules = rules;
    }

    // Method were the input data is processed into a Json data structure that the AI is capable of understanding
   abstract JsonObject toJson();

}
