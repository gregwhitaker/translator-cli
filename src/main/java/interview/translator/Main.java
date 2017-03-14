package interview.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main entry-point of the translator-cli application.
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private static final String OUTPUT_FILE_NAME = "AsYouGo.txt";
    private static final String BATCH_FILE_NAME = "Batched.txt";

    /**
     * Main entry-point of the application.
     *
     * @param args filenames of files to translate
     * @throws Exception
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
    }

    /**
     * Runs the translation process.
     *
     * @param args filenames of files to translate
     * @throws Exception
     */
    public void run(String... args) throws Exception {
        LOGGER.info("Translating '" + args.length + "' files...");

        Path outputFilePath = Paths.get(OUTPUT_FILE_NAME);
        Path batchFilePath = Paths.get(BATCH_FILE_NAME);

        // Cleaning up output files from previous run
        if (Files.exists(outputFilePath)) {
            LOGGER.info("Removing '" + outputFilePath + "' from previous translation run");
            Files.delete(outputFilePath);
        }

        if (Files.exists(batchFilePath)) {
            LOGGER.info("Removing '" + batchFilePath + "' from previous translation run");
            Files.delete(batchFilePath);
        }

        Files.createFile(outputFilePath);
        Files.createFile(batchFilePath);

        try {
            List<Callable<List<String>>> tasks = new ArrayList<>();

            for (String arg : args) {
                Path inputFilePath = Paths.get(arg);

                if (!Files.exists(inputFilePath)) {
                    throw new FileNotFoundException("Input file '" + inputFilePath + "' does not exist!'");
                } else {
                    tasks.add(new ProcessFileTask(inputFilePath, outputFilePath));
                }
            }

            Set<List<String>> results = new HashSet<>();
            for (Future<List<String>> f : EXECUTOR.invokeAll(tasks)) {
                results.add(f.get());
            }

            EXECUTOR.submit(new ProcessBatchFileTask(results, batchFilePath)).get();

            LOGGER.info("Translations Completed!");
        } finally {
            EXECUTOR.shutdown();
        }
    }

}
