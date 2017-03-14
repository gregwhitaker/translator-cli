package interview.translator;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Task that sends strings from a file to the google translate api and returns the results as well as writes
 * them to a file.
 */
public class ProcessFileTask implements Callable<List<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFileTask.class);

    private final Path inputPath;
    private final Path outputPath;

    public ProcessFileTask(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    @Override
    public List<String> call() throws Exception {
        LOGGER.info("Translating file '" + inputPath + "'");

        List<String> translatedValues = new ArrayList<>();

        final String newline = System.getProperty("line.separator");

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath,StandardOpenOption.APPEND);
             BufferedReader reader = Files.newBufferedReader(inputPath, Charset.forName("UTF-8"));
             CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String line;
            while ((line = reader.readLine()) != null) {
                HttpGet httpGet = new HttpGet("https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=sv&dt=t&q=" + line);
                httpGet.setHeader("Accept", "application/json");

                HttpEntity httpEntity = null;
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        httpEntity = response.getEntity();

                        String translatedValue = getTranslatedValueFromResponse(EntityUtils.toString(httpEntity, Charset.forName("UTF-8")));

                        translatedValues.add(translatedValue);
                        writer.write(translatedValue);
                        writer.write(newline);
                    } else {
                        LOGGER.warn("Translation failed for: " + httpGet.getURI().toASCIIString());
                    }
                } finally {
                    if (httpEntity != null) {
                        EntityUtils.consume(httpEntity);
                    }
                }
            }
        }

        return translatedValues;
    }

    private String getTranslatedValueFromResponse(String rawValue) throws Exception {
        // HACK: The API is returning malformed JSON (ex: [[["förseelse","wrongdoing",,,2]],,"en"] ) so I had to do
        // this very gross thing to parse out the translated values :(
        if (rawValue != null && !rawValue.isEmpty()) {
            String val = rawValue.substring(0, rawValue.indexOf(","));
            val = val.replaceAll("\\[", "");
            val = val.replaceAll("\"", "");
            return val;
        }

        return "";
    }

}
