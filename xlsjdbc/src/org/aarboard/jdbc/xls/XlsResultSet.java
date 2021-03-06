/*
 *  XlsJdbc - a JDBC driver for XLS files
 *  Copyright (C) 2002 Andre Schild
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
 */
package org.aarboard.jdbc.xls;

import java.sql.*;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Calendar;

/**
 *This class implements the ResultSet interface for the XlsJdbc driver.
 *
 * @author     Andre Schild
 * @author     Jonathan Ackerman
 * @created    25 November 2001
 * @version    $Id: XlsResultSet.java,v 1.5 2005-11-07 21:16:08 aschild Exp $
 */
public class XlsResultSet implements ResultSet
{

    protected XlsStatement statement;
    protected IXlsReader reader;
    protected String[] columnNames;
    protected String tableName;
    protected ResultSetMetaData resultSetMetaData;

    /**
     *Constructor for the XlsResultSet object
     *
     * @param  statement    Description of Parameter
     * @param  reader       Description of Parameter
     * @param tableName
     * @param  columnNames  Description of Parameter
     * @since
     */
    protected XlsResultSet(XlsStatement statement, IXlsReader reader, String tableName, String[] columnNames)
    {
        this.statement = statement;
        this.reader = reader;
        this.tableName = tableName;
        this.columnNames = columnNames;

        if (columnNames.length == 0 || columnNames[0].equals("*"))
        {
            this.columnNames = reader.getColumnNames();
        }
    }
    
    /**
     * Search for the column with the given name
     * Returns 1-based numbers als used by the JDBC standards
     * 
     * @param columnName
     * @return column number or -1 if not found
     */
    protected int getColumnIndex(String columnName)
    {
        columnName = columnName.toUpperCase();

        for (int loop = 0; loop < columnNames.length; loop++)
        {
            if (columnName.equals(columnNames[loop]))
            {
                return loop+1;
            }
        }
        return -1;
    }

