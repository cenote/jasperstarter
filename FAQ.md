## FAQ - Frequently Asked Questions

**Q: Howto fix database connection errors? It was working before...**

**A:** In short: Use the generic JDBC datasource type like in [Example
with hsql using database type
generic](http://jasperstarter.cenote.de/index.html#Quickstart)
  
  JasperStarter provides some simple options to make it easy to work
  with some popular databases (f.e. Mysql). These options work only
  for some simple use cases. For other databases or more advanced
  connection options you must use the generic JDBC datasource type `-t generic` and
  provide any needed option in the JDBC-URL (Option:  `--db-url` and
  `--jdbc-driver`).
  
  Nowadays, when products tend to have much stronger security
  defaults, it happens that you cannot connect to a new configured
  database with simple standard options. You must read and fully
  understand your database documentation, download the correct
  JDBC-Driver and provide all necessary parameters in a valid
  JDBC-URL. For Mysql you find some information here:
  [jdbc-url-format](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html)
  and [using-ssl](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-using-ssl.html)
  
**Q: I cannot compile/run a report that I created with the newest
  Jaspersoft Studio. Is this a bug?**

**A:** No! JasperStarter is just a command line wrapper around the
  JasperReports library. It's features are defined by the version of
  the JasperReports library which is shipped with JasperStarter. You
  cannot use any feature of JasperReports that is not supported by the
  version of this library. The best option is to use a version of
  Jaspersoft Studio that matches the version of JasperReports inside
  JasperStarter. You can see the version by calling: `jasperstarter
  -V`
