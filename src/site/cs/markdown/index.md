
JasperStarter - Spouštění JasperReports z příkazového řádku
-----------------------------------------------------------

JasperStarter je open-source spouštěč pro příkazový řádek a batch kompilátor pro
[JasperReports][].

**JasperStarter is not vulnerable to [CVE-2021-44228](https://nvd.nist.gov/vuln/detail/CVE-2021-44228).**

**But all releases including 3.5.0 contain log4j-1.2.17 which is affected by
[CVE-2019-17571](https://nvd.nist.gov/vuln/detail/CVE-2019-17571).** I cannot say if it is possible to
exploit this with JasperStarter but in any case you should update to a newer version of JasperStarter.

Má následující vlastnosti:

  * spustí jakýkoliv JasperReport, který potřebuje jdbc, csv či prázdný
    datový zdroj
  * lze použít pro jakoukoliv databázi, pro kterou existuje jdbc driver
  * Provádí reporty, které vyžadují runtime parametery. Podporuje všechny 
    parametry, jejichž class (volba) vyžaduje konstruktor typu String. Navíc
    podporuje následující druhy parametrů, nebo pro ně má speciální funkci:
    * date, image (see usage), locale
  * Umožňuje vybrat si z nabídky parametrů reportu
  * Umožňuje tisk na vybrané nebo na defaultní tiskárně
  * Nabízí možnost zobrazit tiskový dialog pro výběr tiskárny
  * Nabízí možnost zobrazit tiskový náhled
  * Exportuje do následujících formátů:
    * pdf, rtf, xls, xlsx, docx, odt, ods, pptx, csv, html, xhtml, xml, jrprint
  * Exportuje v jednom příkazu více formátů najednou
  * Kompiluje, tiskne a exportuje v jednom příkazu
  * Umožňuje náhled, tisk a export již vyplněných reportů (coby input používá
    jrprint soubor)
  * Umí zkompilovat celý adresář .jrxml souborů
  * Lze ho integrovat do aplikací, které nejsou vytvořené v javě (např. PHP,
    Python)
  * Spouštěcí soubor pro Windows
  * Obsahuje JasperReports, takže už nemusíte instalovat nic jiného

Požadavky

  * Java 1.7 / Java 1.8 
  * JDBC 2.1 driver pro vaši databázi


### Rychlý start

  * Stáhněte si JasperStarter ze [Sourceforge][]
  * Rozbalte distribuční archiv do jakéhokoliv adresáře ve vašem systému
  * Přidejte _./bin_ adresář vaší instalace do proměnné path

  * nebo jednoduše ve windows vyvolejte _setup.exe_

  * uložte své jdbc drivery do adresáře _./jdbc_ vaší instalace nebo odkažte na
    jiný adresář pomocí _\--jdbc-dir_

Vyvoláním JasperStarteru s _\-h_ získáte přehled:

    $ jasperstarter -h

Vyvoláním JasperStarteru s _process \-h_ získáte nápovědu k příkazu _process_

    $ jasperstarter process -h

Příklad s parametry reportu:

    $ jasperstarter pr report.jasper -t mysql -u myuser -f pdf -H myhost \
     -n mydb -o report -p secret -P CustomerNo=10 StartFrom=2012-10-01

Příklad s hsql s použitím databáze typu generic:

    $ jasperstarter pr report.jasper -t generic -f pdf -o report -u sa \
    --db-driver org.hsqldb.jdbcDriver \
    --db-url jdbc:hsqldb:hsql://localhost

Další informace naleznete v distribučním archivu v adresáři docs nebo online
na stránce Použití. [Usage][]


### Release Notes

See the english version for the history of [changes].


### Feedback

Zpětná vazba je vítaná! Pokud máte dotazy či návrhy, neváhejte a napište nám 
do [discussion][] fóra.
Našli jste bug nebo postrádáte jistou funkci? Přihlašte se do našeho
[Issuetrackeru][] a vytvořte nový požadavek.

Jste se softwarem spokojení? Napište hodnocení [review][] :-)


### Vývoj

Zdrojový kód je dostupný na [bitbucket.org/cenote/jasperstarter][], webové
stránky projektu hostuje [Sourceforge][].

JasperStarter je vytvořen pomocí [Maven][]. Distribuční balíček získáte
vyvoláním:

    $ mvn package -P release

nebo, pokud tvoříte z aktualní větve (default branch), raději:

    $ mvn package -P release,snapshot

**Pozor!** `target/jasperstarter.jar` **nelze přímo spustit, pokud v adresáři** 
`../lib` **nemáte závislosti!** Viz profil **dev** níže!

Pokud chcete vytvořit setup pro Windows, musíte mít v proměnné path _nsis_ 
(funguje i v Linuxu, zkompilovanou verzi naleznete na soufceforge ve složce 
_build-tools_), k příkazu musíte přidat **windows-setup** profil:

    $ mvn package -P release,windows-setup

nebo

    $ mvn package -P release,windows-setup,snapshot

Během vývoje možná oceníte rychlejší build. Profil **dev** se obejde bez
některých déle trvajících reportů a bez tvorby zabalených archivů. Místo toho je
výsledek uložený do _target/jasperstarter-dev-bin_.

    $ mvn package -P dev

Teď můžete spustit JasperStarter bez IDE:

    $ target/jasperstarter-dev-bin/bin/jasperstarter

nebo

    $ java -jar target/jasperstarter-dev-bin/lib/jasperstarter.jar

Pokud vás během vývoje omezují testy, zkuste následující užitečnou možnost:

    $ package -P dev -D skipTests

nebo

    $ package -P dev -D maven.test.failure.ignore=true


Pokud chcete sputit JasperStarter v rámci vašeho IDE, přidejte k seznamu příkazů
v konfiguraci _\--jdbc-dir jdbc_. Bez toho dostanete chybovou hlášku:

    Error, (...)/JasperStarter/target/classes/jdbc is not a directory!

Zkopírujte vaše jdbc drivery do adresáře _./jdbc_ vašeho projektu, abyste mohli
vyvolat JasperStarter v rámci vašeho IDE a získali report z databáze.


### Licence

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
[Issuetrackeru]:https://cenote-issues.atlassian.net/browse/JAS
[Usage]:http://jasperstarter.sourceforge.net/usage.html
[Issues]:https://cenote-issues.atlassian.net/browse/JAS
[changes]:changes.html
