JasperStarter Files
--------------------

JasperStarter distribution files have the following naming convention:

    JasperStarter-<version>-<type>.<archiveTye>

Version number for production releases:

    <major>.<minor>.<bugfix>

Version number for release candidates - should be ready for production but needs
some testing from YOU ;-) :

    <major>.<minor>-RC<N>

Version number for testing releases - not for production use:

    <major>.<minor>-SNAPSHOT-<git-short-commit-id>

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

For further information see README.md inside the distribution archive.
