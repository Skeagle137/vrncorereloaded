package net.skeagle.vrncore.trail;

public enum TrailType {
    PLAYER("player"),
    ARROW("arrow");

    String name;

    TrailType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
