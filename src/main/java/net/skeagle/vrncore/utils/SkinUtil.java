package net.skeagle.vrncore.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class SkinUtil {

    private static final Map<String, Skin> skinCache = new HashMap<>();

    public static Skin getSkin(final String name) {
        if (skinCache.keySet().stream().anyMatch(s -> s.equalsIgnoreCase(name)))
            return skinCache.get(name);
        final String[] skinStrings = obtainSkinFromName(name);
        if (skinStrings == null)
            return null;
        final Skin skin = new Skin(name, skinStrings[0], skinStrings[1]);
        skinCache.put(name, skin);
        return skin;
    }

    private static String[] obtainSkinFromName(final String name) {
        final String texture;
        final String signature;
        try {
            final URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            final String uuid = new JsonParser().parse(new InputStreamReader(url.openStream())).getAsJsonObject().get("id").getAsString();

            final URL session = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            final JsonObject textureProperty = new JsonParser().parse(new InputStreamReader(session.openStream())).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            texture = textureProperty.get("value").getAsString();
            signature = textureProperty.get("signature").getAsString();
        } catch (final IOException e) {
            return null;
        }
        return new String[]{texture, signature};
    }
}
