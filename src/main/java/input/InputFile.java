package input;


import javax.json.JsonObject;
import java.io.File;

public abstract class InputFile extends Input {

    final protected File dataFile;

    // The class can be constructed from a file string and a Rules object
    protected InputFile(ProcessRules rules, File file) {
        super(rules);
        this.dataFile = file;
    }


    protected InputFile(ProcessRules rules, String filename) {
        super(rules);
        this.dataFile = new File(filename);
    }

}
