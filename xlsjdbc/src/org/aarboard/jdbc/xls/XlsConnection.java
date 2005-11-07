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
 */
package org.aarboard.jdbc.xls;

import java.sql.*;
import java.util.Map;
import java.util.Hashtable;

/**
 * This class implements the Connection interface for the XlsJdbc driver.
 *
 * @author     Andre Schild
 * @author     Jonathan Ackerman
 * @author     Sander Brienen
 * @created    25 November 2001
 * @version    $Id: XlsConnection.java,v 1.3 2005-11-07 18:08:07 aschild Exp $
 */

public class XlsConnection implements Connection
{
  private String filePath = null;
  private String fileExtension = ".xls";
  private String stringDateFormat = null;
  private char separator=',';
  private boolean suppressHeaders=false;
  private String xlsReaderClass= "org.aarboard.jdbc.xls.POIReader";


  /**
   *Constructor for the XlsConnection object
   *
   * @param  filePath  Description of Parameter
   * @since
   */
  protected XlsConnection(String filePath)
  {
    DriverManager.println("XlsJdbc - XlsConnection() - filePath=" + filePath);
    this.filePath = filePath;
  }


  /**
   *Constructor for the XlsConnection object
   *
   * @param  filePath  Description of Parameter
   * @param  info      Description of Parameter
   * @since
   */
  protected XlsConnection(String filePath, java.util.Properties info)
  {
    DriverManager.println("XlsJdbc - XlsConnection() - filePath=" + filePath);
    this.filePath = filePath;

    // check for properties
    if (info != null)
    {
      fileExtension = info.getProperty(XlsDriver.FILE_EXTENSION,fileExtension);
      separator     = info.getProperty(XlsDriver.SEPARATOR,new Character(separator).toString()).charAt(0);
      suppressHeaders = Boolean.valueOf(info.getProperty(XlsDriver.SUPPRESS_HEADERS,String.valueOf(suppressHeaders))).booleanValue();
      stringDateFormat = info.getProperty(XlsDriver.STRING_DATE_FORMAT, null);
      xlsReaderClass = info.getProperty(XlsDriver.XLS_READER_CLASS, "org.aarboard.jdbc.xls.POIReader");
    }
    DriverManager.println("XlsJdbc - XlsConnection() - filePath=" + filePath +
                                                    " - file extension=" + fileExtension +
                                                    " - separator=" + separator +
                                                    " - suppress headers="+suppressHeaders);
  }


  /**
   *Sets the autoCommit attribute of the XlsConnection object
   *
   * @param  autoCommit        The new autoCommit value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void setAutoCommit(boolean autoCommit) throws SQLException { }


  /**
   *Sets the readOnly attribute of the XlsConnection object
   *
   * @param  readOnly          The new readOnly value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void setReadOnly(boolean readOnly) throws SQLException { }


  /**
   *Sets the catalog attribute of the XlsConnection object
   *
   * @param  catalog           The new catalog value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void setCatalog(String catalog) throws SQLException { }


  /**
   *Sets the transactionIsolation attribute of the XlsConnection object
   *
   * @param  level             The new transactionIsolation value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void setTransactionIsolation(int level) throws SQLException { }


  /**
   *Sets the typeMap attribute of the XlsConnection object
   *
   * @param  map               The new typeMap value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void setTypeMap(Map map) throws SQLException { }


  /**
   *Gets the autoCommit attribute of the XlsConnection object
   *
   * @return                   The autoCommit value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public boolean getAutoCommit() throws SQLException
  {
    // always false
    return false;
  }


  /**
   *Gets the closed attribute of the XlsConnection object
   *
   * @return                   The closed value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public boolean isClosed() throws SQLException
  {
    return false;
  }


  /**
   *Gets the metaData attribute of the XlsConnection object
   *
   * @return                   The metaData value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public DatabaseMetaData getMetaData() throws SQLException
  {
    throw new SQLException("NYI");
  }


  /**
   *Gets the readOnly attribute of the XlsConnection object
   *
   * @return                   The readOnly value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public boolean isReadOnly() throws SQLException
  {
    // always reexpecting ;adonly
    return true;
  }


  /**
   *Gets the catalog attribute of the XlsConnection object
   *
   * @return                   The catalog value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public String getCatalog() throws SQLException
  {
    return null;
  }


  /**
   *Gets the transactionIsolation attribute of the XlsConnection object
   *
   * @return                   The transactionIsolation value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public int getTransactionIsolation() throws SQLException
  {
    return Connection.TRANSACTION_NONE;
  }


  /**
   *Gets the warnings attribute of the XlsConnection object
   *
   * @return                   The warnings value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public SQLWarning getWarnings() throws SQLException
  {
    return null;
  }


  /**
   *Gets the typeMap attribute of the XlsConnection object
   *
   * @return                   The typeMap value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public Map getTypeMap() throws SQLException
  {
    return new Hashtable();
  }


  /**
   *Description of the Method
   *
   * @return                   Description of the Returned Value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public Statement createStatement() throws SQLException
  {
    return new XlsStatement(this);
  }


  /**
   *Description of the Method
   *
   * @param  sql               Description of Parameter
   * @return                   Description of the Returned Value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public PreparedStatement prepareStatement(String sql) throws SQLException
  {
    throw new SQLException("Not Supported !");
  }


  /**
   *Description of the Method
   *
   * @param  sql               Description of Parameter
   * @return                   Description of the Returned Value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public CallableStatement prepareCall(String sql) throws SQLException
  {
    throw new SQLException("Not Supported !");
  }


  /**
   *Description of the Method
   *
   * @param  sql               Description of Parameter
   * @return                   Description of the Returned Value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public String nativeSQL(String sql) throws SQLException
  {
    throw new SQLException("Not Supported !");
  }


  /**
   *Description of the Method
   *
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void commit() throws SQLException { }


  /**
   *Description of the Method
   *
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void rollback() throws SQLException { }


  /**
   *Description of the Method
   *
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void close() throws SQLException { }


  /**
   *Description of the Method
   *
   * @exception  SQLException  Description of Exception
   * @since
   */
  public void clearWarnings() throws SQLException { }


