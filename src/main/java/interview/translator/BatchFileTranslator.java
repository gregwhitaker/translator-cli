package interview.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Translator that translates all values in the file before
 * returning the translated values.
 */
class BatchFileTranslator implements Callable<List<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchFileTranslator.class);

    private List<String> inputFiles;

    public BatchFileTranslator(List<String> inputFiles) {
        this.inputFiles = inputFiles;
    }

    @Override
    public List<String> call() throws Exception {
        return null;
    }

}
