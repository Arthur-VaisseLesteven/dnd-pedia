package dndpedia.site;

import dndpedia.site.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class SiteGenerator {
    private static final Logger LOGGER = LogManager.getLogger(SiteGenerator.class);

    public static void main(String... commandLineParameters) {
        Path templateDirectory = Path.of(getEnv("TEMPLATE_DIRECTORY"));
        Path siteDirectory = Path.of(getEnv("SITE_DIRECTORY"));
        Path sourcesDirectory = Path.of(getEnv("SOURCES_DIRECTORY"));

        new SiteGenerator(
                new DataLocator(sourcesDirectory),
                new SiteFiles(siteDirectory, templateDirectory)
        ).generateSite();
    }

    private static String getEnv(String environmentVariableName) {
        return requireNonNull(System.getenv(environmentVariableName), "Missing environment variable : " + environmentVariableName);
    }

    private final DataLocator dataLocator;
    private final SiteFiles siteFiles;

    public SiteGenerator(DataLocator dataLocator, SiteFiles siteFiles) {
        this.dataLocator = dataLocator;
        this.siteFiles = siteFiles;
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
        LOGGER.warn("cannot paginate {}, race page not yet implemented", race.name().en());
    }

    private void buildRaceIndex(Collection<Race> races) {
        siteFiles.createIndex(
                Path.of("./races"),
                new Index(
                        List.of(
                                new LocalizedString("Race", "Race"),
                                new LocalizedString("Source", "Source")
                        ),
                        races.stream().map(race -> new IndexedElement(
                                race.name().fr(),
                                Map.of(
                                        "Race", race.name(),
                                        "Source", race.sourcebook().title()
                                )
                        )).toList()
                )
        );
    }

    private void buildSourcebookPage(Sourcebook sourcebook) {
        LOGGER.warn("cannot paginate {}, sourcebook page not yet implemented", sourcebook.title().en());
    }

    private void buildSourcebookIndex(Collection<Sourcebook> sourcebooks) {
        LOGGER.warn("cannot index sourcebooks, sourcebooks indexes not yet implemented");
    }


}

