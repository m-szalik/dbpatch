package org.jsoftware.dbpatch.config.dialect;

public class OracleDialectTest extends AbstractDialectTest<OracleDialect> {

    @Override
    protected OracleDialect createDialect() {
        return new OracleDialect();
    }

    @Override
    protected String getCurrentTimestampSQL() {
        return "select sysdate from dual";
    }
}
