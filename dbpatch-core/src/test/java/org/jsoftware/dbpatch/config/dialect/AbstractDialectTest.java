package org.jsoftware.dbpatch.config.dialect;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public abstract class AbstractDialectTest<D extends Dialect> {
    protected D dialect;

    @Before
    public void setUp() throws Exception {
        dialect = createDialect();
    }

    protected abstract D createDialect();

    protected abstract String getCurrentTimestampSQL();


    @Test
    public void testTimestamp() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        Statement statement = Mockito.mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        Timestamp timestamp = Mockito.mock(Timestamp.class);
        when(resultSet.getTimestamp(anyInt())).thenReturn(timestamp);
        Timestamp ts = dialect.getNow(connection);

//        Assert.assertSame(timestamp, ts);
//        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
//        verify(statement).executeQuery(queryCaptor.capture());
//        Assert.assertEquals(getCurrentTimestampSQL().toLowerCase(), queryCaptor.getValue().toLowerCase());
//        verify(resultSet).close();
//        verify(statement).close();
    }

}