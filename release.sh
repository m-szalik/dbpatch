#!/bin/bash
#
# 1. Check if current branch is master
# 2. Change version from "SNAPSHOT to release
# 3. Build
# 4. Commit and create a tag
# 5. Build and deploy artifacts
# 6. Increase/enter version number and add "SNAPSHOT suffix
# 7. Build
# 8. Commit

# functions:
function replaceVersion {
    echo "Replace version $1"
}


# 1:
branch=`git branch |grep '^* '`
if [ "$branch" != "* master" ]; then
    echo Branch is not master
    exit 1
fi

# 2:
version_cur = `cat build.gradle |grep release.sh|cut -d\' -f2`
version_rel = "${version_cur/-SNAPSHOT/}"
echo "Current version is $version_cur changing to $version_rel"
replaceVersion $version_rel

# 3:
gradle clean build shadowJar

# 4:
git add build.gradle
git commit -m "Release $vesion_rel"
git tag "dbpatch-$vesion_rel"

# 5:
gradle clean build shadowJar
#gradle clean build shadowJar uploadArchives

# 6:
version_major = `echo $version_rel | cut -d. -f1`
version_minor = `echo $version_rel | cut -d. -f2`
version_minor_inc = $[version_minor + 1]
version_sugested = "$version_major.version_minor_inc-SNAPSHOT"
echo -n "New snapshot version [$version_sugested]:"
read version_new;
if [ "x$version_new" == "x" ]; then
    version_new = $version_sugested
fi
replaceVersion $version_new

# 7:
gradle clean build shadowJar

# 8:
git add build.gradle
git commit -m "Snapshot $vesion_new"

