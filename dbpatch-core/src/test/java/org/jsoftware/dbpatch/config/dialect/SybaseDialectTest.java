package org.jsoftware.dbpatch.config.dialect;

public class SybaseDialectTest extends AbstractDialectTest<DefaultDialect> {

    @Override
    protected DefaultDialect createDialect() {
        return new SybaseDialect();
    }

    @Override
    protected String getCurrentTimestampSQL() {
        return "select getDate()";
    }
}