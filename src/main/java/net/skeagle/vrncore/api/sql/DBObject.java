package net.skeagle.vrncore.api.sql;

import java.sql.Connection;

public abstract class DBObject<T extends StoreableObject<T>> {

    private final String name;
    private final Class<T> objectClass;
    private final Connection conn = SQLConnection.getConnection();

    public DBObject(final String name, final Class<T> object) {
        this.name = name;
        this.objectClass = object;
    }

    public void onFinishLoad() {
    }

    public String getName() {
        return name;
    }

    public Class<T> getObjectClass() {
        return objectClass;
    }

    public Connection getConn() {
        return conn;
    }
}
