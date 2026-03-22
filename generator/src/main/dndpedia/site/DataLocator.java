package dndpedia.site;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class DataLocator {
    private static final Logger LOGGER = LogManager.getLogger(DataLocator.class);

    private final Path sourcebooksDirectory;

    public DataLocator(Path sourcebooksDirectory) {
        this.sourcebooksDirectory = sourcebooksDirectory;
    }

    public Collection<SourcebookDirectory> data() {
        Collection<SourcebookDirectory> result;

        File[] dndbooksDirectories = sourcebooksDirectory.toFile().listFiles();
        if (dndbooksDirectories != null) {
            result = Stream.of(dndbooksDirectories).map(SourcebookDirectory::new).toList();
        } else {
            result = List.of();
        }

        LOGGER.info("found the following sourcebooks locations : {}", result);
        return result;
    }
}
