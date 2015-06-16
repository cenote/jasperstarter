Soubory JasperStarteru
----------------------

Distribuční soubory JasperStarteru mají následující konvenci pro pojmenování:

    JasperStarter-<version>-<type>.<archiveTye>

Číslování verzí produktu:

    <major>.<minor>.<bugfix>

Číslování kandidátů na zveřejnění - většinou jsou zralé pro zveřejnění, ale 
ještě je musíte otestovat ;-) :

    <major>.<minor>-RC<N>

Číslování testovacích verzí - nevhodných pro produktivní použití:

    <major>.<minor>-SNAPSHOT-<git-short-commit-id>

Typy:

  * **bin** - znamená binární distribuci
  * **setup** - spouštěcí program pro Windows

Vyberte si svůj oblíbený typ archivu. Obsah je naprosto identický.

Prohlášení
---------

Obsah distribučního archivu:

    bin/            - spouštěcí programy pro Windows, Mac OSX, Linux, atd.
    docs/           - JasperStarter Dokumentace ve formátu html
    jdbc/           - Adresář pro vaše JDBC Drivery (soubory jar)
    lib/            - potřebné knihovny
    CHANGES
    LICENSE
    NOTICE
    README.md

Prosím neměňte strukturu adresářů, JasperStarter by pak nefungoval.

Více informací naleznete v README.md, které se nachází v distribučním archivu.
