package net.skeagle.vrncore.rewards;

import net.skeagle.vrncore.hook.LuckPermsHook;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum RewardAction {
    PROMOTE(new LuckPermsHook()::promote),
    SET_GROUP(new LuckPermsHook()::setGroup),
    ADD_GROUP(new LuckPermsHook()::addGroup);

    private final BiConsumer<Player, String> action;

    RewardAction(final BiConsumer<Player, String> action) {
        this.action = action;
    }

    public BiConsumer<Player, String> get() {
        return action;
    }
}
