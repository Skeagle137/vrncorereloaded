package net.skeagle.vrncore.utils.storage.api;

public abstract class StoreableObject<T> {

    public abstract T deserialize();

    public abstract Object serialize();
}
