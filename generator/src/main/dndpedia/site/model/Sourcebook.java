package dndpedia.site.model;

import java.util.ArrayList;
import java.util.List;

public class Sourcebook{
    private LocalizedString title;
    private String rawTitle;
    private final List<Race> races = new ArrayList<>();
    private List<BaseClass> baseclasses = new ArrayList<>();

    public void setTitle(Name title, String rawTitle) {
        this.title = title.name();
        this.rawTitle = rawTitle;
    }

    public void addRace(Name name, String raceContent) {
        this.races.add(new Race(this, name.name() ,raceContent));

    }
    public void addBaseClass(Name name, String baseClassContent) {
        this.baseclasses.add(new BaseClass(this, name.name(), baseClassContent));
    }

    public LocalizedString title() { return title; }
    public String rawTitle() { return rawTitle; }

    public List<Race> races() { return races; }

    public List<BaseClass> baseClasses() {
        return baseclasses;
    }
}
