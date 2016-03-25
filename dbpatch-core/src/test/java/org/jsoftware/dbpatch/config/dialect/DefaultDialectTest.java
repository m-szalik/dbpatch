package org.jsoftware.dbpatch.config.dialect;

public class DefaultDialectTest extends AbstractDialectTest<DefaultDialect> {

    @Override
    protected DefaultDialect createDialect() {
        return new DefaultDialect();
    }

    @Override
    protected String getCurrentTimestampSQL() {
        return "select now()";
    }
}