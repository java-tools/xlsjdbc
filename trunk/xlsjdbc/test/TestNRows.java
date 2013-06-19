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
public class TestNRows extends TestCase {
    
    public TestNRows(String testName) 
    {
        super(testName);
    }

    public void testNRows()
    {
        doTestNRows(10, "org.aarboard.jdbc.xls.POIReader", ".xls");
        doTestNRows(10, "org.aarboard.jdbc.xls.POIReader", ".xlsx");
        // doTestNRows(10, "org.aarboard.jdbc.xls.JXLReader");
        doTestNRows(51, "org.aarboard.jdbc.xls.POIReader", ".xls");
        doTestNRows(51, "org.aarboard.jdbc.xls.POIReader", ".xlsx");
        // doTestNRows(51, "org.aarboard.jdbc.xls.JXLReader");
        doTestNRows(1017, "org.aarboard.jdbc.xls.POIReader", ".xlsx");
        doTestNRows(1017, "org.aarboard.jdbc.xls.POIReader", ".xls");
        // doTestNRows(1017, "org.aarboard.jdbc.xls.JXLReader");
    }
    
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    public void doTestNRows(int nCount, String readerClass, String type)
    {
        String jdbcClassName= "org.aarboard.jdbc.xls.XlsDriver";
        String jdbcURL= "jdbc:aarboard:xls:C:/Develop/Sourceforge/xlsjdbc/xlsjdbc/test/testdata/";
        String jdbcUsername= "";
        String jdbcPassword= "";
        String jdbcTableName= ""+nCount+"rows";
        
        try
        {
	    java.util.Properties info= new java.util.Properties();
	    info.setProperty(org.aarboard.jdbc.xls.XlsDriver.XLS_READER_CLASS, readerClass);
	    info.setProperty(org.aarboard.jdbc.xls.XlsDriver.FILE_EXTENSION, type);
	    
            Class.forName(jdbcClassName);
            java.sql.Connection conn= java.sql.DriverManager.getConnection(jdbcURL, info);  

            // create a Statement object to execute the query with
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet results= stmt.executeQuery("SELECT * FROM "+jdbcTableName);
            int rCount= 0;
            while (results.next())
            {
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
