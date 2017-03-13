package interview.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

class StreamingFileTranslator implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamingFileTranslator.class);

    private final File inputFile;
    private final File outputFile;

    public StreamingFileTranslator(final File inputFile, final File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {

    }

}
