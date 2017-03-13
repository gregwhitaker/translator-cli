package interview.translator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import interview.translator.validation.FileExistsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Main entry-point of the translator-cli application.
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String STREAMING_OUTPUTFILE_NAME = "AsYouGo.txt";
    private static final String BATCH_OUTPUTFILE_NAME = "Batched.txt";

    @Parameter(required = true,
               description = "files to translate",
               validateWith = FileExistsValidator.class)
    List<String> inputFiles;

    /**
     * Main entry-point of the application.
     * @param args a list of filenames to process
     */
    public static void main(String... args) {
        Main main = new Main();
        new JCommander(main, args);
        main.run();
    }

    /**
     * Runs the translation process.
     */
    public void run() {
        LOGGER.info("Starting to translate '" + inputFiles.size() + "' files...");
        doStreamingProcess(inputFiles);
        doBatchProcess(inputFiles);
        LOGGER.info("Translation Complete");
    }

    private void doStreamingProcess(List<String> inputFiles) {
        LOGGER.info("Starting streaming processing of files...");
    }

    private void doBatchProcess(List<String> inputFiles) {
        LOGGER.info("Starting batch processing of files...");
    }

}
