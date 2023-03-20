package net.skeagle.vrncore.homes;

import net.skeagle.vrnlib.misc.LocationUtils;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record Home(String name, UUID owner, Location location) {

    public CompletableFuture<Void> save(SQLHelper db) {
        return CompletableFuture.runAsync(() ->
                db.execute("INSERT INTO homes (name, owner, location) VALUES (?, ?, ?)",
                        name, owner, LocationUtils.toString(location)));
    }
}
