
JasperStarter - Ausführen von JasperReports über die Befehlszeile
------------------------------------------------------------------

JasperStarter ist ein Opensource Befehlszeilen Starter und Batch Compiler für
[JasperReports][].

Es hat die folgenden Eigenschaften:

  * Startet jeden JasperReport, der eine JDBC, CSV oder eine leere Datenquelle
    benötigt
  * Verwendbar mit jeder Datenbank, für die ein JDBC Treiber vorhanden ist
  * Führt Reports aus, die Laufzeitparameter benötigen. Jeder Parameter, dessen
    Klasse einen Konstruktor vom Typ String hat, wird akzeptiert. Zusätzlich
    werden die folgenden Parameter-typen unterstützt oder haben eine besondere
    Behandlung:
    * date, image (siehe Verwendung), locale
  * Optionale Eingabeaufforderung für Report-Parameter
  * Druckt direkt auf den Standarddrucker oder auf einen benannten Drucker
  * Zeigt optional einen Druckerdialog zur Auswal des Druckers
  * Zeigt optional eine Druckvorschau an
  * Export in Dateien in den folgenden Formaten:
    * pdf, rtf, xls, xlsx, docx, odt, ods, pptx, csv, html, xhtml, xml, jrprint
  * Exportiert mehrere Formate in einem Aufruf
  * Kompiliert, druckt und exportiert in einem Aufruf
  * Zeigt, druckt oder exportiert zuvor gefüllte Reports (verwendet eine jrprint
    Datei als Eingabe)
  * Kann ein ganzes Verzeichnis von .jrxml Dateien kompilieren.
  * Integriert JasperReports in Anwendungen, die nicht in Java programmiert
    sind. (beispielsweise PHP, Python)
  * Ausführbare Datei unter Windows
  * Enthält JasperReports so das Sie außer diesem Werkzeug nichts installieren
    müssen

Anforderungen

  * Java 1.8 oder höher.
  * Ein JDBC 2.1 Treiber für Ihre Datenbank


### Schnellstart

  * Laden Sie JasperStarter von [Sourceforge][] herunter
  * Entpacken Sie das Distributions Archiv in ein beliebiges Verzeichnis auf
    Ihrem System.
  * Fügen Sie das _./bin_ Verzeichnis Ihrer Installation zu Ihrem Suchpfad
    hinzu.

  * oder, falls Sie mit Windows arbeiten, führen Sie einfach _setup.exe_ aus.

  * Kopieren Sie ihre JDBC Treiber in das _./jdbc_ Verzeichnis Ihrer
    Installation oder verwenden Sie _\--jdbc-dir_ um ein anderes Verzeichnis
    anzugeben.

Rufen Sie JasperStarter mit _\-h_ auf um einen Überblick zu erhalten:

    $ jasperstarter -h

Rufen Sie JasperStarter mit _process \-h_ auf um Hilfe für das Kommando _process_ zu
erhalten:

    $ jasperstarter process -h

Beispiel mit Report-Parametern:

    $ jasperstarter pr report.jasper -t mysql -u myuser -f pdf -H myhost \
     -n mydb -o report -p secret -P CustomerNo=10 StartFrom=2012-10-01

Beispiel mit hsql unter Verwendung des Datenbanktyps generic:

    $ jasperstarter pr report.jasper -t generic -f pdf -o report -u sa \
    --db-driver org.hsqldb.jdbcDriver \
    --db-url jdbc:hsqldb:hsql://localhost

Für weitere Informationen werfen Sie einen Blick in das docs Verzeichnis des
Distributionsarchives oder lesen Sie die Seite Verwendung online. [Usage][]


### Release Notes

Die Änderungen im Projekt können in der englischen Version der
[Änderungsdatei][Changes] eingesehen werden.

### Feedback

Rückmeldungen sind jederzeit wilkommen! Falls Sie irgendwelche Fragen oder
Vorschläge haben, zögern Sie nicht in unser Forum [discussion][] zu schreiben
(möglichst in englisch).
Falls Sie einen Fehler gefunden haben oder eine Funktion vermissen, melden Sie
sich in unserem [Issuetracker][] an und erzeugen Sie einen "Issue" vom Typ "Bug"
oder "New Feature".

Falls Ihnen die Software gefällt, können Sie auch hier [review][] eine Bewertung
abgeben. :-)


### Entwicklung

Der Quellcode ist bei [bitbucket.org/cenote/jasperstarter][] verfügbar, die
Projekt-Webseite ist bei [Sourceforge][] gehostet.

JasperStarter wird mit Hilfe von [Maven][] erzeugt. Um ein Distributionsarchiv
zu erhalten, rufen Sie den folgenden Befehl auf:

    $ mvn package -P release

oder, falls Sie aus dem aktuellen Entwicklungszweig (default branch) erzeugen,
verwenden Sie besser:

    $ mvn package -P release,snapshot

**Achtung! Sie können** `target/jasperstarter.jar` **nicht direkt ausführen,**
**ohne die Abhängigkeiten im Verzeichnis** `../lib` **zu haben!** Siehe **dev**
Profil weiter unten!

Falls Sie das Windows Setup erzeugen wollen, benötigen Sie _nsis_ in Ihrem
Suchpfad (funktioniert auch unter Linux, eine kompilierte Version habe ich auf
Sourceforge im Ordner _build-tools_ bereit gestellt) und Sie müssen das Profil
**windows-setup** zum Aufruf hinzufügen:

    $ mvn package -P release,windows-setup

oder

    $ mvn package -P release,windows-setup,snapshot

Während der Entwicklung möchten Sie vielleicht einen schnelleren Build. Das
**dev** Profil spart einige lang laufende Reports und die Erzeugung der
gepackten Archive aus. Stattdessen wird das Ergebnis in
_target/jasperstarter-dev-bin_ abgelegt.

    $ mvn package -P dev

Nun können Sie JasperStarter ohne IDE aufrufen:

    $ target/jasperstarter-dev-bin/bin/jasperstarter

oder

    $ java -jar target/jasperstarter-dev-bin/lib/jasperstarter.jar

Während der Entwicklung möchten Sie vielleicht nicht von Tests gestört werden.
Daher sind die folgenden Optionen sinnvoll:

    $ package -P dev -D skipTests

oder

    $ package -P dev -D maven.test.failure.ignore=true

Um JasperStarter aus Ihrer IDE heraus auszuführen, fügen Sie _\--jdbc-dir jdbc_
zu den Argumenten Ihrer Startkonfiguration hinzu. Andernfalls erhalten Sie
folgenden Fehler:

    Error, (...)/JasperStarter/target/classes/jdbc is not a directory!

Kopieren Sie Ihre JDBC Treiber in das _./jdbc_ Verzeichnis Ihres Projektes, um
aus der IDE heraus einen Datenbank Report zu starten.


### Lizenz

Copyright 2012, 2013, 2014 Cenote GmbH.

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
[review]:http://sourceforge.net/projects/jasperstarter/reviews
[discussion]:http://sourceforge.net/p/jasperstarter/discussion/
[Issuetracker]:https://cenote-issues.atlassian.net/browse/JAS
[Usage]:http://jasperstarter.sourceforge.net/usage.html
[Issues]:https://cenote-issues.atlassian.net/browse/JAS
[Changes]:changes.html
