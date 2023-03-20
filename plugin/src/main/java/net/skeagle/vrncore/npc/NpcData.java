package net.skeagle.vrncore.npc;

import net.skeagle.vrncore.VRNCore;
import net.skeagle.vrncore.api.Npc;
import net.skeagle.vrncore.api.VRNCoreNMS;
import net.skeagle.vrncore.utils.Skin;
import net.skeagle.vrnlib.misc.LocationUtils;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NpcData {
    private final VRNCoreNMS api;
    private final String name;
    private final String display;
    private final Location location;
    private final Skin skin;
    private final boolean rotateHead;
    private Npc npc;

    public NpcData(final VRNCoreNMS api, final String name, final String display, final Location location, final Skin skin, final boolean rotateHead) {
        this.api = api;
        this.name = name;
        this.display = display;
        this.location = location;
        this.skin = skin;
        this.rotateHead = rotateHead;
    }

    public void createNPCForPlayer(final Player player) {
        npc = api.createNpc(display.equals(name) ? name : display, location);
        npc.updateForPlayer(player);
    }

    public void removeForPlayer(Player player) {
        npc.removeForPlayer(player);
    }

    public void save() {
        final SQLHelper db = VRNCore.getInstance().getDB();
        Task.asyncDelayed(() -> {
            db.execute("DELETE FROM npc WHERE name = (?)", name);
            db.execute("INSERT INTO npc (name, display, location, skin, rotatehead) VALUES (?, ?, ?, ?, ?)",
                    name, display, LocationUtils.toString(location), skin != null ? skin.serialize() : null, rotateHead);
        });
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }

    public Location getLocation() {
        return location;
    }

    public Skin getSkin() {
        return skin;
    }

    public boolean isRotateHead() {
        return rotateHead;
    }
}
