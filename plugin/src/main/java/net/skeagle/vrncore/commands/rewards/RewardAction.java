package net.skeagle.vrncore.commands.rewards;

import net.skeagle.vrncore.hook.HookManager;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum RewardAction {
    PROMOTE(HookManager.getLuckPermsHook()::promote),
    SET_GROUP(HookManager.getLuckPermsHook()::setGroup),
    ADD_GROUP(HookManager.getLuckPermsHook()::addGroup);

    private final BiConsumer<Player, String> action;

    RewardAction(final BiConsumer<Player, String> action) {
        this.action = action;
    }

    public BiConsumer<Player, String> get() {
        return action;
    }
}
