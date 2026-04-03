package dndpedia.site;

import dndpedia.site.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class SiteFiles {
    private static final Logger LOGGER = LogManager.getLogger(SiteFiles.class);
    private final Path indexPageTemplate;
    private final Path racePageTemplate;
    private final Path baseClassesPageTemplate;
    private final Path siteDirectory;

    public SiteFiles(Path siteDirectory, Path templateDirectory) {
        this.indexPageTemplate = templateDirectory.resolve("./indexPage.html.template");
        this.racePageTemplate = templateDirectory.resolve("./racePageTemplate.html.template");
        this.baseClassesPageTemplate = templateDirectory.resolve("./classPageTemplate.html.template");
        this.siteDirectory = siteDirectory;
    }

    public void createIndex(String title, Path path, Index index) {
        if (path.isAbsolute()) throw new IllegalStateException("expects a relative path for index directory, but got : " + path);

        try {
            String content = Files.readString(indexPageTemplate)
                .replace("${title}", title)
                .replace("${columns}", serializedColumns(index))
                .replace("${content}", serializedContent(index));

            Files.writeString(siteDirectory.resolve(path + "/list.html"), content);
            LOGGER.info("Indexed {}", title);
        } catch (IOException e) {
            throw new IllegalStateException("Fails to copy ", e);
        }
    }

    private CharSequence serializedColumns(Index index) {
        //column-1="Race" column-2="Source"
        StringBuilder columns = new StringBuilder();

        for (int i = 0; i < index.columns().size(); i++ ) {
            columns.append("column-%s=\"%s\" ".formatted(i+1, index.columns().get(i).fr()));
        }

        return columns.toString();
    }

    private CharSequence serializedContent(Index index) {
        StringBuilder result = new StringBuilder();
        for (IndexedElement row : index.data()) {
            StringBuilder resutRow = new StringBuilder();
            resutRow.append("\ttheContentTable.addContent({href: './" + row.pageName()+".html'");
            for (LocalizedString column : index.columns()) {
                resutRow.append(", '%s': '%s'".formatted(column.fr(), row.columns().get(column.fr()).fr().replace("'", "\\'")));
            }
            resutRow.append("});\n");
            result.append(resutRow);
        }

        return result.toString();
    }

    public void create(Race race) {
        Map<String, String> placeholders = Map.of(
                "${title}", race.name().fr(),
                "${source}", race.sourcebook().rawTitle(),
                "${content}", race.jsonContent()
        );
        addToSite(racePageTemplate, placeholders, "./races/" + race.name().fr() + ".html");
    }

    public void create(BaseClass baseClass) {
        Map<String, String> placeholders = Map.of(
                "${title}", baseClass.name().fr(),
                "${source}", baseClass.sourcebook().rawTitle(),
                "${content}", baseClass.jsonContent()
        );
        addToSite(baseClassesPageTemplate, placeholders, "./classes/" + baseClass.name().fr() + ".html");
    }

    private void addToSite(Path template, Map<String, String> placeholders, String filename) {
        try {
            String content = Files.readString(template);

            for (String placeholder : placeholders.keySet()) {
                content = content.replace(placeholder, placeholders.get(placeholder));
            }

            Files.writeString(siteDirectory.resolve(filename), content);
            LOGGER.info("Generated page {}", filename);
        } catch (IOException e) {
            throw new RuntimeException("encountered an exception while generating " + filename);
        }
    }
}
