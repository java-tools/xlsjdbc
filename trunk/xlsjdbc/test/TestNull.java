import junit.framework.*;
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
public class TestNull extends TestCase {
    
    public TestNull(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    public void testNull()
    {
        String jdbcClassName= "org.aarboard.jdbc.xls.XlsDriver";
        String jdbcURL= "jdbc:aarboard:xls:C:/Develop/Sourceforge/xlsjdbc/test/testdata/";
        String jdbcUsername= "";
        String jdbcPassword= "";
        String jdbcTableName= "nulltest1";
        int nCount= 1093;
        
        try
        {
            Class.forName(jdbcClassName);
            java.sql.Connection conn= java.sql.DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword );  

            // create a Statement object to execute the query with
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet results= stmt.executeQuery("SELECT * FROM "+jdbcTableName);
            int rCount= 0;
            while (results.next())
            {
                String thisCountry= results.getString("Land");
                
                rCount++;
                // System.out.println("Current row: "+rCount);
            }
            assertTrue("Did not find expected "+nCount+" rows, but "+rCount, rCount == nCount);
            results.close();
            stmt.close();
            conn.close();
        }
        catch (Exception e)
        {
            assertFalse("Exception e: "+e.getMessage(), true);
        }
    }
}
