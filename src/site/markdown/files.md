JasperStarter Files
--------------------

As the major version number of zero indicates, this software is in **beta**
state!
Feedback is very welcome. See README.md inside the distribution archive.

JasperStarter distribution files have the following naming convention:

    JasperStarter-<version>-<type>.<archiveTye>

Version number for production releases:

    <major>.<minor>.<bugfix>

Version number for testing releases - not for production use:

    <major>.<minor>-SNAPSHOT

Types:

  * **bin** - means binary distribution
  * **setup** - Windows Installer

Choose your favorit archive type. The content is equal in each archive.

Manifest
---------

Content of a distribution archive:

    bin/            - executable binaries for Windows, Mac OSX, Linux, etc.
    docs/           - JasperStarter documentation in html format
    jdbc/           - place for your jdbc drivers (jar files)
    lib/            - needed libraries
    CHANGES
    LICENSE
    NOTICE
    README.md

Please don't touch the structure of the directories or JasperStarter will not
work.
