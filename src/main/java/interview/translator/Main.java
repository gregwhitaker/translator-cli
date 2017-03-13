package interview.translator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import interview.translator.validation.FileExistsValidator;

import java.util.List;
import java.util.function.Consumer;

public class Main {

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
        inputFiles.stream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
    }

}
