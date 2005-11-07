/*
 * IXlsReader.java
 *
 * Created on 7. November 2005, 18:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.aarboard.jdbc.xls;

import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author sca
 */
public interface IXlsReader 
{
    
    public void openFile() throws java.lang.Exception;

    /**
     * Description of the Method
     * 
     * 
     * @since 
     */
    void close();

    /**
     * Get value from column at specified name.
     * If the column name is not found, throw an error.
     * 
     * 
     * @param columnName     Description of Parameter
     * @return The column value
     * @exception Exception  Description of Exception
     * @since 
     */
    String getColumn(String columnName) throws Exception;

    /**
     * Get the value of the column at the specified index.
     * 
     * 
     * @param columnIndex  Description of Parameter
     * @return The column value
     * @since 
     */
    String getColumn(int columnIndex);

    /**
     * Get the value of the column at the specified index.
     * 
     * 
     * @param columnIndex  Description of Parameter
     * @return The column value
     * @since 
     */
    boolean getColumnBoolean(int columnIndex);

    /**
     * Get value from column at specified name.
     * If the column name is not found, throw an error.
     * 
     * 
     * @param columnName     Description of Parameter
     * @return The column value
     * @exception Exception  Description of Exception
     * @since 
     */
    Date getColumnDate(String columnName) throws Exception;

    /**
     * Get the value of the column at the specified index.
     * 
     * 
     * @param columnIndex  Description of Parameter
     * @return The column value
     * @since 
     */
    Date getColumnDate(int columnIndex) throws ParseException;

    /**
     * Get value from column at specified name.
     * If the column name is not found, throw an error.
     * 
     * 
     * @param columnName     Description of Parameter
     * @return The column value
     * @exception Exception  Description of Exception
     * @since 
     */
    double getColumnDouble(String columnName) throws Exception;

    /**
     * Get the value of the column at the specified index.
     * 
     * 
     * @param columnIndex  Description of Parameter
     * @return The column value
     * @since 
     */
    double getColumnDouble(int columnIndex);

    /**
     * Get value from column at specified name.
     * If the column name is not found, throw an error.
     * 
     * 
     * @param columnName     Description of Parameter
     * @return The column value
     * @exception Exception  Description of Exception
     * @since 
     */
    int getColumnInt(String columnName) throws Exception;

    /**
     * Get the value of the column at the specified index.
     * 
     * 
     * @param columnIndex  Description of Parameter
     * @return The column value
     * @since 
     */
    int getColumnInt(int columnIndex);

    /**
     * Get value from column at specified name.
     * If the column name is not found, throw an error.
     * 
     * 
     * @param columnName     Description of Parameter
     * @return The column value
     * @exception Exception  Description of Exception
     * @since 
     */
    long getColumnLong(String columnName) throws Exception;

    /**
     * Get the value of the column at the specified index.
     * 
     * 
     * @param columnIndex  Description of Parameter
     * @return The column value
     * @since 
     */
    long getColumnLong(int columnIndex);

    /**
     * Gets the columnNames attribute of the XlsReader object
     * 
     * 
     * @return The columnNames value
     * @since 
     */
    String[] getColumnNames();

    /**
     * Get value from column at specified name.
     * If the column name is not found, throw an error.
     * 
     * 
     * @param columnName     Description of Parameter
     * @return The column value
     * @exception Exception  Description of Exception
     * @since 
     */
    short getColumnShort(String columnName) throws Exception;

    /**
     * Get the value of the column at the specified index.
     * 
     * 
     * @param columnIndex  Description of Parameter
     * @return The column value
     * @since 
     */
    short getColumnShort(int columnIndex);

    /**
     * Description of the Method
     * 
     * 
     * @return Description of the Returned Value
     * @exception Exception  Description of Exception
     * @since 
     */
    boolean next() throws Exception;

    
    public char getSeparator();
    public void setSeparator(char separator);
    public boolean isSuppressHeaders();
    public void setSuppressHeaders(boolean suppressHeaders);
    public String getStringDateFormat();
    public void setStringDateFormat(String stringDateFormat);
    public String getFileName();
    public void setFileName(String fileName);    
}
