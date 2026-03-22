package dndpedia.site;

import dndpedia.site.model.Race;
import dndpedia.site.model.Sourcebook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public class SiteGenerator {
    private static final Logger logger = LogManager.getLogger(SiteGenerator.class);

    public static void main(String... commandLineParameters) {
        String projectRoot = Objects.requireNonNull(System.getenv("PROJECT_ROOT"), "Missing environment variable : PROJECT_ROOT");

        new SiteGenerator(
                new DataLocator(Path.of(projectRoot))
        ).generateSite();
    }

    private final DataLocator dataLocator;

    public SiteGenerator(DataLocator dataLocator) {
        this.dataLocator = dataLocator;
    }

    private void generateSite() {
        Collection<SourcebookDirectory> data = dataLocator.data();
        SourcebookLibrary library = new SourcebookLibrary(data);

        for (Race race : library.races()) {
            buildRacePage(race);
        }
        buildRaceIndex(library.races());

        for (Sourcebook sourcebook : library.sourcebooks()) {
            buildSourcebookPage(sourcebook);
        }
        buildSourcebookIndex(library.sourcebooks());
    }

    private void buildRacePage(Race race) {
        logger.warn("cannot paginate {}, race page not yet implemented", race.name().en());
    }

    private void buildRaceIndex(Collection<Race> races) {
        logger.warn("cannot index races, race index not yet implemented");
    }

    private void buildSourcebookPage(Sourcebook sourcebook) {
        logger.warn("cannot paginate {}, sourcebook page not yet implemented", sourcebook.title().en());
    }

    private void buildSourcebookIndex(Collection<Sourcebook> sourcebooks) {
        logger.warn("cannot index sourcebooks, sourcebooks indexes not yet implemented");
    }


}

