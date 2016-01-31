Dbpatch-gradle-plugin
=====================

### Manage versioning of your databases with Gradle

[![video presentation](http://img.youtube.com/vi/hD5ACGfmkM4/0.jpg)](http://www.youtube.com/watch?v=hD5ACGfmkM4)

#### Plugin's tasks:
 * dbpatch-help – help screen
 * dbpatch-help-parse – parse sql _(use dbpatch.file system property to indicate file to parse)_
 * dbpatch-list – display list of patches
 * dbpatch-patch – patch database
 * dbpatch-rollback-list – check if there is a rollback file for each patch
 * dbpatch-rollback – rollback patch or multiple patches
 * dbpatch-interactive – interactive mode (GUI) – screen below

#### Interactive mode
This plugin can be executed as standard java program (java -jar [plugin-jar-file.jar](http://central.maven.org/maven2/org/jsoftware/dbpatch/dbpatch-core))

![interactive mode screen shot](https://raw.github.com/m-szalik/dbpatch/master/docs/dbpatch-interactive-screen.png)

#### Configuration
For configuration example see [how-to-use-example](how-to-use-example)

#### More info on Wiki
https://github.com/m-szalik/dbpatch/wiki/gradle

#### License
Apache License 2.0

#### Problems and questions
In case of problems or questions contact me by [creating an issue](https://github.com/m-szalik/dbpatch/issues/new) on GitHub.

