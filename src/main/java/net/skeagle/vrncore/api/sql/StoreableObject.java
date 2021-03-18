package net.skeagle.vrncore.api.sql;

public abstract class StoreableObject<T> {

    protected T deserialize() {
        return null;
    }

    protected Object serialize() {
        return null;
    }
}
