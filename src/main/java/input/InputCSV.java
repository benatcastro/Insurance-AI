package input;

import javax.json.JsonObject;
import java.io.File;

public class InputCSV extends InputFile{
    protected InputCSV(ProcessRules rules, File file) {
        super(rules, file);
    }

    protected InputCSV(ProcessRules rules, String filename) {
        super(rules, filename);
    }

    @Override
    JsonObject toJson() {
        return null;
    }
}
