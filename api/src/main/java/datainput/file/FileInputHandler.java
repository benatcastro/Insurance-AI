package datainput.file;

import datainput.Input;
import datainput.ProcessRules;

import java.io.File;
import java.io.IOException;

public abstract class FileInputHandler extends Input {

    final protected File dataFile;

    // The class can be constructed from a file string and a Rules object
    protected FileInputHandler(ProcessRules rules, File file) {
        super(rules);
        this.dataFile = file;
    }


    protected FileInputHandler(ProcessRules rules, String filename) {
        super(rules);
        this.dataFile = new File(filename);
    }
}
