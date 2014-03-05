#!/bin/bash

base="http://central.maven.org/maven2/org/jsoftware/dbpatch/"
wget "$base" -q -O .list.txt
ver=`cat .list.txt |grep '/</a>'|grep -v 'Index of'|cut -d\" -f2|cut -d/ -f1`
rm .list.txt

echo "Current version is $ver"
wget "$base$ver/dbpatch-$ver.jar" -O dbpatch.jar


