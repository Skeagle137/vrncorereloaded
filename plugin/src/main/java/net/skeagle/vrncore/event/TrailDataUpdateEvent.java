package net.skeagle.vrncore.event;

import net.skeagle.vrncore.playerdata.TrailData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TrailDataUpdateEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final TrailData data;
    private final boolean updateStyle;

    public TrailDataUpdateEvent(Player player, TrailData data) {
        super(player);
        this.data = data;
        this.updateStyle = false;
    }

    public TrailDataUpdateEvent(Player player, TrailData data, boolean updateStyle) {
        super(player);
        this.data = data;
        this.updateStyle = updateStyle;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public TrailData getTrailData() {
        return data;
    }

    public boolean shouldUpdateStyle() {
        return updateStyle;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
