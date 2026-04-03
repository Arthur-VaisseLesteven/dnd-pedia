package dndpedia.site;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dndpedia.site.model.BaseClass;
import dndpedia.site.model.Name;
import dndpedia.site.model.Race;
import dndpedia.site.model.Sourcebook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SourcebookLibrary {
    private static final Logger LOGGER = LogManager.getLogger(SourcebookLibrary.class);
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
            String rawTitle = sourcebookDirectory.title();
            sourcebook.setTitle(objectMapper.readValue(rawTitle, Name.class), rawTitle);
            LOGGER.info("processing {}", sourcebook.title().en());

            for (String raceContent : sourcebookDirectory.races()) {
                LOGGER.debug("reading {}", raceContent);
                Name name = objectMapper.readValue(raceContent, Name.class);
                sourcebook.addRace(name, raceContent);
                LOGGER.info("Read race {}", name.name().en());
            }

            for (String baseClassContent : sourcebookDirectory.baseclasses()) {
                LOGGER.debug("reading {}", baseClassContent);
                Name name = objectMapper.readValue(baseClassContent, Name.class);
                sourcebook.addBaseClass(name, baseClassContent);
                LOGGER.info("read base class {}", name.name().en());
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

    public Collection<BaseClass> base_classes() {
        List<BaseClass> result = new ArrayList<>();

        this.sourcebooks.stream().map(Sourcebook::baseClasses).forEach(result::addAll);

        return result;
    }
}
