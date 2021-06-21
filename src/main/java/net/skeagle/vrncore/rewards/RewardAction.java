package net.skeagle.vrncore.rewards;

import net.skeagle.vrncore.hook.LuckPermsHook;

public enum RewardAction {
    PROMOTE("Promote player", new LuckPermsHook()::promote, true),
    SET_GROUP("Set group", new LuckPermsHook()::setGroup),
    ADD_GROUP("Add group", new LuckPermsHook()::addGroup);

    private final String name;
    private final Action action;
    private boolean appliestoTrack;

    RewardAction(final String name, final Action action) {
        this.name = name;
        this.action = action;
    }

    RewardAction(final String name, final Action action, final boolean appliestoTrack) {
        this.name = name;
        this.action = action;
        this.appliestoTrack = appliestoTrack;
    }

    public String getName() {
        return name;
    }

    public Action getAction() {
        return action;
    }

    public boolean appliesToTrack() {
        return appliestoTrack;
    }

    public RewardAction next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
