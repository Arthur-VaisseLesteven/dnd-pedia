package dndpedia.site;

import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record SourcebookDirectory (File directory) {

    public String title() {
        return read(getFile("meta.json").orElseThrow());
    }

    public List<String> races() {
        List<String> races = new ArrayList<>();

        getFile("races").map(File::listFiles).ifPresent(files -> {
            for (File raceFile : files) {
                races.add(read(raceFile));
            }
        });

        return races;
    }

    private Optional<File> getFile(String filename) {
        File[] files = directory.listFiles(file -> file.getName().equals(filename));

        if (files == null || files.length == 0) return Optional.empty();
        if (files.length > 1) {
            LogManager.getLogger(SourcebookDirectory.class).error("While looking for the single {} file of {}, found {}", filename, this, Arrays.toString(files));
            throw new IllegalStateException( "Did not found a single file " + filename);
        }

        return Optional.of(files[0]);
    }

    private static String read(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (IOException ioException) {
            throw new IllegalStateException("Encountered an error while accessing file system. This is not supposed ot happen", ioException);
        }
    }

}
