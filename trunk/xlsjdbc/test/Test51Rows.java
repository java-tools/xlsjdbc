import junit.framework.*;
/*
 * Test51Rows.java
 * JUnit based test
 *
 * Created on 13. Dezember 2004, 15:13
 */

/**
 *
 * @author sca
 */
public class Test51Rows extends TestCase {
    
    public Test51Rows(String testName) {
        super(testName);
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    public void test51Rows()
    {
        String jdbcClassName= "org.aarboard.jdbc.xls.XlsDriver";
        String jdbcURL= "jdbc:aarboard:xls:C:/Develop/Sourceforge/xlsjdbc/test/testdata/";
        String jdbcUsername= "";
        String jdbcPassword= "";
        String jdbcTableName= "51rowsexcel";
        
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
                rCount++;
                System.out.println("Current row: "+rCount);
            }
            assertTrue("Did not find excpected 51 rows, but "+rCount, rCount == 51);
        }
        catch (Exception e)
        {
            assertFalse("Exception e: "+e.getMessage(), true);
        }
}
    
}
