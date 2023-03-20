package net.skeagle.vrncore.utils;

public class Skin {

    private final String name;
    private final String texture;
    private final String signature;

    public Skin(final String name, final String texture, final String signature) {
        this.name = name;
        this.texture = texture;
        this.signature = signature;
    }

    public String serialize() {
        return VRNUtil.GSON.toJson(this);
    }

    public String getName() {
        return name;
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }
}
