package interview.translator

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Paths


class ProcessFileTaskTest extends Specification {

    @Rule
    TemporaryFolder tempDir

    def "translatesValueToSwedish"() {
        setup:
        def inputFile = tempDir.newFile()
        inputFile << "test"

        def inputFilePath = Paths.get(inputFile.absolutePath)
        def outputFile = tempDir.newFile()
        def outputFilePath = Paths.get(outputFile.absolutePath)

        ProcessFileTask task = new ProcessFileTask(inputFilePath, outputFilePath)

        when:
        def result = task.call()

        then:
        result.size() == 1
        result.get(0) == "testa"
    }

}
