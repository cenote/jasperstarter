
JasperStarter - Spouštění JasperReports z příkazového řádku
-----------------------------------------------------------

JasperStarter je open-source spouštěč pro příkazový řádek a batch kompilátor pro
[JasperReports][].

Má následující vlastnosti:

  * spustí jakýkoliv JasperReport, který potřebuje jdbc datový zdroj či prázdný
    datový zdroj
  * lze použít pro jakoukoliv databázi, pro kterou existuje jdbc driver
  * Provádí reporty, které vyžadují runtime parametery. Podporuje následující
    druhy parametrů:
    * string, int, double, date, image (viz použití)
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

  * Java 1.6 či vyšší
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

Vyvoláním JasperStarteru s _pr \-h_ získáte nápovědu k příkazu _process_

    $ jasperstarter pr -h

Příklad s parametry reportu:

    $ jasperstarter pr -t mysql -u myuser -f pdf -H myhost -n mydb -i report.jasper \
    -o report -p secret -P CustomerNo=string:10 StartFrom=date:2012-10-01

Příklad s hsql s použitím databáze typu generic:

    $ jasperstarter pr -t generic -f pdf -i report.jasper -o report -u sa \
    --db-driver org.hsqldb.jdbcDriver \
    --db-url jdbc:hsqldb:hsql://localhost

Další informace naleznete v distribučním archivu v adresáři docs nebo online
na stránce Použití. [Usage][]


### Release Notes

Poznámky k vydání naleznete v anglické verzi této stránky: [original][]




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

Pokud chcete sputit JasperStarter v rámci vašeho IDE, přidejte k seznamu příkazů
v konfiguraci _\--jdbc-dir jdbc_. Bez toho dostanete chybovou hlášku:

    Error, (...)/JasperStarter/target/classes/jdbc is not a directory!

Zkopírujte vaše jdbc drivery do adresáře _./jdbc_ vašeho projektu, abyste mohli
vyvolat JasperStarter v rámci vašeho IDE a získali report z databáze.


### Licence

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
[review]:http://sourceforge.net/projects/jasperstarter/reviews
[discussion]:http://sourceforge.net/p/jasperstarter/discussion/
[Issuetrackeru]:https://cenote-issues.atlassian.net/browse/JAS
[Usage]:http://jasperstarter.sourceforge.net/usage.html
[Issues]:https://cenote-issues.atlassian.net/browse/JAS
[original]:../index.html
