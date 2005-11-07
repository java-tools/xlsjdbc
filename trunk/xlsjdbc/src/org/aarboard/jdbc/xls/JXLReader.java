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

/*
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
*/
 
/**
 * This class is a helper class that handles the reading and parsing of data
 * from a .xls file.
 * 
 * 
 * 
 * @author Andre Schild
 * @author Jonathan Ackerman
 * @author Sander Brienen
 * @author Stuart Mottram (fritto)
 * @version $Id: JXLReader.java,v 1.2 2005-11-07 21:16:08 aschild Exp $
 * @created 25 November 2001
 */

public class JXLReader implements IXlsReader
{
    protected jxl.Workbook workbook = null;
    
    
//    private Workbook workbook;
    private jxl.Sheet	    input;
    private int     cRow;   // The current row we are on
    private String[] columnNames;
    // private HSSFRow columns;
    // private HSSFRow buf = null;
    private char separator = ',';
    private boolean suppressHeaders = false;
    private String stringDateFormat= null;
    private String fileName= null;
    private int	inputRows= 0;


  /**
     * Constructor for the POIReader object
     * 
     * 
     * 
     * @param fileName       Description of Parameter
     * @exception Exception  Description of Exception
     * @since 
     */
  public JXLReader()
  {
  }

    
  /**
     * Constructor for the POIReader object
     * 
     * 
     * 
     * @param fileName       Description of Parameter
     * @exception Exception  Description of Exception
     * @since 
     */
  public JXLReader(String fileName) throws Exception
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
  public JXLReader(String fileName, char separator, boolean suppressHeaders, String stringDateFormat)
       throws java.lang.Exception
  {
    this.setSeparator(separator);
    this.setSuppressHeaders(suppressHeaders);
    this.setStringDateFormat(stringDateFormat);
    this.setFileName(fileName);
    openFile();
  }


  public void openFile() throws java.lang.Exception
  {      
    workbook = jxl.Workbook.getWorkbook(new java.io.File(getFileName()));

    // workbook = Workbook.getWorkbook(new File(fileName));
    int sCount= workbook.getNumberOfSheets();
    input= workbook.getSheet(0);
    cRow= 0;
                          
    //input = new BufferedReader(new FileReader(fileName));
    if (this.isSuppressHeaders())
    {
      // No column names available. Read first data line and determine number of colums.
	int nColumns= input.getColumns();
	columnNames = new String[nColumns];
	for (int i = 0; i < nColumns; i++)
	{
	    columnNames[i] = "COLUMN" + String.valueOf(i+1);
	}
	cRow= -1;
    }
    else
    {
	int nColumns= input.getColumns();
	columnNames = new String[nColumns];
	for (int i = 0; i < nColumns; i++)
	{
            columnNames[i] = input.getCell(i, cRow).getContents().toUpperCase();
	}
    }
    inputRows= input.getRows();
  }
  
  
  
  /**
     * Gets the columnNames attribute of the POIReader object
     * 
     * 
     * 
     * @return The columnNames value
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
	jxl.Cell cellData= input.getCell(columnIndex, cRow);
	if (cellData.getType() == jxl.CellType.EMPTY)
	{
	    return null;
	}
	return cellData.getContents();
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
      jxl.Cell cellData= input.getCell(columnIndex, cRow);
      java.util.Date retVal= null;
      if (cellData.getType() == jxl.CellType.EMPTY)
      {
	  return null;
      }
      try
      {
	    jxl.DateCell dCell= (jxl.DateCell) cellData;
            retVal= dCell.getDate();
      }
      catch (NumberFormatException e)
      {
          // Occurs when the cell is a string
          String sData= cellData.getContents();
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
      jxl.Cell cellData= input.getCell(columnIndex, cRow);
      if (cellData.getType() == jxl.CellType.EMPTY)
      {
	  return false;
      }
      
//      if (thisCell.getType() == jxl.BooleanCell)
      {
	  jxl.BooleanCell bData= (jxl.BooleanCell) cellData;
	  return bData.getValue();
      }
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
      jxl.Cell cellData= input.getCell(columnIndex, cRow);
      if (cellData.getType() == jxl.CellType.EMPTY)
      {
	  return 0;
      }
//      if (thisCell.getType() == jxl.NumberCell)
      {
	  jxl.NumberCell bData= (jxl.NumberCell) cellData;
	  return bData.getValue();
      }
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
      jxl.Cell cellData= input.getCell(columnIndex, cRow);
      if (cellData.getType() == jxl.CellType.EMPTY)
      {
	  return 0;
      }
//      if (thisCell.getType() == jxl.NumberCell)
      {
	  jxl.NumberCell bData= (jxl.NumberCell) cellData;
	  return (int) bData.getValue();
      }
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
      jxl.Cell cellData= input.getCell(columnIndex, cRow);
      if (cellData.getType() == jxl.CellType.EMPTY)
      {
	  return 0;
      }
//      if (thisCell.getType() == jxl.NumberCell)
      {
	  jxl.NumberCell bData= (jxl.NumberCell) cellData;
	  return (long) bData.getValue();
      }
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
      jxl.Cell cellData= input.getCell(columnIndex, cRow);
      if (cellData.getType() == jxl.CellType.EMPTY)
      {
	  return 0;
      }
//      if (thisCell.getType() == jxl.NumberCell)
      {
	  jxl.NumberCell bData= (jxl.NumberCell) cellData;
	  return (short) bData.getValue();
      }
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
      boolean retVal= false;

      // System.out.println(cRow);
      if (cRow >= inputRows-1)
      {
	  retVal= false;
      }
      else
      {
	  cRow++;
	  retVal= true;
      }
      return retVal;
  }


  /**
   *Description of the Method
   *
   * @since
   */
  public void close()
  {
      workbook.close();
  }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public boolean isSuppressHeaders() {
        return suppressHeaders;
    }

    public void setSuppressHeaders(boolean suppressHeaders) {
        this.suppressHeaders = suppressHeaders;
    }

    public String getStringDateFormat() {
        return stringDateFormat;
    }

    public void setStringDateFormat(String stringDateFormat) {
        this.stringDateFormat = stringDateFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
  
}

