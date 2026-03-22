package dndpedia.site.model;

import java.util.ArrayList;
import java.util.List;

public class Sourcebook{
    private LocalizedString title;
    private String rawTitle;
    private final List<Race> races = new ArrayList<>();

    public void setTitle(Name title, String rawTitle) {
        this.title = title.name();
        this.rawTitle = rawTitle;
    }

    public void addRace(Name name, String raceContent) {
        this.races.add(new Race(this, name.name() ,raceContent));

    }

    public LocalizedString title() { return title; }
    public String rawTitle() { return rawTitle; }
    public List<Race> races() { return races; }
}