    /**
     *Sets the fetchDirection attribute of the XlsResultSet object
     *
     * @param  p0                The new fetchDirection value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void setFetchDirection(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Sets the fetchSize attribute of the XlsResultSet object
     *
     * @param  p0                The new fetchSize value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void setFetchSize(int p0) throws SQLException
    {
        // 
        // Allowed just to ignore
        //
        // throw new SQLException("Not Supported !");
    }

    /**
     *Gets the string attribute of the XlsResultSet object
     *
     * @param  columnIndex       Description of Parameter
     * @return                   The string value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public String getString(int columnIndex) throws SQLException
    {
        try
        {
            return reader.getColumn(columnIndex);
        }
        catch (NullPointerException ne)
        {
            return null;
        }
        catch (Exception e)
        {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     *Gets the string attribute of the XlsResultSet object
     *
     * @param  columnName        Description of Parameter
     * @return                   The string value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public String getString(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getString(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the statement attribute of the XlsResultSet object
     *
     * @return                   The statement value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Statement getStatement() throws SQLException
    {
        return statement;
    }

    /**
     *Gets the boolean attribute of the XlsResultSet object
     *
     * @param  columnIndex                Description of Parameter
     * @return                   The boolean value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean getBoolean(int columnIndex) throws SQLException
    {
        try
        {
            return reader.getColumnBoolean(columnIndex);
        }
        catch (Exception e)
        {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     *Gets the byte attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The byte value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public byte getByte(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the short attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The short value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public short getShort(int p0) throws SQLException
    {
        return reader.getColumnShort(p0);
    }

    /**
     *Gets the int attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The int value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int getInt(int p0) throws SQLException
    {
        return reader.getColumnInt(p0);
    }

    /**
     *Gets the long attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The long value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public long getLong(int p0) throws SQLException
    {
        return reader.getColumnLong(p0);
    }

    /**
     *Gets the float attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The float value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public float getFloat(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the double attribute of the XlsResultSet object
     *
     * @param  columnIndex       Column to read
     * @return                   The double value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public double getDouble(int columnIndex) throws SQLException
    {
        try
        {
            return reader.getColumnDouble(columnIndex);
        }
        catch (Exception e)
        {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     *Gets the bigDecimal attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The bigDecimal value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int p0, int p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the bytes attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The bytes value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public byte[] getBytes(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the date attribute of the XlsResultSet object
     *
     * @param  columnIndex    Column to get date value for
     * @return                   The date value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Date getDate(int columnIndex) throws SQLException
    {
        try
        {
            java.util.Date retVal = reader.getColumnDate(columnIndex);
            if (retVal != null)
            {
                return new java.sql.Date(retVal.getTime());
            }
            else
            {
                return null;
            }
        }
        catch (java.lang.NullPointerException ne)
        {
            return null;
        }
        catch (Exception e)
        {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     *Gets the time attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The time value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Time getTime(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the timestamp attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The timestamp value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Timestamp getTimestamp(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the asciiStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The asciiStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public InputStream getAsciiStream(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the unicodeStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The unicodeStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    @Deprecated
    public InputStream getUnicodeStream(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the binaryStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The binaryStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public InputStream getBinaryStream(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the boolean attribute of the XlsResultSet object
     *
     * @param  columnName        Column to read
     * @return                   The boolean value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean getBoolean(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getBoolean(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the byte attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The byte value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public byte getByte(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the short attribute of the XlsResultSet object
     *
     * @param  columnName        Column to read
     * @return                   The short value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public short getShort(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getShort(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the int attribute of the XlsResultSet object
     *
     * @param  columnName        Column to read
     * @return                   The int value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int getInt(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getInt(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the long attribute of the XlsResultSet object
     *
     * @param  columnName        Column to read
     * @return                   The long value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public long getLong(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getLong(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the float attribute of the XlsResultSet object
     *
     * @param  columnName        Column to read
     * @return                   The float value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public float getFloat(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getFloat(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the double attribute of the XlsResultSet object
     *
     * @param  columnName        Column to read
     * @return                   The double value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public double getDouble(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getDouble(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the bigDecimal attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The bigDecimal value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String p0, int p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the bytes attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The bytes value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public byte[] getBytes(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the date attribute of the XlsResultSet object
     *
     * @param  columnName        Name of column to retrieve date for
     * @return                   The date value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Date getDate(String columnName) throws SQLException
    {
        int index= getColumnIndex(columnName);
        if (index != -1)
        {
            return getDate(index);
        }
        else
        {
            throw new SQLException("Column '" + columnName + "' not found.");
        }
    }

    /**
     *Gets the time attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The time value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Time getTime(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the timestamp attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The timestamp value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Timestamp getTimestamp(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the asciiStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The asciiStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public InputStream getAsciiStream(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the unicodeStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The unicodeStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    @Deprecated
    public InputStream getUnicodeStream(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the binaryStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The binaryStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public InputStream getBinaryStream(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the warnings attribute of the XlsResultSet object
     *
     * @return                   The warnings value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public SQLWarning getWarnings() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the cursorName attribute of the XlsResultSet object
     *
     * @return                   The cursorName value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public String getCursorName() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the metaData attribute of the XlsResultSet object
     *
     * @return                   The metaData value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException
    {
        if (resultSetMetaData == null)
        {
            resultSetMetaData = new XlsResultSetMetaData(tableName, columnNames);
        }

        return resultSetMetaData;
    }

    /**
     *Gets the object attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The object value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Object getObject(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the object attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The object value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Object getObject(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the characterStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The characterStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Reader getCharacterStream(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the characterStream attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The characterStream value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Reader getCharacterStream(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the bigDecimal attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The bigDecimal value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public BigDecimal getBigDecimal(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the bigDecimal attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The bigDecimal value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public BigDecimal getBigDecimal(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the beforeFirst attribute of the XlsResultSet object
     *
     * @return                   The beforeFirst value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean isBeforeFirst() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the afterLast attribute of the XlsResultSet object
     *
     * @return                   The afterLast value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean isAfterLast() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the first attribute of the XlsResultSet object
     *
     * @return                   The first value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean isFirst() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the last attribute of the XlsResultSet object
     *
     * @return                   The last value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean isLast() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the row attribute of the XlsResultSet object
     *
     * @return                   The row value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int getRow() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the fetchDirection attribute of the XlsResultSet object
     *
     * @return                   The fetchDirection value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int getFetchDirection() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the fetchSize attribute of the XlsResultSet object
     *
     * @return                   The fetchSize value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int getFetchSize() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the type attribute of the XlsResultSet object
     *
     * @return                   The type value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int getType() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the concurrency attribute of the XlsResultSet object
     *
     * @return                   The concurrency value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int getConcurrency() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the object attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The object value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Object getObject(int p0, Map p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the ref attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The ref value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Ref getRef(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the blob attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The blob value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Blob getBlob(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the clob attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The clob value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Clob getClob(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the array attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The array value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Array getArray(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the object attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The object value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Object getObject(String p0, Map p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the ref attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The ref value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Ref getRef(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the blob attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The blob value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Blob getBlob(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the clob attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The clob value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Clob getClob(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the array attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @return                   The array value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Array getArray(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the date attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The date value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Date getDate(int p0, Calendar p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the date attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The date value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Date getDate(String p0, Calendar p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the time attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The time value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Time getTime(int p0, Calendar p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the time attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The time value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Time getTime(String p0, Calendar p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the timestamp attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The timestamp value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Timestamp getTimestamp(int p0, Calendar p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Gets the timestamp attribute of the XlsResultSet object
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @return                   The timestamp value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public Timestamp getTimestamp(String p0, Calendar p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean next() throws SQLException
    {
        try
        {
            return reader.next();
        }
        catch (Exception e)
        {
            throw new SQLException("Error reading data. Message was: " + e);
        }
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void close() throws SQLException
    {
        reader.close();
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean wasNull() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void clearWarnings() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public int findColumn(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void beforeFirst() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void afterLast() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean first() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean last() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean absolute(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean relative(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean previous() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean rowUpdated() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean rowInserted() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @return                   Description of the Returned Value
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public boolean rowDeleted() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateNull(int p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBoolean(int p0, boolean p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateByte(int p0, byte p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateShort(int p0, short p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateInt(int p0, int p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateLong(int p0, long p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateFloat(int p0, float p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateDouble(int p0, double p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBigDecimal(int p0, BigDecimal p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateString(int p0, String p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBytes(int p0, byte[] p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateDate(int p0, Date p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateTime(int p0, Time p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateTimestamp(int p0, Timestamp p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateAsciiStream(int p0, InputStream p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBinaryStream(int p0, InputStream p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateCharacterStream(int p0, Reader p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateObject(int p0, Object p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateObject(int p0, Object p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateNull(String p0) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBoolean(String p0, boolean p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateByte(String p0, byte p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateShort(String p0, short p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateInt(String p0, int p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateLong(String p0, long p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateFloat(String p0, float p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateDouble(String p0, double p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBigDecimal(String p0, BigDecimal p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateString(String p0, String p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBytes(String p0, byte[] p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateDate(String p0, Date p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateTime(String p0, Time p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateTimestamp(String p0, Timestamp p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateAsciiStream(String p0, InputStream p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateBinaryStream(String p0, InputStream p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateCharacterStream(String p0, Reader p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @param  p2                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateObject(String p0, Object p1, int p2) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @param  p0                Description of Parameter
     * @param  p1                Description of Parameter
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateObject(String p0, Object p1) throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void insertRow() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void updateRow() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void deleteRow() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void refreshRow() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void cancelRowUpdates() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void moveToInsertRow() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    /**
     *Description of the Method
     *
     * @exception  SQLException  Description of Exception
     * @since
     */
    @Override
    public void moveToCurrentRow() throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public URL getURL(int columnNumber)
    {
        return null;
    }

    @Override
    public URL getURL(String columnNumber)
    {
        return null;
    }

    @Override
    public void updateRef(int columnIndex,
            java.sql.Ref x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateRef(String columnName,
            Ref x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateBlob(int columnIndex,
            Blob x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateBlob(String columnName,
            Blob x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateClob(int columnIndex,
            Clob x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateClob(String columnName,
            Clob x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateArray(int columnIndex,
            Array x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateArray(String columnName,
            Array x)
            throws SQLException
    {
        throw new SQLException("Not Supported !");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClosed() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}

