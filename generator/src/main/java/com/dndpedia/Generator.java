package com.dndpedia;

import com.dndpedia.model.Race;
import com.dndpedia.model.Section;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Generator {

    private static final List<String> LANGUAGES = List.of("en", "fr");

    private static final Map<String, String> LORE_HEADING = Map.of(
            "en", "Lore",
            "fr", "Description"
    );

    private static final Map<String, String> FEATURES_HEADING = Map.of(
            "en", "Racial Features",
            "fr", "Traits raciaux"
    );

    private final ObjectMapper mapper;
    private final Path projectRoot;

    public Generator(Path projectRoot) {
        this.projectRoot = projectRoot;
        this.mapper = new ObjectMapper();
        this.mapper.configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
    }

    public static void main(String[] args) throws IOException {
        Path root;
        if (args.length > 0) {
            root = Path.of(args[0]);
        } else {
            root = detectProjectRoot();
        }

        System.out.println("Project root: " + root);

        Generator gen = new Generator(root);
        gen.generateRaces();
    }

    // ── Race generation ────────────────────────────────────────────────

    private void generateRaces() throws IOException {
        Path dataDir = projectRoot.resolve("data/sourcebook/races");
        Path outDir = projectRoot.resolve("site/races");
        Files.createDirectories(outDir);

        if (!Files.isDirectory(dataDir)) {
            System.err.println("Data directory not found: " + dataDir);
            return;
        }

        // Collect all race entries: slug → Race
        Map<String, Race> races = new TreeMap<>();

        try (Stream<Path> files = Files.list(dataDir)) {
            files.filter(p -> p.toString().endsWith(".json")).forEach(jsonFile -> {
                String slug = jsonFile.getFileName().toString().replace(".json", "");
                try {
                    Race race = mapper.readValue(jsonFile.toFile(), Race.class);
                    races.put(slug, race);
                    System.out.println("  Loaded: " + slug + " → " + race.getName());
                } catch (IOException e) {
                    System.err.println("  ERROR reading " + jsonFile + ": " + e.getMessage());
                }
            });
        }

        // Generate one HTML per race
        for (var entry : races.entrySet()) {
            String slug = entry.getKey();
            Race race = entry.getValue();
            Path htmlFile = outDir.resolve(slug + ".html");
            String html = buildRacePage(slug, race);
            Files.writeString(htmlFile, html);
            System.out.println("  Generated: " + htmlFile);
        }

        // Generate races/index.html
        String indexHtml = buildRacesIndex(races);
        Files.writeString(outDir.resolve("index.html"), indexHtml);
        System.out.println("  Generated: " + outDir.resolve("index.html"));

        System.out.println("Done. " + races.size() + " race(s) generated.");
    }

    // ── Single race page ───────────────────────────────────────────────

    private String buildRacePage(String slug, Race race) {
        StringBuilder sb = new StringBuilder();

        sb.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%s — D&amp;D Pedia</title>
                    <link rel="stylesheet" href="../css/style.css">
                </head>
                <body>
                
                <page-header base-url=".."></page-header>
                
                <main class="main-content">
                <race-page name-en="%s" name-fr="%s" source-en="%s" source-fr="%s">
                """.formatted(
                esc(race.getName().getOrDefault("en", slug)),
                esc(race.getName().getOrDefault("en", slug)),
                esc(race.getName().getOrDefault("fr", slug)),
                esc(race.getSource().getOrDefault("en", "")),
                esc(race.getSource().getOrDefault("fr", ""))
        ));

        // Content blocks for each language
        for (String lang : LANGUAGES) {
            sb.append("  <div class=\"lang-content\" data-lang=\"").append(lang).append("\">\n");

            // ── Lore ──
            List<Section> lore = race.getLore() != null ? race.getLore().getOrDefault(lang, List.of()) : List.of();
            if (!lore.isEmpty()) {
                sb.append("    <section class=\"race-lore\">\n");
                sb.append("      <h2>").append(esc(LORE_HEADING.getOrDefault(lang, "Lore"))).append("</h2>\n");
                for (Section s : lore) {
                    sb.append("      <article>\n");
                    if (s.getTitle() != null && !s.getTitle().isBlank()) {
                        sb.append("        <h3>").append(esc(s.getTitle())).append("</h3>\n");
                    }
                    sb.append("        <p>").append(esc(s.getContent())).append("</p>\n");
                    sb.append("      </article>\n");
                }
                sb.append("    </section>\n");
            }

            // ── Racial features ──
            List<Section> features = race.getRacialFeatures() != null ? race.getRacialFeatures().getOrDefault(lang, List.of()) : List.of();
            if (!features.isEmpty()) {
                sb.append("    <section class=\"race-features\">\n");
                sb.append("      <h2>").append(esc(FEATURES_HEADING.getOrDefault(lang, "Features"))).append("</h2>\n");
                sb.append("      <ul>\n");
                for (Section s : features) {
                    sb.append("        <li>").append(esc(s.getContent())).append("</li>\n");
                }
                sb.append("      </ul>\n");
                sb.append("    </section>\n");
            }

            sb.append("  </div>\n");
        }

        sb.append("""
                </race-page>
                </main>
                
                <script src="../js/components/page-header.js"></script>
                <script src="../js/components/race-page.js"></script>
                </body>
                </html>
                """);

        return sb.toString();
    }

    // ── Races index page ───────────────────────────────────────────────

    private String buildRacesIndex(Map<String, Race> races) {
        StringBuilder sb = new StringBuilder();

        sb.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Races — D&amp;D Pedia</title>
                    <link rel="stylesheet" href="../css/style.css">
                </head>
                <body>
                
                <page-header base-url=".."></page-header>
                
                <main class="main-content">
                    <h1 class="section-title">Races</h1>
                    <div class="race-list">
                """);

        for (var entry : races.entrySet()) {
            String slug = entry.getKey();
            Race race = entry.getValue();
            String nameEn = esc(race.getName().getOrDefault("en", slug));
            String nameFr = esc(race.getName().getOrDefault("fr", slug));
            String source = esc(race.getSource().getOrDefault("en", ""));

            sb.append("""
                            <a href="%s.html" class="race-list__item">
                                <span class="race-list__name">%s</span>
                                <span class="race-list__name-alt">%s</span>
                                <span class="race-list__source">%s</span>
                            </a>
                    """.formatted(slug, nameEn, nameFr, source));
        }

        sb.append("""
                    </div>
                </main>
                
                <script src="../js/components/page-header.js"></script>
                </body>
                </html>
                """);

        return sb.toString();
    }

    // ── Utilities ──────────────────────────────────────────────────────

    /**
     * Walk up from the current working directory until we find a folder
     * that contains both {@code data/} and {@code site/}.
     */
    private static Path detectProjectRoot() {
        Path cwd = Path.of("").toAbsolutePath();
        for (Path candidate = cwd; candidate != null; candidate = candidate.getParent()) {
            if (Files.isDirectory(candidate.resolve("data")) && Files.isDirectory(candidate.resolve("site"))) {
                return candidate;
            }
        }
        // Fallback: assume cwd IS the root
        return cwd;
    }

    private static String esc(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}

