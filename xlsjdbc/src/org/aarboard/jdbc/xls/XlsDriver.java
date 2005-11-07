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
import java.util.Properties;
import java.io.File;

/**
 * This class implements the Driver interface for the XlsJdbc driver.
 *
 * @author     Andre Schild
 * @author     Jonathan Ackerman
 * @author     Sander Brienen
 * @author     JD Evora
 * @version    $Id: XlsDriver.java,v 1.6 2005-11-07 18:08:07 aschild Exp $
 */

public class XlsDriver implements Driver
{
  public static final String FILE_EXTENSION="fileExtension";
  public static final String SEPARATOR="separator";
  public static final String SUPPRESS_HEADERS="suppressHeaders";
  public static final String STRING_DATE_FORMAT="stringDateFormat"; /// The format to use when converting a string into date in getDate calls
  private final static String URL_PREFIX = "jdbc:aarboard:xls:";
  public static final String XLS_READER_CLASS= "XlsReaderClass";    /// What class to use for acessing xls files, can be either "org.aarboard.jdbc.xls.POIReader" or "org.aarboard.jdbc.xls.JXLReader"
  private Properties info = null;


  /**
   *Gets the propertyInfo attribute of the XlsDriver object
   *
   * @param  url               Description of Parameter
   * @param  info              Description of Parameter
   * @return                   The propertyInfo value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
       throws SQLException
  {
    return new DriverPropertyInfo[0];
  }


  /**
   *Gets the majorVersion attribute of the XlsDriver object
   *
   * @return    The majorVersion value
   * @since
   */
  public int getMajorVersion()
  {
    return 1;
  }


  /**
   *Gets the minorVersion attribute of the XlsDriver object
   *
   * @return    The minorVersion value
   * @since
   */
  public int getMinorVersion()
  {
    return 5;
  }


  /**
   *Description of the Method
   *
   * @param  url               Description of Parameter
   * @param  info              Description of Parameter
   * @return                   Description of the Returned Value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public Connection connect(String url, Properties info) throws SQLException
  {
    DriverManager.println("XlsJdbc - XlsDriver:connect() - url=" + url);
    if (url == null)
    {
        throw new SQLException("Null path specified");
    }
    // check for correct url
    if (!url.startsWith(URL_PREFIX))
    {
        throw new SQLException("URL does not start with "+URL_PREFIX);
    }
    // get filepath from url
    String filePath = url.substring(URL_PREFIX.length());
    if (!filePath.endsWith(File.separator))
    {
      filePath += File.separator;
    }

    DriverManager.println("XlsJdbc - XlsDriver:connect() - filePath=" + filePath);

    // check if filepath is a correct path.
    File checkPath = new File(filePath);
    if (!checkPath.exists())
    {
      throw new SQLException("Specified path '" + filePath + "' not found !");
    }
    if (!checkPath.isDirectory())
    {
      throw new SQLException(
          "Specified path '" + filePath + "' is  not a directory !");
    }

    return new XlsConnection(filePath, info);
  }


  /**
   *Description of the Method
   *
   * @param  url               Description of Parameter
   * @return                   Description of the Returned Value
   * @exception  SQLException  Description of Exception
   * @since
   */
  public boolean acceptsURL(String url) throws SQLException
  {
    DriverManager.println("XlsJdbc - XlsDriver:accept() - url=" + url);
    return url.startsWith(URL_PREFIX);
  }


  /**
   *Description of the Method
   *
   * @return    Description of the Returned Value
   * @since
   */
  public boolean jdbcCompliant()
  {
    return false;
  }
  // This static block inits the driver when the class is loaded by the JVM.
  static
  {
    try
    {
      java.sql.DriverManager.registerDriver(new XlsDriver());
    }
    catch (SQLException e)
    {
      throw new RuntimeException(
          "FATAL ERROR: Could not initialise XLS driver ! Message was: "
           + e.getMessage());
    }
  }
}

