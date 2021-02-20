package net.skeagle.vrncore.utils.storage.api;

import lombok.Getter;
import net.skeagle.vrncore.db.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;

public abstract class DBObject<T extends StoreableObject<T>> {

    @Getter
    private final String name;
    @Getter
    private final Class<T> objectClass;
    @Getter
    private final Connection conn = DBConnect.getConn();

    public DBObject(String name, Class<T> object) {
        this.name = name;
        this.objectClass = object;
    }

    protected void save(T object) {
    }

    protected T load() {
        return null;
    }

    public void onFinishLoad() {
    }


}
