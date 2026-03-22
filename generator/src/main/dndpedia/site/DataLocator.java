package dndpedia.site;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class DataLocator {
    private static final Logger logger = LogManager.getLogger(DataLocator.class);

    private final Path projectRoot;

    public DataLocator(Path projectRoot) {
        this.projectRoot = projectRoot;
    }

    public Collection<SourcebookDirectory> data() {
        try (Stream<Path> search = Files.find(projectRoot, 10, (path, attributes) -> path.toFile().getName().equals("sourcebooks"))){
            List<SourcebookDirectory> sourcebookDirectories = Stream.of(search.findFirst().orElseThrow().toFile().listFiles()).map(SourcebookDirectory::new).toList();
            logger.info("found the following sourcebooks locations : {}", sourcebookDirectories);
            return sourcebookDirectories;
        } catch (IOException ioException) {
            throw new IllegalArgumentException();
        }
    }
}
