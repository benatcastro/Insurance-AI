package datainput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Storage class for the rules that will take into account later into proccessing data.
 *
 * @version 0.1 Nov 2023
 * @author Benat Castro
 */
public class ProcessRules {


    private int batchSize;

    private ArrayList<String> ignoredKeys;

    public ProcessRules() {
        this.ignoredKeys = new ArrayList<String>();
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public ArrayList<String> getIgnoredKeys() {
        return this.ignoredKeys;
    }



    public void ignoreKey(String keyToIgnore) {
        if (!this.ignoredKeys.contains(keyToIgnore))
            this.ignoredKeys.add(keyToIgnore);
    };
}
