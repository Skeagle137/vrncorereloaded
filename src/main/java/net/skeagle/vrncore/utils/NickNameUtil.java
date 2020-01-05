package net.skeagle.vrncore.utils;


import org.bukkit.ChatColor;

import java.util.*;

public class NickNameUtil
{
    private Resources r;
    private final Map<UUID, String> nicknames;
    
    public NickNameUtil(Resources r) {
        nicknames = new HashMap<>();
        this.r = r;
    }
    
    public String getNickName(final UUID player) {
        return nicknames.get(player);
    }
    
    public UUID getPlayerFromNickName(String nick) {
        nick = ChatColor.stripColor(nick);
        for (final Map.Entry<UUID, String> entry : nicknames.entrySet()) {
            if (ChatColor.stripColor(entry.getValue()).equals(nick)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public void setNickName(final UUID id, final String nickname) {
        nicknames.remove(id);
        if (nickname == null || nickname.isEmpty()) {
            return;
        }
        this.nicknames.put(id, nickname);
    }
    
    public void loadNicks() {
        Set<String> keys = r.getplayerData().getKeys(false);
        for (String key : keys) {
            nicknames.put(UUID.fromString(key), r.getplayerData().getString(key + ".nick"));
        }
    }
    
    public void saveNicks() {
        final List<String> done = new ArrayList<>();
        for (final Map.Entry<UUID, String> entry : nicknames.entrySet()) {
            final String key = entry.getKey().toString();
            r.getplayerData().set(key + ".nick", entry.getValue());
            done.add(key);
        }
        for (final String key2 : r.getplayerData().getKeys(false)) {
            if (!done.contains(key2)) {
                r.getplayerData().set(key2 + ".nick", null);
            }
        }
    }
}
