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
# 9: Upload latest shadowJar to github wiki repository


# functions:
function replaceVersion {
    line=`cat build.gradle | grep release.sh`
    search=`echo $line | cut -d\' -f2`
    nline=`echo $line | sed -e "s/$search/$1/"`
    sed "s#$line#$nline#" < build.gradle > build.gradle.tmp
    mv build.gradle.tmp build.gradle
}

function gradleBuild {
    gradle $*
    if [ $? -gt 0 ]; then
        echo "Build problem!"
        exit 2
    fi
}
# 1:
branch=`git branch |grep '^* '`
if [ "$branch" != "* master" ]; then
    echo "Branch is not master!"
    exit 1
fi

# 2:
version_cur=`cat build.gradle | grep release.sh | cut -d\' -f2`
version_rel="${version_cur/-SNAPSHOT/}"
echo "Current version is $version_cur changing to $version_rel"
version_major=`echo $version_rel | cut -d. -f1`
version_minor=`echo $version_rel | cut -d. -f2`
version_minor_inc=$[version_minor +1]
version_sugested="$version_major.$version_minor_inc-SNAPSHOT"
echo -n "New snapshot version [$version_sugested]:"
read version_new;
if [ "x$version_new" == "x" ]; then
    version_new=$version_sugested
fi

replaceVersion $version_rel

# 3:
gradleBuild clean build shadowJar

# 4:
git add build.gradle
git commit -m "Release $version_rel"
git tag "dbpatch-$version_rel"

# 5:
gradleBuild clean build shadowJar
gradle clean build shadowJar uploadArchives
SJAR="/tmp/dbpatch-$version_rel.jar"
cp "dbpatch-app/build/libs/dbpatch-$version_rel.jar" "$SJAR"

# 6:
replaceVersion $version_new

# 7:
gradleBuild clean build shadowJar

# 8:
git add build.gradle
git commit -m "Snapshot $version_new"

# 9:
cdir=`pwd`
cd /tmp
git clone ssh://git@github.com/m-szalik/dbpatch.wiki.git
cd dbpatch.wiki/releases
mv "$SJAR" .
JAR_NAME=`basename "$SJAR"`
lni -s "$SJAR_NAME" "dbpatch-latest.jar"
git add .
git commit -m "Shadow jar for release $version_rel"
echo "Pushing wiki"
git push origin master
cd "$cdir"
rm -fr "/tmp/dbpatch.wiki"

# Done
echo "Push your changes (master branch and tags) on remote repo"

