/*
 *  XlsJdbc - a JDBC driver for XLS files
 *  Copyright (C) 2002 Andre Schild
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * IXlsReader.java
 *
 * Created on 7. November 2005, 18:11
 *
 */
package org.aarboard.jdbc.xls;

import java.text.ParseException;
import java.util.Date;

/**
 * Interface for hiding jxl and poi implementation details
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
