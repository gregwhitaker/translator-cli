package interview.translator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import interview.translator.validation.FileExistsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Parameter(required = true,
               description = "files to translate",
               validateWith = FileExistsValidator.class)
    List<String> inputFiles;

    public static void main(String... args) {
        Main main = new Main();
        new JCommander(main, args);
        main.run();
    }

    public void run() {
        LOGGER.info("Starting to translate '" + inputFiles.size() + "' files...");
        runStreamingProcess(inputFiles);
        runBatchProcess(inputFiles);
        LOGGER.info("Translation Complete");
    }

    private void runStreamingProcess(List<String> inputFiles) {
        LOGGER.info("Starting streaming processing of files...");
    }

    private void runBatchProcess(List<String> inputFiles) {
        LOGGER.info("Starting batch processing of files...");
    }

}
