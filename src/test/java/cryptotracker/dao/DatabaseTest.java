package cryptotracker.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {
    
    File testFile;
    Database database;
    Connection conn;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        this.testFile = new File("db/databaseTest.db");
        initializeDatabase(testFile.getPath());
    }

    @After
    public void tearDown() {
        testFile.delete();
    }
    
    private void initializeDatabase(String testAddress) throws ClassNotFoundException, SQLException {
        this.database = new Database("jdbc:sqlite:" + testAddress);
        this.conn = database.getConnection();
    }

    @Test
    public void connectionCanBeEstablished() throws SQLException {
        assertFalse(conn.isClosed());
    }
    
    @Test
    public void initializeTablesWorks() throws SQLException {
        database.initializeTables();
    }

}