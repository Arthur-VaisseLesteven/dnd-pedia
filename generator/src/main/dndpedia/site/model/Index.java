package dndpedia.site.model;

import java.util.List;

public record Index(
        List<LocalizedString> columns,
        List<IndexedElement> data
) {
}
