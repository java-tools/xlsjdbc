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

import java.io.*;
import java.util.*;
import xlrd.*;

/**
 * This class is a helper class that handles the reading and parsing of data
 * from a .xls file.
 *
 * @author     Andre Schild
 * @author     Jonathan Ackerman
 * @author     Sander Brienen
 * @author     Stuart Mottram (fritto)
 * @created    25 November 2001
 * @version    $Id: XlsReader.java,v 1.1.1.1 2002-04-27 21:06:19 aschild Exp $
 */

public class XlsReader
{
    private Workbook workbook;
    private Sheet    input;
    private int     cRow;   // The current row we are on
    private String[] columnNames;
    private Cell[] columns;
    private Cell[] buf = null;
    private char separator = ',';
    private boolean suppressHeaders = false;


  /**
   *Constructor for the XlsReader object
   *
   * @param  fileName       Description of Parameter
   * @exception  Exception  Description of Exception
   * @since
   */
  public XlsReader(String fileName) throws Exception
  {
    this(fileName, ',', false);
  }


  /**
   * Insert the method's description here.
   *
   * Creation date: (6-11-2001 15:02:42)
   *
   * @param  fileName                 java.lang.String
   * @param  seperator                char
   * @param  suppressHeaders          boolean
   * @exception  java.lang.Exception  The exception description.
   * @since
   */
  public XlsReader(String fileName, char separator, boolean suppressHeaders)
       throws java.lang.Exception
  {
    this.separator = separator;
    this.suppressHeaders = suppressHeaders;

    
    workbook = Workbook.getWorkbook(new File(fileName));
    int sCount= workbook.getNumberOfSheets();
    input= workbook.getSheet(0);
    cRow= 0;
                          
    //input = new BufferedReader(new FileReader(fileName));
    if (this.suppressHeaders)
    {
      // No column names available. Read first data line and determine number of colums.
      Cell[] data = input.getRow(cRow++);
      columnNames = new String[data.length];
      for (int i = 0; i < data.length; i++)
      {
        columnNames[i] = "COLUMN" + String.valueOf(i+1);
      }
      data = null;
      // throw away.
    }
    else
    {
      Cell[] data = input.getRow(cRow++);
      columnNames = new String[data.length];
      for (int i = 0; i < data.length; i++)
      {
        columnNames[i] = data[i].getContents().toUpperCase();
      }
    }
  }


  /**
   *Gets the columnNames attribute of the XlsReader object
   *
   * @return    The columnNames value
   * @since
   */
  public String[] getColumnNames()
  {
    return columnNames;
  }


  /**
   * Get the value of the column at the specified index.
   *
   * @param  columnIndex  Description of Parameter
   * @return              The column value
   * @since
   */

  public String getColumn(int columnIndex)
  {
    return columns[columnIndex].getContents();
  }


  /**
   * Get value from column at specified name.
   * If the column name is not found, throw an error.
   *
   * @param  columnName     Description of Parameter
   * @return                The column value
   * @exception  Exception  Description of Exception
   * @since
   */

  public String getColumn(String columnName) throws Exception
  {
    columnName = columnName.toUpperCase();
    for (int loop = 0; loop < columnNames.length; loop++)
    {
      if (columnName.equals(columnNames[loop]))
      {
        return getColumn(loop);
      }
    }
    throw new Exception("Column '" + columnName + "' not found.");
  }


  /**
   *Description of the Method
   *
   * @return                Description of the Returned Value
   * @exception  Exception  Description of Exception
   * @since
   */
  public boolean next() throws Exception
  {
    //columns = new String[columnNames.length];
    Cell dataLine[];
    if (suppressHeaders && (buf != null))
    {
      // The buffer is not empty yet, so use this first.
      dataLine = buf;
      buf = null;
    }
    else
    {
      // read new line of data from input.
        if (cRow >= input.getRows())
        {
            dataLine= null;
        }
        else
        {
            dataLine = input.getRow(cRow++);
        }
    }
    if (dataLine == null)
    {
      input= null;
      workbook.close();
      return false;
    }
    columns = dataLine;
    return true;
  }


  /**
   *Description of the Method
   *
   * @since
   */
  public void close()
  {
    try
    {
      input= null;
      workbook.close();
      buf = null;
    }
    catch (Exception e)
    {
    }
  }

}

