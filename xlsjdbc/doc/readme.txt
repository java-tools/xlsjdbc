XLS JDBC
--------

XLSJDBC is a readonly jdbc driver to be able to access any xls files from java,
just as it where another ordinary SQL database.

The project ist actually hosted at sourceforge.

https://sourceforge.net/projects/xlsjdbc/ is the main project link.

To be able to use this library you must abtain two other libraries as well.
The first is the org.relique.jdbc.csv package who contains a parser for SQL
statement. Most parts of this project ae anyway based on the development of
the CVSJDBC driver.
You can download this driver package from sourceforge.

http://sourceforge.net/projects/csvjdbc/ is the place to look for.

Second you need a library who can read the XLS files. For this we use the
jakarta poi package. We only have tested it with release 2.5 of POI.
Available at http://jakarta.apache.org/poi/

Once you got those packages you can compile and use the driver.

Actually the "only" thing you can do is a select * from xlsfile
statement.

There are no other sql statements or options supported, not even a simple
where clausle. You see, there  is plenty room for improvements.

Retrieving date fields can cause problems, when the date is entered as string
in excel and not as date. To solve this problem you have to specify the
dateformat (as used by java.text.SimpleDateFormat)

For Switzerland we use this:

info.setProperty(org.aarboard.jdbc.xls.XlsDriver.STRING_DATE_FORMAT, "d.M.yyyy");
conn= DriverManager.getConnection(jdbcURL, info );


Ask me if you wish to contribute to this project.

(C) 2004 a.schild@aarboard.ch
