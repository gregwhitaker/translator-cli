package interview.translator

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Paths

class ProcessBatchFileTaskTest extends Specification {

    @Rule
    TemporaryFolder tempDir

    def "createSortedBatchFile"() {
        setup:
        Set<List<String>> translations = new HashSet<>()
        translations << ['a']
        translations << ['c']
        translations << ['b']

        def outputFile = tempDir.newFile()
        def outputFilePath = Paths.get(outputFile.absolutePath)

        ProcessBatchFileTask task = new ProcessBatchFileTask(translations, outputFilePath)

        when:
        task.run()

        then:
        outputFile.text == "a\nb\nc\n"

    }

}
