package dndpedia.site.model;

import java.util.Map;

public record IndexedElement(
        String pageName,
        Map<String, LocalizedString> columns
) {
}
