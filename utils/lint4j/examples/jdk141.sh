#!/bin/sh

if [ $# -lt 1 ] ; then
    echo "$0: Usage: <path to your JDK 1.4.1 source distribution> [options]"
    exit 1
fi

prg=$0
dirname=`dirname $prg`
path=$1
options=$2
vmoptions=$3

java -Xmx1024M $vmoptions -jar $dirname/../jars/lint4j.jar $options -sourcepath $path java.\* sunw.\* javax.\* com.sun.\* org.\*