package dndpedia.site;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dndpedia.site.model.Name;
import dndpedia.site.model.Race;
import dndpedia.site.model.Sourcebook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SourcebookLibrary {
    private static final Logger logger = LogManager.getLogger(SourcebookLibrary.class);
    private final Collection<Sourcebook> sourcebooks;

    public SourcebookLibrary(Collection<SourcebookDirectory> sourcebookDirectories) {
        sourcebooks = readAndParseSourcebooks(sourcebookDirectories);
    }

    private Collection<Sourcebook> readAndParseSourcebooks(Collection<SourcebookDirectory> sourcebookDirectories) {
        return sourcebookDirectories.stream().map(this::readAndParseSourcebook).toList();
    }

    private Sourcebook readAndParseSourcebook(SourcebookDirectory sourcebookDirectory) {
        ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Sourcebook sourcebook = new Sourcebook();

        try {
            sourcebook.setTitle(objectMapper.readValue(sourcebookDirectory.title(), Name.class));
            logger.info("processing {}", sourcebook.title().en());

            for (String raceContent : sourcebookDirectory.races()) {
                logger.debug("reading {}", raceContent);
                Name name = objectMapper.readValue(raceContent, Name.class);
                sourcebook.addRace(name, raceContent);
                logger.info("Read race {}", name.name().en());
            }
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        return sourcebook;
    }

    public Collection<Race> races() {
        List<Race> result = new ArrayList<>();

        this.sourcebooks.stream().map(Sourcebook::races).forEach(result::addAll);

        return result;
    }

    public Collection<Sourcebook> sourcebooks() {
        return sourcebooks;
    }
}
