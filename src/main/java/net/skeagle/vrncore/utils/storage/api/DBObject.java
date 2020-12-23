package net.skeagle.vrncore.utils.storage.api;

import lombok.Getter;
import net.skeagle.vrncore.db.DBConnect;

import java.sql.Connection;

public abstract class DBObject<T extends StoreableObject<T>> {

    @Getter
    private final String name;
    @Getter
    private final Class<T> objectClass;
    @Getter
    private final Connection conn = DBConnect.getInstance().getConn();

    public DBObject(String name, Class<T> object) {
        this.name = name;
        this.objectClass = object;
    }

    protected void save(T object) {
    }

    protected T load() {
        return null;
    }

    protected void delete(T object) {
    }

    protected void update(T object) {
    }

    public void onFinishLoad() {
    }


}
