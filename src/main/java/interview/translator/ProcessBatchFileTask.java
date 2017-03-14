package interview.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Task that sorts and batches the translated results returned from {@link ProcessFileTask}s.
 */
public class ProcessBatchFileTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFileTask.class);

    private final Set<List<String>> translations;
    private final Path outputFilePath;

    public ProcessBatchFileTask(Set<List<String>> translations, Path outputFilePath) {
        this.translations = translations;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void run() {
        LOGGER.info("Batching translations in '" + outputFilePath + "'");

        List<String> currentTranslations = null;
        int currentIndex = 0;
        final String newline = System.getProperty("line.separator");

        try {
            try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath, StandardOpenOption.APPEND)) {
                while (true) {
                    currentTranslations = new ArrayList<>(translations.size());

                    for (List<String> values : translations) {
                        if (values.size() >= currentIndex + 1) {
                            currentTranslations.add(values.get(currentIndex));
                        }
                    }

                    // No more translations to process
                    if (currentTranslations.isEmpty()) {
                        break;
                    }

                    Collections.sort(currentTranslations);

                    currentTranslations.forEach(t -> {
                        try {
                            writer.write(t);
                            writer.write(newline);
                        } catch (IOException e) {
                            // Just raise this up and blow the whole file creation up if we have issues
                            // writing one of the lines.
                            throw new RuntimeException("Error while writing batch file.", e);
                        }
                    });

                    currentIndex++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while writing batch file.", e);
        }
    }

}
