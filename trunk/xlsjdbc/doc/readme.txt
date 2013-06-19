XLS JDBC

XLSJDBC is a readonly jdbc driver to access any xls or xlsx files from java,
just as it where another ordinary SQL database.

The project ist actually hosted at sourceforge.

http://sourceforge.net/projects/xlsjdbc/ is the main project link.

To be able to use this library you must abtain two other libraries as well.
The first is the org.relique.jdbc.csv package who contains a parser for SQL
statement. Most parts of this project are anyway based on the development of
the CVSJDBC driver.
You can download this driver package from sourceforge.

http://csvjdbc.sourceforge.net/ is the place to look for.

Second you need a library who can read the XLS and XLSX files.
For this we use the jakarta poi package.
We only have tested it with release 3.9 of POI.
Available at http://jakarta.apache.org/poi/

If you wish to use XLSX files, make sure to add all required dependencies 
for POI as described here:
http://poi.apache.org/overview.html#components

Once you got those packages you can compile and use the driver.

Actually the "only" thing you can do is a select * from xlsfile
statement.

If you wish to open a xlsx file, you must set the file extension
to .xlsx in the driver properties.

// The default is XLS
info.setProperty(org.aarboard.jdbc.xls.XlsDriver.FILE_EXTENSION, ".xlsx");


There are no other sql statements or options supported, not even a simple
where clausle. You see, there is plenty room for improvements.

Retrieving date fields can cause problems, when the date is entered as string
in excel and not as date. To solve this problem you have to specify the
dateformat (as used by java.text.SimpleDateFormat)

For Switzerland we use this:

info.setProperty(org.aarboard.jdbc.xls.XlsDriver.STRING_DATE_FORMAT, "d.M.yyyy");
conn= DriverManager.getConnection(jdbcURL, info );


Ask me if you wish to contribute to this project.



There exists another project who allows you access to XLS files via JDBC.

http://xlsql.dev.java.net/

With xlsql you can use the full select syntax and even have write access to xls files.

But it requires a rather large setup before you can use it.



For news you can subscribe to freshmeat

http://freshmeat.net/projects/xlsjdbc





(C) 2013 a.schild@aarboard.ch







