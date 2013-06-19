import java.sql.SQLException;
import junit.framework.*;
import static junit.framework.Assert.assertTrue;
/*
 * TestNull.java
 * JUnit based test
 *
 * Created on 20. April 2005, 11:21
 */

/**
 *
 * @author sca
 */
public class TestColIndex extends TestCase {
    
    public TestColIndex(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testNull()
    {
	testNull("org.aarboard.jdbc.xls.POIReader", ".xls");
	testNull("org.aarboard.jdbc.xls.POIReader", ".xlsx");
	// testNull("org.aarboard.jdbc.xls.JXLReader");
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    public void testNull(String readerClass, String type)
    {
        String jdbcClassName= "org.aarboard.jdbc.xls.XlsDriver";
        String jdbcURL= "jdbc:aarboard:xls:C:/Develop/Sourceforge/xlsjdbc/xlsjdbc/test/testdata/";
        String jdbcUsername= "";
        String jdbcPassword= "";
        String jdbcTableName= "rowcolmap";
        int nCount= 1093;
        
        try
        {
            Class.forName(jdbcClassName);
	    java.util.Properties info= new java.util.Properties();
	    info.setProperty(org.aarboard.jdbc.xls.XlsDriver.XLS_READER_CLASS, readerClass);
	    info.setProperty(org.aarboard.jdbc.xls.XlsDriver.FILE_EXTENSION, type);
            java.sql.Connection conn= java.sql.DriverManager.getConnection(jdbcURL, info);  

            // create a Statement object to execute the query with
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet results= stmt.executeQuery("SELECT * FROM "+jdbcTableName);
            int rCount= 0;
            while (results.next())
            {
                String thisA= results.getString("A");
                String thisB= results.getString("B");
                String thisAa= results.getString(1);
                String thisBb= results.getString(2);
                assertTrue("Did not find expected "+thisA+", but "+thisAa, thisA.equals(thisAa));
                assertTrue("Did not find expected "+thisB+", but "+thisBb, thisB.equals(thisBb));
            }
            results.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException e)
        {
            assertFalse("SQLException e: "+e.getMessage(), true);
        }
        catch (ClassNotFoundException ce)
        {
            assertFalse("SQLException ce: "+ce.getMessage(), true);
        }
    }
}
