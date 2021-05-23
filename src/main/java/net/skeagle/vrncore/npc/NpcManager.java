package net.skeagle.vrncore.npc;

import com.google.gson.Gson;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Skin;
import net.skeagle.vrncore.utils.SkinUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class NpcManager {

    private final List<Npc> npcList;
    private final Gson gson;

    public NpcManager() {
        this.npcList = new ArrayList<>();
        this.gson = new Gson();
        this.load();

        new EventListener<>(VRNcore.getInstance(), PlayerJoinEvent.class,
                e -> loadNpcsForPlayer(e.getPlayer()));
    }

    protected void load() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        final SQLHelper.Results res = db.queryResults("SELECT * FROM npc");
        res.forEach(npc -> {
            final int id = npc.get(1);
            final String name = npc.getString(2);
            final String display = npc.getString(3);
            final Location loc = VRNUtil.LocationSerialization.deserialize(npc.getString(4));
            final Skin skin = gson.fromJson(npc.getString(5), Skin.class);
            final boolean rotateHead = npc.getBoolean(6);
            npcList.add(new Npc(id, name, display, loc, skin, rotateHead));
        });
    }

    public void loadNpcsForPlayer(final Player p) {
        npcList.forEach(npc -> npc.createNPCForPlayer(p));
    }

    public void deleteNpc(final Npc npc) {
        final SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM npc WHERE id = (?)", npc.getId());
        npcList.remove(npc);
        npc.delete();
    }

    public void createNPC(final String name, final Player p) {
        final Npc npc = new Npc(this.npcList.size(), name, name, p.getLocation(), SkinUtil.getSkin(name), false);
        npcList.add(npc);
        npc.save();
    }

    public Npc getNpc(final String s) {
        return this.npcList.stream().filter(npc -> npc.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
    }

    public List<Npc> getNpcs() {
        return npcList;
    }
}