  /**
   *Description of the Method
   *
   * @param  resultSetType         Description of Parameter
   * @param  resultSetConcurrency  Description of Parameter
   * @return                       Description of the Returned Value
   * @exception  SQLException      Description of Exception
   * @since
   */
  public Statement createStatement(int resultSetType, int resultSetConcurrency)
       throws SQLException
  {
    throw new SQLException("Not Supported !");
  }


  /**
   *Description of the Method
   *
   * @param  sql                   Description of Parameter
   * @param  resultSetType         Description of Parameter
   * @param  resultSetConcurrency  Description of Parameter
   * @return                       Description of the Returned Value
   * @exception  SQLException      Description of Exception
   * @since
   */
  public PreparedStatement prepareStatement(
      String sql,
      int resultSetType,
      int resultSetConcurrency)
       throws SQLException
  {
    throw new SQLException("Not Supported !");
  }


  /**
   *Description of the Method
   *
   * @param  sql                   Description of Parameter
   * @param  resultSetType         Description of Parameter
   * @param  resultSetConcurrency  Description of Parameter
   * @return                       Description of the Returned Value
   * @exception  SQLException      Description of Exception
   * @since
   */
  public CallableStatement prepareCall(
      String sql,
      int resultSetType,
      int resultSetConcurrency)
       throws SQLException
  {
    throw new SQLException("Not Supported !");
  }


  /**
   *Gets the filePath attribute of the XlsConnection object
   *
   * @return    The filePath value
   * @since
   */
  protected String getFilePath()
  {
    return filePath;
  }


  /**
   * Insert the method's description here.
   *
   * Creation date: (14-11-2001 8:49:37)
   *
   * @return    java.lang.String
   * @since
   */
  protected String getExtension()
  {
    return fileExtension;
  }

  /**
   * Insert the method's description here.
   *
   * Creation date: (14-11-2001 8:49:37)
   *
   * @return    java.lang.String
   * @since
   */
  protected String getStringDateFormat()
  {
    return stringDateFormat;
  }

  /**
   * Insert the method's description here.
   *
   * Creation date: (13-11-2001 10:49:00)
   *
   * @return    char
   * @since
   */
  protected char getSeperator()
  {
    return separator;
  }


  /**
   * Insert the method's description here.
   *
   * Creation date: (13-11-2001 10:49:00)
   *
   * @return    boolean
   * @since
   */
  protected boolean isSuppressHeaders()
  {
    return suppressHeaders;
  }
  
  
  public int getHoldability() throws java.sql.SQLException 
{
    throw new SQLException("Not Supported !");
}
  
  public java.sql.CallableStatement prepareCall(String str, int param, int param2, int param3) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  
  public java.sql.PreparedStatement prepareStatement(String str, int param) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  
  public java.sql.PreparedStatement prepareStatement(String str, int[] values) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  
  public java.sql.PreparedStatement prepareStatement(String str, String[] str1) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }

  public java.sql.PreparedStatement prepareStatement(String str, int param, int param2, int param3) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  
  public void releaseSavepoint(java.sql.Savepoint savepoint) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  public void rollback(java.sql.Savepoint savepoint) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  public void setHoldability(int param) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }
  
  public java.sql.Savepoint setSavepoint(String str) throws java.sql.SQLException {
    throw new SQLException("Not Supported !");
  }

  public Statement createStatement(int resultSetType,
                                 int resultSetConcurrency,
                                 int resultSetHoldability)
                          throws SQLException
{
    throw new SQLException("Not Supported !");
  }

    public String getXlsReaderClass() {
        return xlsReaderClass;
    }

    public void setXlsReaderClass(String xlsReaderClass) {
        this.xlsReaderClass = xlsReaderClass;
    }
}

