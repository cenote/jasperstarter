JasperStarter Dateien
----------------------

JasperStarter Distributions-Dateien haben die folgenden Namenskonventionen:

    JasperStarter-<version>-<type>.<archiveTye>

Versionsnummer für Produktionsreleases:

    <major>.<minor>.<bugfix>

Versionsnummer für Release-Kandidaten - sollten für die Produktion reif sein,
benötigen aber noch einige Test durch Sie ;-) :

    <major>.<minor>-RC<N>

Versionsnummer für Testreleases - nicht für den produktiven Einsatz:

    <major>.<minor>-SNAPSHOT-<git-short-commit-id>

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
