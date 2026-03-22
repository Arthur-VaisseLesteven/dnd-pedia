package dndpedia.site;

import dndpedia.site.model.Index;
import dndpedia.site.model.IndexedElement;
import dndpedia.site.model.LocalizedString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SiteFiles {
    private static final Logger LOGGER = LogManager.getLogger(SiteFiles.class);
    private final Path indexPageTemplate;
    private final Path siteDirectory;

    public SiteFiles(Path siteDirectory, Path templateDirectory) {
        indexPageTemplate = templateDirectory.resolve("./indexPage.html.template");
        this.siteDirectory = siteDirectory;
    }

    public void createIndex(Path path, Index index) {
        if (path.isAbsolute()) throw new IllegalStateException("expects a relative path for index directory, but got : " + path);

        try {
            String template = Files.readString(indexPageTemplate)
                .replace("${columns}", serializedColumns(index))
                .replace("${content}", serializedContent(index));

            Files.writeString(siteDirectory.resolve(path + "/list.html"), template);
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
        //theContentTable.addContent({href: './races/demi-elfes.html',    Race: 'Demi-Elfes',    Source: 'Le Manuel du Joueur'});
        for (IndexedElement row : index.data()) {
            StringBuilder resutRow = new StringBuilder();
            resutRow.append("\ttheContentTable.addContent({href: './" + row.pageName()+"'");
            for (LocalizedString column : index.columns()) {
                resutRow.append(", %s: '%s'".formatted(column.fr(), row.columns().get(column.fr()).fr().replace("'", "\\'")));
            }
            resutRow.append("});\n");
            result.append(resutRow);
        }

        return result.toString();
    }
}
