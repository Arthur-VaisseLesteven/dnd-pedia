package com.dndpedia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Race {

    private Map<String, String> source;
    private Map<String, String> name;
    private Map<String, List<Section>> lore;

    @JsonProperty("racial-features")
    private Map<String, List<Section>> racialFeatures;

    public Map<String, String> getSource() {
        return source;
    }

    public void setSource(Map<String, String> source) {
        this.source = source;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<String, List<Section>> getLore() {
        return lore;
    }

    public void setLore(Map<String, List<Section>> lore) {
        this.lore = lore;
    }

    public Map<String, List<Section>> getRacialFeatures() {
        return racialFeatures;
    }

    public void setRacialFeatures(Map<String, List<Section>> racialFeatures) {
        this.racialFeatures = racialFeatures;
    }
}

