package net.skeagle.vrncore.api.util;

import lombok.Getter;

public final class SkinUtil {

    @Getter
    private final String texture;
    @Getter
    private final String signature;

    public SkinUtil(final String texture, final String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public SkinUtil(final String name) {
        final String[] skin = VRNUtil.getSkin(name);
        if (skin == null) {
            this.texture = null;
            this.signature = null;
            return;
        }
        this.texture = skin[0];
        this.signature = skin[1];
    }

    public String serialize() {
        return getTexture() + " " + getSignature();
    }

    public static SkinUtil deserialize(final String s) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split(" ");
        if (split.length != 2) {
            return null;
        }
        return new SkinUtil(split[0], split[1]);
    }


}
