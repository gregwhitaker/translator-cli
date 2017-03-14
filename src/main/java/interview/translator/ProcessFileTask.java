package interview.translator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ProcessFileTask implements Callable<List<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFileTask.class);

    private final Path inputPath;
    private final Path outputPath;
    private final ObjectMapper mapper = new ObjectMapper();

    public ProcessFileTask(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    @Override
    public List<String> call() throws Exception {
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

                        String translatedValue = getTranslatedValueFromResponse(httpEntity.getContent());

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

    private String getTranslatedValueFromResponse(InputStream rawValue) throws Exception {
        JsonNode json = mapper.readTree(rawValue);
        boolean test = true;
        return "test";
    }

}
