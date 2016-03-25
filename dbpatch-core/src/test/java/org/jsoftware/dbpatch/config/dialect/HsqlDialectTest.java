package org.jsoftware.dbpatch.config.dialect;

public class HsqlDialectTest extends AbstractDialectTest<HsqlDialect> {

    @Override
    protected HsqlDialect createDialect() {
        return new HsqlDialect();
    }

    @Override
    protected String getCurrentTimestampSQL() {
        return "VALUES (CURRENT_TIMESTAMP)";
    }
}
