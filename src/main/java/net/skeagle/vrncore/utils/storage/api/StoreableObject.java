package net.skeagle.vrncore.utils.storage.api;

public abstract class StoreableObject<T> {

    protected T deserialize() {
        return null;
    }

    protected Object serialize() {
        return null;
    }
}
