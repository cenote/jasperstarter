JasperStarter Dateien
----------------------

JasperStarter Distributions-Dateien haben die folgenden Namens Konventionen:

    JasperStarter-<version>-<type>.<archiveTye>

Versions Nummer für Produktions Releases:

    <major>.<minor>.<bugfix>

Versions Nummer für Release Kandidaten - sollten für die Produktion reif sein,
benötigen aber noch einige Test durch Sie ;-) :

    <major>.<minor>-RC<N>

Versions Nummer für Test Releases - nicht für den produktiven Einsatz:

    <major>.<minor>-SNAPSHOT-<Mercurial-Revision>

Typen:

  * **bin** - bedeutet binäre Distribution
  * **setup** - Windows Installations Programm

Wählen Sie Ihren bevorzugten Archiv Typ. Der Inhalt ist gleich in jedem Archiv.

Manifest
---------

Inhalt eines Distributions Archives:

    bin/            - Ausführbare Dateien für Windows, Mac OSX, Linux, etc.
    docs/           - JasperStarter Dokumentation im html Format
    jdbc/           - Verzeichnis für Ihre JDBC Treiber (jar Dateien)
    lib/            - Benötigte Bibliotheken
    CHANGES
    LICENSE
    NOTICE
    README.md

Bitte ändern Sie nicht die Struktur der Verzeichnisse, JasperStarter wird sonst
nicht funktionieren.

Für weitere Informationen siehe README.md im Distributions Archiv.