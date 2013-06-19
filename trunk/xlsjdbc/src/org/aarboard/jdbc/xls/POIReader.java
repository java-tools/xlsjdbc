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

import java.io.InputStream;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * This class is a helper class that handles the reading and parsing of data
 * from a .xls file. Uses the poi libraries.
 * 
 * 
 * 
 * @author Andre Schild
 * @author Jonathan Ackerman
 * @author Sander Brienen
 * @author Stuart Mottram (fritto)
 * @version $Id: POIReader.java,v 1.1 2005-11-07 18:08:07 aschild Exp $
 * @created 25 November 2001
 */
public class POIReader implements IXlsReader
{

    private InputStream stream = null;
    private Record[] records = null;
    protected Workbook workbook = null;
//    private Workbook workbook;
    private Sheet input;
    private int cRow;   // The current row we are on
    private String[] columnNames;
    private Row columns;
    private Row buf = null;
    private char separator = ',';
    private boolean suppressHeaders = false;
    private String stringDateFormat = null;
    private String fileName = null;

    /**
     * Constructor for the POIReader object
     * 
     */
    public POIReader()
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
    public POIReader(String fileName) throws Exception
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
    public POIReader(String fileName, char separator, boolean suppressHeaders, String stringDateFormat)
            throws java.lang.Exception
    {
        this.setSeparator(separator);
        this.setSuppressHeaders(suppressHeaders);
        this.setStringDateFormat(stringDateFormat);
        this.setFileName(fileName);
        openFile();
    }

    @Override
    public void openFile() throws Exception
    {
        InputStream inp= new FileInputStream(fileName);

        workbook = WorkbookFactory.create(inp);

        // workbook = Workbook.getWorkbook(new File(fileName));
        int sCount = workbook.getNumberOfSheets();
        input = workbook.getSheetAt(0);
        cRow = 0;

        //input = new BufferedReader(new FileReader(fileName));
        if (this.isSuppressHeaders())
        {
            // No column names available. Read first data line and determine number of colums.
            Row data = input.getRow(cRow++);
            columnNames = new String[data.getLastCellNum()];
            for (int i = 0; i < data.getLastCellNum(); i++)
            {
                columnNames[i] = "COLUMN" + String.valueOf(i + 1);
            }
            data = null;
        // throw away.
        }
        else
        {
            Row data = input.getRow(cRow++);
            columnNames = new String[data.getLastCellNum()];
            for (short i = 0; i < data.getLastCellNum(); i++)
            {
                Cell cell= data.getCell(i);
                if (cell == null)
                {
                        columnNames[i] = "COLUMN" + String.valueOf(i + 1);
                }
                else
                {
                    String headerName= data.getCell(i).getStringCellValue();
                    if (headerName == null || headerName.trim().length()==0)
                    {
                        columnNames[i] = "COLUMN" + String.valueOf(i + 1);
                    }
                    else
                    {
                        columnNames[i] = data.getCell(i).getStringCellValue().toUpperCase();
                    }
                }
            }
        }
    }

    /**
     * Gets the columnNames attribute of the POIReader object
     * 
     * 
     * 
     * @return The columnNames value
     * @since 
     */
    @Override
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
    @Override
    public String getColumn(int columnIndex)
    {
        Cell thisCell = columns.getCell( columnIndex-1);
        if (thisCell.getCellType() == Cell.CELL_TYPE_STRING)
        {
            return thisCell.getStringCellValue();
        }
        else if (thisCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            double cValue = thisCell.getNumericCellValue();
            CellStyle cStyle = thisCell.getCellStyle();
            short cFormatIndex = cStyle.getDataFormat();
            DataFormat thisFormat = workbook.createDataFormat();
            // String cFormat= thisFormat.getFormat(cFormatIndex);
            String retVal = Double.toString(cValue);
            if (retVal.substring(retVal.length() - 2).equals(".0"))
            {
                retVal = retVal.substring(0, retVal.length() - 2);
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
    @Override
    public Date getColumnDate(int columnIndex) throws ParseException
    {
        Cell cellData = columns.getCell(columnIndex-1);
        Date retVal = null;
        try
        {
            retVal = cellData.getDateCellValue();
        }
        catch (NumberFormatException e)
        {
            // Occurs when the cell is a string
            String sData = cellData.getStringCellValue();
            if (sData != null && sData.trim().length() > 0)
            {
                SimpleDateFormat sdFormat;
                if (getStringDateFormat() == null)
                {
                    sdFormat = new SimpleDateFormat();
                }
                else
                {
                    sdFormat = new SimpleDateFormat(getStringDateFormat());
                }
                retVal = sdFormat.parse(sData);
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
    @Override
    public boolean getColumnBoolean(int columnIndex)
    {
        return columns.getCell(columnIndex-1).getBooleanCellValue();
    }

    /**
     * Get the value of the column at the specified index.
     *
     * @param  columnIndex  Description of Parameter
     * @return              The column value
     * @since
     */
    @Override
    public double getColumnDouble(int columnIndex)
    {
        return columns.getCell(columnIndex-1).getNumericCellValue();
    }

    /**
     * Get the value of the column at the specified index.
     *
     * @param  columnIndex  Description of Parameter
     * @return              The column value
     * @since
     */
    @Override
    public int getColumnInt(int columnIndex)
    {
        return (int) columns.getCell(columnIndex-1).getNumericCellValue();
    }

    /**
     * Get the value of the column at the specified index.
     *
     * @param  columnIndex  Description of Parameter
     * @return              The column value
     * @since
     */
    @Override
    public long getColumnLong(int columnIndex)
    {
        return (long) columns.getCell(columnIndex-1).getNumericCellValue();
    }

    /**
     * Get the value of the column at the specified index.
     *
     * @param  columnIndex  Description of Parameter
     * @return              The column value
     * @since
     */
    @Override
    public short getColumnShort(int columnIndex)
    {
        return (short) columns.getCell(columnIndex-1).getNumericCellValue();
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
    @Override
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
    @Override
    public Date getColumnDate(String columnName) throws Exception
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public boolean next() throws Exception
    {
        //columns = new String[columnNames.length];
        Row dataLine;
        if (isSuppressHeaders() && (buf != null))
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
            // See and vote: http://issues.apache.org/bugzilla/show_bug.cgi?id=30635
            //
            int nRows= input.getLastRowNum();
            if (nRows == 0)
            {
                // See the poi javadoc on this
                nRows= input.getPhysicalNumberOfRows();
            }
            if (cRow > nRows)
            {
                dataLine = null;
            }
            else
            {
                dataLine = input.getRow(cRow++);
            }
        }
        if (dataLine == null)
        {
            input = null;
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
    @Override
    public void close()
    {
        try
        {
            input = null;
            // workbook.close();
            buf = null;
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public char getSeparator()
    {
        return separator;
    }

    @Override
    public void setSeparator(char separator)
    {
        this.separator = separator;
    }

    @Override
    public boolean isSuppressHeaders()
    {
        return suppressHeaders;
    }

    @Override
    public void setSuppressHeaders(boolean suppressHeaders)
    {
        this.suppressHeaders = suppressHeaders;
    }

    @Override
    public String getStringDateFormat()
    {
        return stringDateFormat;
    }

    @Override
    public void setStringDateFormat(String stringDateFormat)
    {
        this.stringDateFormat = stringDateFormat;
    }

    @Override
    public String getFileName()
    {
        return fileName;
    }

    @Override
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}

