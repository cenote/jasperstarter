
JasperStarter - Running JasperReports from command line
--------------------------------------------------------

JasperStarter is an opensource command line launcher for [JasperReports][].

It has the following features:

  * Run any JasperReport that needs a jdbc datasource or empty datasource
  * Use with any database for which a jdbc driver is available
  * Execute reports that need runtime parameters. The following parameter types
    are supported:
    * string, int, date
  * Print directly to system default or given printer
  * Optionally show printer dialog to choose printer
  * Optionally Show printpreview
  * Export to file in the following formats:
    * pdf, rtf, docx, odt, html
  * Export multiple formats in one commanding call
  * Print and export in one commanding call
  * Integrate in non Java applications (for example PHP, Python)
  * Binary executable on Windows


### Quickstart

Download JasperStarter from [Sourceforge][]

Extract the distribution archive to any directory on your system.

Add the ./bin directoy of your installation to your searchpath.

Put your jdbc drivers in the _./jdbc_ directory of your installation or use
 _\--jdbc-dir_ to point to a different directory.

Invoke JasperStarter with _\-h_ to get an overview:

    $ jasperstarter -h

Example with reportparameters:

    $ jasperstarter -t mysql -u myuser -f pdf -H myhost -n mydb -i report.jasper \
    -o report -p secret -P CustomerNo=string:10 StartFrom=date:2012-10-01

Example with hsql using database type generic:

    $ jasperstarter -t generic -f pdf -i report.jasper -o report -u sa \
    --db-driver org.hsqldb.jdbcDriver \
    --db-url jdbc:hsqldb:hsql://localhost

For more information take a look in the docs directory of the distibution
archive or the project website.


### Developement

The sourcecode is available at [bitbucket.org/cenote/jasperstarter][], the
project website is hosted at [Sourceforge][].

JasperStarter is build with [Maven][]. To get a distribution package run:

    $ mvn package

To run JasperStarter from within your IDE add _\--jdbc-dir jdbc_ to the argument
list of your run configuration. Otherwise you will get an error:

    Error, (...)/JasperStarter/target/classes/jdbc is not a directory!

Put your jdbc drivers in the _./jdbc_ directory of the project to invoke
JasperStarter from within your IDE to call up a database based report.


### License

Copyright 2012 Cenote GmbH.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[JasperReports]:http://community.jaspersoft.com/project/jasperreports-library
[Maven]:http://maven.apache.org/
[Sourceforge]:http://sourceforge.net/projects/jasperstarter/
[bitbucket.org/cenote/jasperstarter]:http://bitbucket.org/cenote/jasperstarter
