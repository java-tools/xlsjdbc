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

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Random;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;

/**
 * This class is a helper class that handles the reading and parsing of data
 * from a .xls file.
 *
 * @author     Andre Schild
 * @author     Jonathan Ackerman
 * @author     Sander Brienen
 * @author     Stuart Mottram (fritto)
 * @created    25 November 2001
 * @version    $Id: XlsReader.java,v 1.6 2004-12-13 15:20:20 aschild Exp $
 */

public class XlsReader
{
    private InputStream    stream       = null;
    private Record[]       records      = null;
    protected HSSFWorkbook workbook = null;
    
    
//    private Workbook workbook;
    private HSSFSheet    input;
    private int     cRow;   // The current row we are on
    private String[] columnNames;
    private HSSFRow columns;
    private HSSFRow buf = null;
    private char separator = ',';
    private boolean suppressHeaders = false;
    private String stringDateFormat= null;


  /**
   *Constructor for the XlsReader object
   *
   * @param  fileName       Description of Parameter
   * @exception  Exception  Description of Exception
   * @since
   */
  public XlsReader(String fileName) throws Exception
  {
    this(fileName, ',', false, null);
  }


  /**
   *
   * Creation date: (6-11-2001 15:02:42)
   *
   * @param  fileName                 java.lang.String
   * @param  seperator                char
   * @param  suppressHeaders          boolean
   * @exception  java.lang.Exception  The exception description.
   * @since
   */
  public XlsReader(String fileName, char separator, boolean suppressHeaders, String stringDateFormat)
       throws java.lang.Exception
  {
    this.separator = separator;
    this.suppressHeaders = suppressHeaders;
    this.stringDateFormat= stringDateFormat;

    POIFSFileSystem fs =new POIFSFileSystem(new FileInputStream(fileName));
    
    workbook = new HSSFWorkbook(fs);

    // workbook = Workbook.getWorkbook(new File(fileName));
    int sCount= workbook.getNumberOfSheets();
    input= workbook.getSheetAt(0);
    cRow= 0;
                          
    //input = new BufferedReader(new FileReader(fileName));
    if (this.suppressHeaders)
    {
      // No column names available. Read first data line and determine number of colums.
      HSSFRow data = input.getRow(cRow++);
      columnNames = new String[data.getLastCellNum()];
      for (int i = 0; i < data.getLastCellNum(); i++)
      {
        columnNames[i] = "COLUMN" + String.valueOf(i+1);
      }
      data = null;
      // throw away.
    }
    else
    {
      HSSFRow data = input.getRow(cRow++);
      columnNames = new String[data.getLastCellNum()];
      for (short i = 0; i < data.getLastCellNum(); i++)
      {
        columnNames[i] = data.getCell(i).getStringCellValue().toUpperCase();
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
    HSSFCell thisCell= columns.getCell((short)columnIndex);
    if (thisCell.getCellType() == HSSFCell.CELL_TYPE_STRING)
    {
        return thisCell.getStringCellValue();
    }
    else if (thisCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
    {
        double cValue= thisCell.getNumericCellValue();
        HSSFCellStyle cStyle= thisCell.getCellStyle();
        short cFormatIndex = cStyle.getDataFormat();
        HSSFDataFormat thisFormat= workbook.createDataFormat();
        // String cFormat= thisFormat.getFormat(cFormatIndex);
        String retVal= Double.toString(cValue );
        if (retVal.substring(retVal.length()-2).equals(".0"))
        {
            retVal= retVal.substring(0, retVal.length()-2);
        }
        return retVal;
    }
    else
    {
        return thisCell.getStringCellValue();
    }
  }


  
  /**
   * Get the value of the column at the specified index.
   *
   * @param  columnIndex  Description of Parameter
   * @return              The column value
   * @since
   */
  
  public java.util.Date getColumnDate(int columnIndex) throws java.text.ParseException
  {
      HSSFCell cellData= columns.getCell((short) columnIndex);
      java.util.Date retVal= null;
      try
      {
            retVal= cellData.getDateCellValue();
      }
      catch (NumberFormatException e)
      {
          // Occurs when the cell is a string
          String sData= cellData.getStringCellValue();
          if (sData != null && sData.trim().length() > 0)
          {
              java.text.SimpleDateFormat sdFormat;
              if (stringDateFormat == null)
              {
                  sdFormat= new java.text.SimpleDateFormat();
              }
              else
              {
                  sdFormat= new java.text.SimpleDateFormat(stringDateFormat);
              }
              retVal= sdFormat.parse(sData);
          }
      }
      return retVal;
  }

  /**
   * Get the value of the column at the specified index.
   *
   * @param  columnIndex  Description of Parameter
   * @return              The column value
   * @since
   */
  
  public boolean getColumnBoolean(int columnIndex)
  {
      return columns.getCell((short) columnIndex).getBooleanCellValue();
  }

  /**
   * Get the value of the column at the specified index.
   *
   * @param  columnIndex  Description of Parameter
   * @return              The column value
   * @since
   */
  
  public double getColumnDouble(int columnIndex)
  {
      return columns.getCell((short) columnIndex).getNumericCellValue();
  }

  /**
   * Get the value of the column at the specified index.
   *
   * @param  columnIndex  Description of Parameter
   * @return              The column value
   * @since
   */
  
  public int getColumnInt(int columnIndex)
  {
      return (int) columns.getCell((short) columnIndex).getNumericCellValue();
  }
  
  /**
   * Get the value of the column at the specified index.
   *
   * @param  columnIndex  Description of Parameter
   * @return              The column value
   * @since
   */
  
  public long getColumnLong(int columnIndex)
  {
      return (long) columns.getCell((short) columnIndex).getNumericCellValue();
  }
  
  /**
   * Get the value of the column at the specified index.
   *
   * @param  columnIndex  Description of Parameter
   * @return              The column value
   * @since
   */
  
  public short getColumnShort(int columnIndex)
  {
      return (short) columns.getCell((short) columnIndex).getNumericCellValue();
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
   * Get value from column at specified name.
   * If the column name is not found, throw an error.
   *
   * @param  columnName     Description of Parameter
   * @return                The column value
   * @exception  Exception  Description of Exception
   * @since
   */
  
  public java.util.Date getColumnDate(String columnName) throws Exception
  {
      columnName = columnName.toUpperCase();
      for (int loop = 0; loop < columnNames.length; loop++)
      {
          if (columnName.equals(columnNames[loop]))
          {
              return getColumnDate(loop);
          }
      }
      throw new Exception("Column '" + columnName + "' not found.");
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
  
  public boolean getColumnBoolean(String columnName) throws Exception
  {
      columnName = columnName.toUpperCase();
      for (int loop = 0; loop < columnNames.length; loop++)
      {
          if (columnName.equals(columnNames[loop]))
          {
              return getColumnBoolean(loop);
          }
      }
      throw new Exception("Column '" + columnName + "' not found.");
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
  
  public double getColumnDouble(String columnName) throws Exception
  {
      columnName = columnName.toUpperCase();
      for (int loop = 0; loop < columnNames.length; loop++)
      {
          if (columnName.equals(columnNames[loop]))
          {
              return getColumnDouble(loop);
          }
      }
      throw new Exception("Column '" + columnName + "' not found.");
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
  
  public int getColumnInt(String columnName) throws Exception
  {
      columnName = columnName.toUpperCase();
      for (int loop = 0; loop < columnNames.length; loop++)
      {
          if (columnName.equals(columnNames[loop]))
          {
              return getColumnInt(loop);
          }
      }
      throw new Exception("Column '" + columnName + "' not found.");
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
  
  public short getColumnShort(String columnName) throws Exception
  {
      columnName = columnName.toUpperCase();
      for (int loop = 0; loop < columnNames.length; loop++)
      {
          if (columnName.equals(columnNames[loop]))
          {
              return getColumnShort(loop);
          }
      }
      throw new Exception("Column '" + columnName + "' not found.");
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
  
  public long getColumnLong(String columnName) throws Exception
  {
      columnName = columnName.toUpperCase();
      for (int loop = 0; loop < columnNames.length; loop++)
      {
          if (columnName.equals(columnNames[loop]))
          {
              return getColumnLong(loop);
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
    HSSFRow dataLine;
    if (suppressHeaders && (buf != null))
    {
      // The buffer is not empty yet, so use this first.
      dataLine = buf;
      buf = null;
    }
    else
    {
      // read new line of data from input.
        //
        // Correct would be a check for >=, but since there is a bug in POI 2.5
        // who returns one row too less, we do a check on >=
        //
        if (cRow > input.getLastRowNum())
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
      // workbook.close();
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
      // workbook.close();
      buf = null;
    }
    catch (Exception e)
    {
    }
  }

}

