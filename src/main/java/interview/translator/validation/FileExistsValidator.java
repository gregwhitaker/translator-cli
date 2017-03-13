package interview.translator.validation;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Paths;

/**
 * Validates if the supplied command line parameter is a
 * valid {@link java.io.File} that exists on the filesystem.
 */
public class FileExistsValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!Paths.get(value).toFile().exists()) {
            throw new ParameterException("Input file: '" + value + "' does not exist!");
        }
    }

}
