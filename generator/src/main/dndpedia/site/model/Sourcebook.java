package dndpedia.site.model;

import java.util.ArrayList;
import java.util.List;

public class Sourcebook{
    private LocalizedString title;
    private final List<Race> races = new ArrayList<>();

    public void setTitle(Name title) { this.title = title.name(); }

    public void addRace(Name name, String raceContent) {
        this.races.add(new Race(this, name.name() ,raceContent));

    }

    public LocalizedString title() { return title; }
    public List<Race> races() { return races; }
}
