#!/bin/sh 

if [ $# -lt 1 ] ; then
    echo "$0: Usage: <path to your geronimo source distribution> [options]"
    exit 1
fi

prg=$0
dirname=`dirname $prg`
path=$1/modules
options=$2
vmoptions=$3

SRCPATH=`find $path -name java | xargs echo | tr ' ' ':'`
CLASSPATH=`find ~/.maven/repository -name \*.jar | xargs echo | tr ' ' ':'`
$dirname/../bin/lint4j -J "-Xms100M -Xmx400M $vmoptions" $options -sourcepath $SRCPATH -classpath $CLASSPATH org.apache.geronimo.\*

